/*********************************************************************
 *
 *  File Name: E131Writer.java
 *
 *  Description: Implements writer using E131 protocol
 *
 *  Date Created: Oct 6, 2011
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

import java.util.*;

//base 
import com.erissoft.base.ESException;

public class E131Writer extends Writer {

    public E131Writer() { 
        super(E131.BUF_LEN);
        packets_ = new E131[MAX_LIGHT_COUNT_];
        for (int i = 0; i < MAX_LIGHT_COUNT_; i++)
            packets_[i] = new E131((short) i);
    }

    protected Comparator<Light> getComparator_() { return new LightComparator(); }

    protected int write_() throws ESException {

        int curAddr = -1;

        portCount_ = 0;
        int port = 0;
        int lightCount = 0;
        Map<Integer, Light> map = new TreeMap<Integer, Light>();

        for (Light light : lights_) {
            if (light.getAddr() != curAddr) {
                saveBuf_(map);
                curAddr = light.getAddr();
                map.clear();
            }

            lightCount++;
            map.put(light.getTrack(), light);
        }

        saveBuf_(map);
        lights_.clear();

        return lightCount;

    }

    private void saveBuf_(Map<Integer, Light> lights) throws ESException {
        // find last non-empty light. We can skip anything past it
        int lastGood = -1;
        for (Light light : lights.values())
            if (!light.shouldSkip())
                lastGood = light.getTrack();

        if (lastGood < 0 || lights.isEmpty())
            return;

        int goodAddr = lights.get(lastGood).getAddr();
        
        if (goodAddr >= MAX_LIGHT_COUNT_)
            return;

        Collection<Integer> ports = new TreeSet<Integer>();
        buf_.clear();
        for (int i = 0; i <= lastGood; i++) {
            Light light = lights.get(i);
            if (light != null)  {
                ports.add(getPort(i));
                buf_.put((byte) (light.getColor().getRed().getValue() * 0x11));
                buf_.put((byte) (light.getColor().getGreen().getValue() * 0x11));
                buf_.put((byte) (light.getColor().getBlue().getValue() * 0x11));
                buf_.put((byte) light.getBrightness());
            } else
                buf_.putInt(0);
        }

        byte buf[] = packets_[goodAddr].getBytes(buf_.array(), buf_.position());
        buf_.clear();
        buf_.put(buf);

        portCount_ = ports.size();
        writeBuf_();
    }

    private E131 packets_[];

    private final static int MAX_LIGHT_COUNT_ = 50;

    private static class LightComparator implements Comparator<Light> {

        public int compare(Light l1, Light l2) {
            int ret = l1.getAddr() - l2.getAddr();

            if (ret == 0) 
                ret = getPort(l1.getTrack()) - getPort(l2.getTrack());

            if (ret == 0)
                ret = l1.getTrack() - l2.getTrack();

            return ret;
        }
    }

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
