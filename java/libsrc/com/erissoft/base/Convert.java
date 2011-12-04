/*********************************************************************
 *
 *  File Name: Convert.java
 *
 *  Description: Conversion routines
 *
 *  Date Created: Jun 17, 2005
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

public class Convert {

    public static boolean stringToBoolean(String str) {
        if (str == null)
            return false;
	
        str = str.toLowerCase();
        if (str.equals("1")
                || str.equalsIgnoreCase("on")
                || str.equalsIgnoreCase("true")
                || str.equalsIgnoreCase("yes"))
            return true;

        return false;
    }

    public static String toHexString(short val) {
        String ret = Integer.toHexString(val & 0xFFFF).toUpperCase();
        ret = (new String("0000")).substring(ret.length()) + ret;

        return ret;
    }

    public static String toHexString(byte buf[]) {
        return toHexString(buf, buf.length);
    }

    public static String toHexString(byte buf[], int len) {
        String ret = "";
        
        for (int i = 0; i < len; i++) 
            ret += " " + toHexString(buf[i]);

        return ret.isEmpty() ? "" : ret.substring(1);
    }

    public static String toHexString(byte val) {
        String digit = Integer.toHexString(val & 0xFF).toUpperCase();
        return (digit.length() > 1 ? "" : "0") + digit;
    }

    public static long stringToLong(String val) {
        try {
            int base = 10;

            if (val != null) {
                if (val.startsWith("0x")) {
                    base = 16;
                    val = val.substring(2);
                }

                return Long.parseLong(val, base);
            }
        } catch (NumberFormatException e) {}

        return 0;
    }

}
