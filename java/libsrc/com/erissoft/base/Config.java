/*********************************************************************
 *
 *  File Name: Config.java
 *
 *  Description: Config file interface
 *
 *  Date Created: Mar 9, 2005
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
 *  
 *  Copyright (C) 2005, Erissoft
 *  
 *  This file is part of Erissoft common framework
 *
 *  HAM is free software; you can redistribute it and/or
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
package com.erissoft.base;

import java.util.List;

//log4j
import org.apache.log4j.*;

public enum Config {

    INSTANCE;

    Config() {}

    public static synchronized void setReader(ConfigReader reader) {
        INSTANCE.reader_ = reader;
        INSTANCE.init_();
    }

    public ConfigReader getReader() { return reader_; }

    private void init_() {
        try {
            String fmtString = getStrValNoEx("engine.loggers.format_string");
            if (fmtString == null || fmtString.isEmpty()) {
                System.err.println("Couldn't initialize logger: format string is empty");
                System.exit(-1);
            }

            PatternLayout layout = new PatternLayout(fmtString); 
            BasicConfigurator.configure(new ConsoleAppender(layout));

            Logger.getLogger("erissoft").setLevel(Level.toLevel(getStrVal("engine.loggers.output_level")));

            for (String name : getMultVals("engine.loggers.subloggers", "logger.name")) {
                List<String> level = Config.INSTANCE.selectMultValNoEx("engine.loggers.subloggers", 
                        "logger.name", name, "output_level");
                if (level != null)
                    Logger.getLogger(name).setLevel(Level.toLevel(level.get(0)));
            }
        } catch (Exception e) {
            System.err.println("Couldn't initialize logger");
            System.exit(-1);
        }
    }


    public String getStrVal(String name) throws ESException {
        try {
            return reader_.getVal(name);
        } catch (ConfigValNotFoundException e) {
            throw new ESException(e);
        }
    }

    public String getStrValNoEx(String name) {
        return getStrValNoEx(name, null);
    }

    public String getStrValNoEx(String name, String defVal) {
        try {
            return reader_.getVal(name);
        } catch (ConfigValNotFoundException e) {
            return defVal;
        }
    }

    public boolean getBoolVal(String name) throws ESException {
        return Convert.stringToBoolean(getStrVal(name));
    }

    public boolean getBoolValNoEx(String name) {
        return Convert.stringToBoolean(getStrValNoEx(name));
    }

    public int getIntVal(String name) throws ESException {
        return Integer.parseInt(getStrVal(name));
    }

    public int getIntValNoEx(String name, int defVal) {
        return Integer.parseInt(getStrValNoEx(name, String.valueOf(defVal)));
    }

    public int getIntValNoEx(String name) {
        return Integer.parseInt(getStrValNoEx(name, "0"));
    }

    public double getDoubleVal(String name) throws ESException {
        return Double.parseDouble(getStrVal(name));
    }

    public double getDoubleValNoEx(String name, double defVal) {
        return Double.parseDouble(getStrValNoEx(name, String.valueOf(defVal)));
    }

    public double getDoubleValNoEx(String name) {
        return Double.parseDouble(getStrValNoEx(name, "0"));
    }

    public List<String> getMultVals(String parentName, String childName) throws ESException {
        try { 
            return reader_.getMultVals(parentName, childName);
        } catch (ConfigValNotFoundException e) {
            throw new ESException(e);
        }
    }

    public List<String> getMultValsNoEx(String parentName, String childName) {
        try {
            return reader_.getMultVals(parentName, childName);
        } catch (ConfigValNotFoundException e) {
            return null;
        }
    }

    public List<String> selectMultVal(String parentName, String selectName, 
            String selectVal, String childName) throws ESException {
        try {
            return reader_.selectMultVal(parentName, selectName, 
                    selectVal, childName);
        } catch (ConfigValNotFoundException e) {
            throw new ESException(e);
        }
    }

    public List<String> selectMultValNoEx(String parentName, String selectName, 
            String selectVal, String childName) {
        try {
            return reader_.selectMultVal(parentName, selectName,
                    selectVal, childName);
        } catch (ConfigValNotFoundException e) {
            return null;
        }	    
    }

    public void saveFile() throws ESException { reader_.saveFile(); }
    private ConfigReader reader_;

    //private Logger logger_ = Logger.getLogger("erissoft.lib.com.base");
}
