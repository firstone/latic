/*********************************************************************
 *
 *  File Name: ConfigReader.java
 *
 *  Description: Config file reader interface
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

public interface ConfigReader {

    public String getVal(String name) throws ConfigValNotFoundException;

    public List<String> getMultVals(String parentName, String childName) 
	throws ConfigValNotFoundException;

    public List<String> selectMultVal(String parentName, String selectName, 
				String selectVal, String childName) 
	throws ConfigValNotFoundException;

    public void saveFile() throws ESException;

}
