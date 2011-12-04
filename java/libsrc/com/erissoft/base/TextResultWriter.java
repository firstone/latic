/*********************************************************************
 *
 *  File Name: TextResultWriter.java
 *
 *  Description: Text implmentation for ResultWriter interface
 *
 *  Date Created: Apr 5, 2011
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
 *  
 *  Copyright (C) 2011, Erissoft
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

public class TextResultWriter implements ResultWriter {

    public TextResultWriter() {
        buf_ = new StringBuilder();
    }

    public void printElement(String name, String val) {
        buf_.append(name).append(": ").append(val).append('\n');
    }

    public void printAttr(String name, String val) throws ESException {
        buf_.append(name).append("=").append(val).append('\n');
    }

    public void printComment(String val) {
        buf_.append(val).append('\n');
    }

    public void startElement(String name) {
        buf_.append(name).append(": ");
    }

    public void finishElement(String name) {
        buf_.append('\n');
    }

    public void print(String val) {
        buf_.append(val);
    }

    public String getBuffer() {
        String ret = buf_.toString();
        reset();

        return ret;
    }

    public void reset() {
        buf_ = new StringBuilder();
    }

    private StringBuilder buf_;
}

