/*********************************************************************
 *
 *  File Name: LightString.java
 *
 *  Description: String of lights registry
 *
 *  Date Created: Oct 13, 2011
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
import com.erissoft.base.Config;
import com.erissoft.base.ESException;

public enum LightStringManager {

    INSTANCE;


    LightStringManager() {
        map_ = new HashMap<String, LightString>();
    }

    public void init() throws ESException {

        List<String> names = Config.INSTANCE.getMultVals("app.lights", "string.name");
        List<String> desc = Config.INSTANCE.getMultVals("app.lights", "string.desc");
        List<String> pins = Config.INSTANCE.getMultVals("app.lights", "string.pin");
        List<String> counts = Config.INSTANCE.getMultVals("app.lights", "string.count");

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            try {
                map_.put(name, new LightString(name, desc.get(i), 
                            Integer.valueOf(pins.get(i)), Integer.valueOf(counts.get(i))));
            } catch (NumberFormatException e) {
                throw new ESException("Light string " + name + " has invalid pin or light count");
            }
        }

    }

    public LightString get(String name) throws ESException {
        LightString ls = map_.get(name);
        if (ls == null)
            throw new ESException("Light string " + name + " not found");

        return ls;
    }

    public Collection<LightString> getAll() { 
        return new LinkedList<LightString>(map_.values());
    }

    private Map<String, LightString> map_;

}
