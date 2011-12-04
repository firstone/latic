/*********************************************************************
 *
 *  File Name: Writer.java
 *
 *  Description: Sends lights to the controller
 *
 *  Date Created: Sep 26, 2011
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

import java.io.IOException;

import java.net.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.*;

//base 
import com.erissoft.base.Config;
import com.erissoft.base.Convert;
import com.erissoft.base.ESException;

public abstract class Writer {

    protected Writer(int size) { 
        buf_ = ByteBuffer.allocate(size);
        buf_.order(ByteOrder.BIG_ENDIAN);
        lastSent_ = System.nanoTime();
        lights_ = new TreeSet<Light>(getComparator_());
        lastDelay_ = 0;
        portBuf_ = new byte[PORT_COUNT * (1 + PORT_PIN_COUNT * 5)];
        inited_ = false;
    }

    public int write() throws ESException {
        if (socket_ == null)
            throw new ESException("Writer is not initialized");

        return write_();

    }

    public static int getPort(int track) {
        Integer port = PORTS_.get(track);
        if (port == null)
            return 0;

        return port;
    }

    public static void printPinConfig() {
        Map<Integer, Collection<Integer>> map = new HashMap<Integer, Collection<Integer>>();

        for (Map.Entry<Integer, Integer> entry : PORTS_.entrySet()) {
            Collection<Integer> list = map.get(entry.getValue());
            if (list == null) {
                list = new LinkedList<Integer>();
                map.put(entry.getValue(), list);
            }

            list.add(entry.getKey());
        }

        logger_.info("Printing Arduino pins configuration for " + ARDUINO_TYPE_);

        for (Map.Entry<Integer, Collection<Integer>> entry : map.entrySet()) {
            String s = "Bank: " + entry.getKey() + " Pins: ";
            for (Integer pin : entry.getValue())
                s += pin + ", ";

            logger_.info(s.substring(0, s.length() - 2));
        }
    }

    public void init() throws ESException {
        if (!inited_) {
            if (HOST_NAME_ == null)
                throw new ESException("Host name is not set up");

            if (PORT_ == 0)
                throw new ESException("Port is not set up");

            try {
                addr_ = InetAddress.getByName(HOST_NAME_);
            } catch (UnknownHostException e) {
                throw new ESException(e);
            }

            try {
                socket_ = new DatagramSocket();
                packet_ = new DatagramPacket(portBuf_, portBuf_.length, addr_, PORT_);
            } catch (SocketException e) {
                throw new ESException(e);
            }

            inited_ = true;
        }
    }

    public void addLight(Light light) { lights_.add(light); }

    public void idle() throws ESException {
        if (inited_) {
            buf_.clear();
            buf_.put((byte) 0xFF);

            writeBuf_();
        }
    }

    protected abstract Comparator<Light> getComparator_();

    protected abstract int write_() throws ESException;

    protected void writeBuf_() throws ESException {
        if (buf_.position() > 0) {
            logger_.debug(Convert.toHexString(buf_.array(), buf_.position()));

            logger_.debug("Port count: " + portCount_ + " delay: " + lastDelay_ / 1000);

            try {
                while (System.nanoTime() - lastSent_ < lastDelay_)
                    Thread.sleep(0, 1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            lastDelay_ = PORT_DELAY_ * portCount_ + SEND_DELAY_ + BYTE_SEND_DELAY_ * buf_.position();

            packet_.setData(buf_.array(), 0, buf_.position());
            try {
                socket_.send(packet_);
                socket_.disconnect();
            } catch (IOException e) {
                throw new ESException(e);
            }

            lastSent_ = System.nanoTime();

        }

    }

    protected ByteBuffer buf_;
    protected int portCount_;
    protected Collection<Light> lights_;

    private DatagramPacket packet_;
    private DatagramSocket socket_;
    private long lastSent_;
    private InetAddress addr_;
    private byte portBuf_[];
    private boolean inited_;

    private long lastDelay_;

    private static class LightComparator implements Comparator<Light> {

        public int compare(Light l1, Light l2) {
            int ret = getPort(l1.getTrack()) - getPort(l2.getTrack());
            if (ret == 0) 
                ret = l1.getTrack() - l2.getTrack();

            if (ret == 0)
                ret = l1.getAddr() - l2.getAddr();

            return ret;
        }
    }

    public final static int PORT_PIN_COUNT = 8;
    public final static int PORT_COUNT;

    public final static String PROTOCOL;

    private final static long SEND_DELAY_, PORT_DELAY_, BYTE_SEND_DELAY_;
    private final static String HOST_NAME_;
    private final static int PORT_;
    private final static String ARDUINO_TYPE_;

    private static Map<Integer, Integer> PORTS_;

    static {
        PORT_DELAY_ = Config.INSTANCE.getIntValNoEx("app.writer.port_delay", 920) * 1000;
        SEND_DELAY_ = Config.INSTANCE.getIntValNoEx("app.writer.send_delay", 220) * 1000;
        BYTE_SEND_DELAY_ = Config.INSTANCE.getIntValNoEx("app.writer.byte_send_delay", 12) * 1000;
        HOST_NAME_ = Config.INSTANCE.getStrValNoEx("app.writer.hostname");
        PORT_ = Config.INSTANCE.getIntValNoEx("app.writer.port");
    
        int portCount = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        String type = "";

        try {
            type = Config.INSTANCE.getStrVal("engine.writer.arduino_type");

            List<String> pins = Config.INSTANCE.selectMultVal("engine.writer", 
                    "arduino.type", type, "port.pin");
            List<String> ports = Config.INSTANCE.selectMultVal("engine.writer", 
                    "arduino.type", type, "port.number");
            
            for (int i = 0; i < pins.size(); i++)
                map.put(Integer.valueOf(pins.get(i)), Integer.valueOf(ports.get(i)));

            PORTS_ = map;

            Collections.sort(ports);

            portCount = Integer.valueOf(ports.get(ports.size() - 1));

        } catch (ESException e) {
        }

        PORT_COUNT = portCount;
        PORTS_ = map;
        ARDUINO_TYPE_ = type;

        PROTOCOL = Config.INSTANCE.getStrValNoEx("app.writer.protocol", "e131");
    }

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
