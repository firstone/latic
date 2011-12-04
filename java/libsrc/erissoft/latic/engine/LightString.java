/*********************************************************************
 *
 *  File Name: LightString.java
 *
 *  Description: String of lights object
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

public class LightString {

    public LightString(String name, String desc, int pin, int count) { 
        name_ = name;
        desc_ = desc;
        pin_ = pin;
        count_ = count;
    }

    public String getName() { return name_; }

    public String getDesc() { return desc_; }

    public int getPin() { return pin_; }

    public int getCount() { return count_; }

    public String toString() {
        return "String: " + desc_ + " (pin: " + pin_ + ", count: " + count_ + ")";
    }

    private final String name_, desc_;
    private final int pin_, count_;

}
