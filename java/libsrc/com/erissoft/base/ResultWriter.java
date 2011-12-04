/*********************************************************************
 *
 *  File Name: ResultWriter.java
 *
 *  Description: Interface for writing results to the client
 *
 *  Date Created: Apr 14, 2005
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

public interface ResultWriter {

    public void printElement(String name, String val);
    
    public void printAttr(String name, String val) throws ESException;

    public void printComment(String val);

    public void startElement(String name);

    public void finishElement(String name);

    public void print(String val);

    public String getBuffer() throws ESException;

    public void reset();

}
