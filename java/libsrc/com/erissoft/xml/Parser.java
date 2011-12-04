/*********************************************************************
 *
 *  File Name: Parser.java
 *
 *  Description: Parses xml file
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
package com.erissoft.xml;

import java.io.*;
import javax.xml.parsers.*;

//xerces
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;

//base
import com.erissoft.base.*;

public class Parser {

    public Parser(String fileName, boolean validate) throws ESException {
        try {
            DOMParser parser = new DOMParser();
            parser.setErrorHandler(new DefaultErrorHandler());
            parser.setFeature("http://xml.org/sax/features/validation", validate);
            parser.setFeature("http://apache.org/xml/features/validation/schema", validate);
            parser.parse(fileName);

            doc_ = parser.getDocument();
            docElem_ = doc_.getDocumentElement();

        } catch (Exception e) {
            throw new ESException(e.toString());
        }
    }

    public Parser(StringReader reader) throws ESException {
        try {
            DOMParser parser = new DOMParser();
            parser.setErrorHandler(new ClientErrorHandler());
            parser.parse(new InputSource(reader));

            doc_ = parser.getDocument();
            docElem_ = doc_.getDocumentElement();

        } catch (Exception e) {
            throw new ESException(e.toString());
        }
    }

    public Document getDocument() { return doc_; }

    public Element getElement() { return docElem_; }

    private Element docElem_;
    private Document doc_;
}
