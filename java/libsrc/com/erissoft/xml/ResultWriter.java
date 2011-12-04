/*********************************************************************
 *
 *  File Name: ResultWriter.java
 *
 *  Description: Implmentation for ResultWriter interface
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
package com.erissoft.xml;

import java.io.StringReader;
import java.io.StringWriter;

//base
import com.erissoft.base.ESException;

public class ResultWriter implements com.erissoft.base.ResultWriter {

    public ResultWriter() {
        elemOpen_ = false;
        buf_ = new StringBuilder();
    }

    public void printElement(String name, String val) {
        startElement(name);
        print(val);
        finishElement(name);
    }

    public void printAttr(String name, String val) throws ESException {
        if (!elemOpen_)
            throw new ESException("Can't write attribute at this time", true);

        buf_.append(" " + name + "=\"" + val + "\"");
    }

    public void printComment(String val) {
        buf_.append("<!--");
        buf_.append(val);
        buf_.append("-->");
    }

    public void startElement(String name) {
        closeElement_();

        buf_.append("<" + name);
        elemOpen_ = true;
    }

    public void finishElement(String name) {
        if (elemOpen_) {
            buf_.append("/>");
            elemOpen_ = false;
        } else
            buf_.append("</" + name + ">");
    }

    public void print(String val) {
        closeElement_();

        buf_.append(val);
    }

    public String getBuffer() throws ESException {
        Parser parser = new Parser(new StringReader(buf_.toString()));
        StringWriter writer = new StringWriter();
        XMLUtils.writeXML(parser.getDocument(), writer);
        String ret = writer.toString();
        reset();

        return ret;
    }

    public void reset() {
        buf_ = new StringBuilder();
    }

    private void closeElement_() {
        if (elemOpen_)
            buf_.append(">");

        elemOpen_ = false;
    }

    private boolean elemOpen_;
    private StringBuilder buf_;
}

