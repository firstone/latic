/*********************************************************************
 *
 *  File Name: NativeWriter.java
 *
 *  Description: Implements writer using native LATIC protocol
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

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

//base 
import com.erissoft.base.ESException;

public class NativeWriter extends Writer {

    public NativeWriter() { 
        // 5 bytes per each port pin + 1 byte length
        super(PORT_COUNT * (PORT_PIN_COUNT * 5 + 1)); 
    }

    protected Comparator<Light> getComparator_() { return new LightComparator(); }

    protected int write_() throws ESException {
        portCount_ = 0;
        int port = 0;
        int lightCount = 0;
        Collection<Light> list = new LinkedList<Light>();

        buf_.clear();
        for (Light light : lights_)
            if (!light.shouldSkip()) {
                if (getPort(light.getTrack()) != port) {
                    portCount_++;
                    port = getPort(light.getTrack());
                    saveBuf_(list);
                    list.clear();
                }

                lightCount++;
                list.add(light);
            }

        saveBuf_(list);
        lights_.clear();

        writeBuf_();

        return lightCount;

    }

    private void saveBuf_(Collection<Light> lights) {
        if (lights.size() > 0) {
            buf_.put((byte) lights.size());
            for (Light light : lights) {
                buf_.put((byte) light.getTrack());
                buf_.putInt(light.getData());
            }
        }
    }

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

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
