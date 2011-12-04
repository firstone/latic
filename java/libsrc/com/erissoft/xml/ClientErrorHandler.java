/*********************************************************************
 *
 *  File Name: ClientErrorHandler.java
 *
 *  Description: Server side xml error handler
 *
 *  Date Created: Apr 5, 2005
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
package com.erissoft.xml;

import java.io.IOException;
import java.io.OutputStream;

//xerces
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ClientErrorHandler implements ErrorHandler {

    public ClientErrorHandler() {
    }

    public void warning(SAXParseException e) throws SAXException {
    }
    
    public void error(SAXParseException e) throws SAXException {
    }
    
    public void fatalError(SAXParseException e) throws SAXException {
    }    
}

