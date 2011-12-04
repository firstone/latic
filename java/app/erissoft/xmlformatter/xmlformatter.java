/*********************************************************************
 *
 *  File Name: xmlformatter.java
 *
 *  Description: Formats XML file
 *
 *  Date Created: Jun 15, 2011
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
 *  
 *  This file is part of Home Automation Module (HAM).
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
package erissoft.xmlformatter;

//log4j
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//base
import com.erissoft.base.ESException;

//xml
import com.erissoft.xml.Parser;
import com.erissoft.xml.XMLUtils;

public class xmlformatter {

    public static void main(String args[]) {
        if (args.length != 1)
            printUsage();

        new xmlformatter(args[0]);
    }

    public static void printUsage() {
        System.err.println("Usage: java erissoft.app.xmlformatter <xml file name>");
        System.exit(-1);
    }

    public xmlformatter(String xmlFile) {
        try {
            Logger.getLogger("erissoft").setLevel(Level.INFO);

            Parser parser = new Parser(xmlFile, false);
            
            XMLUtils.writeXMLFile(parser.getDocument(), xmlFile);
        } catch (ESException e) {
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
