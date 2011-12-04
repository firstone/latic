/*********************************************************************
 *
 *  File Name: E131.java
 *
 *  Description: Converts message into E1.31 packet
 *
 *  Date Created: Oct 5, 2011
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
 *  
 *  Copyright (C) 2011, Erissoft
 *  
 *  This file is part of LED Animation Controller (LATIC)
 *
 *  LATIC is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 *********************************************************************/
package erissoft.latic.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.Arrays;
import java.util.UUID;

//base 
import com.erissoft.base.Config;
import com.erissoft.base.Convert;
import com.erissoft.base.ESException;

public class E131 {

    public E131(short universe) { 
        buf_ = ByteBuffer.allocate(BUF_LEN);
        buf_.order(ByteOrder.BIG_ENDIAN);
        universe_ = universe;
        seqNum_ = (byte) 0;
    }

    public byte[] getBytes(byte buf[]) throws ESException {
        return getBytes(buf, buf.length);
    }

    public byte[] getBytes(byte buf[], int len) throws ESException {
        if (root_ == null) {
            root_ = new RootLayer(this);
            frame_ = new FramingLayer(this);
            dmp_ = new DMPLayer(this);
        }


        buf_.clear();

        root_.write();
        frame_.write();
        dmp_.write(buf, len);

        buf_.flip();

        root_.setLength();
        frame_.setLength();
        dmp_.setLength();

        byte ret[] = new byte[buf_.limit()];
        buf_.rewind();
        buf_.get(ret);

        return ret;
    }

    private ByteBuffer buf_;
    private short universe_;
    private byte seqNum_;
    private RootLayer root_;
    private FramingLayer frame_;
    private DMPLayer dmp_;

    private static class Layer {
        public Layer(E131 packet) {
            packet_ = packet;
        }

        protected void setLength_(int offset, int len) throws ESException {
            if (packet_.buf_.limit() <= offset)
                throw new ESException("Invalid buffer length: " + packet_.buf_.limit());

            packet_.buf_.position(offset);
            packet_.buf_.putShort((short) (FLAGS_ | (len & 0xFFF)));
        }

        protected E131 packet_;
    }

    private static class RootLayer extends Layer {

        public RootLayer(E131 packet) { super(packet); }

        public void write() {
            packet_.buf_.putShort(PREAMBLE_SIZE_).putShort(POSTAMBLE_SIZE_).put(ACN_PACKET_ID_)
                .putShort((short) 0).putInt(VECTOR_).putLong(SENDER_CID_MSB_).putLong(SENDER_CID_LSB_);
        }

        public void setLength() throws ESException {
            setLength_(LEN_OFFSET_, packet_.buf_.limit() - LEN_OFFSET_);
        }

        private final static int LEN_OFFSET_ = 16;

        private final static short PREAMBLE_SIZE_ = (short) 0x0010;
        private final static short POSTAMBLE_SIZE_ = (short) 0x0000;

        private final static byte ACN_PACKET_ID_[] = {
            (byte) 0x41, (byte) 0x53, (byte) 0x43, (byte) 0x2d, (byte) 0x45,
            (byte) 0x31, (byte) 0x2e, (byte) 0x31, (byte) 0x37, (byte) 0x00,
            (byte) 0x00, (byte) 0x00 };

        private final static int VECTOR_ = 0x00000004;

        private final static long SENDER_CID_MSB_, SENDER_CID_LSB_;

        static {
            long msb = 0, lsb = 0;

            try {
                UUID uuid = UUID.fromString(Config.INSTANCE.getStrValNoEx("app.e131.sender_uuid"));
                msb = uuid.getMostSignificantBits();
                lsb = uuid.getLeastSignificantBits();
            } catch (Exception e) {}

            SENDER_CID_MSB_ = msb;
            SENDER_CID_LSB_ = lsb;

        }

    }

    private static class FramingLayer extends Layer {

        public FramingLayer(E131 packet) { super(packet); }

        public void write() throws ESException {
            if (packet_.buf_.position() != START_POS_)
                throw new ESException("Invalid start position: " + packet_.buf_.position());

            packet_.buf_.putShort((short) 0).putInt(VECTOR_).put(SOURCE_NAME_).put(DEF_PRIORITY_)
                .putShort((short) 0).put(packet_.seqNum_++).put((byte) 0).putShort(packet_.universe_);
        }

        public void setLength() throws ESException {
            setLength_(LEN_OFFSET_, packet_.buf_.limit() - LEN_OFFSET_);
        }

        private final static int LEN_OFFSET_ = 38;
        private final static int START_POS_ = 38;
        private final static int VECTOR_ = 0x00000002;
        private final static byte DEF_PRIORITY_ = (byte) 100;

        private final static byte SOURCE_NAME_[];

        static {
            byte nameArray[] = new byte[64];
            Arrays.fill(nameArray, (byte) 0);

            try {
                byte name[] = Config.INSTANCE.getStrValNoEx("app.e131.source_name", "").getBytes("UTF-8");
                System.arraycopy(name, 0, nameArray, 0, name.length > 63 ? 63 : name.length);
            } catch (Exception e) {}

            SOURCE_NAME_ = nameArray;
        }
    }

    private static class DMPLayer extends Layer {

        public DMPLayer(E131 packet) { super(packet); }

        public void write(byte data[], int len) throws ESException {
            if (packet_.buf_.position() != START_POS_)
                throw new ESException("Invalid start position: " + packet_.buf_.position());

            packet_.buf_.putShort((short) 0).put(VECTOR_).put(ADDR_TYPE_DATA_TYPE_)
                .putShort(FIRST_PROPERTY_ADDR_).putShort(ADDR_INCREMENT_)
                .putShort((short) (len + 1)).put((byte) 0).put(data, 0, len);
        }

        public void setLength() throws ESException {
            setLength_(LEN_OFFSET_, packet_.buf_.limit() - LEN_OFFSET_);
        }

        private final static int LEN_OFFSET_ = 115;
        private final static int START_POS_ = 115;
        private final static byte VECTOR_ = (byte) 0x02;
        private final static byte ADDR_TYPE_DATA_TYPE_ = (byte) 0xA1;
        private final static short FIRST_PROPERTY_ADDR_ = (short) 0x0000;
        private final static short ADDR_INCREMENT_ = (short) 0x0001;

    }

    public final static int BUF_LEN = 638;

    private final static short FLAGS_ = 0x7000;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
