/*********************************************************************
 *
 *  File Name: XMLUtils.java
 *
 *  Description: Commonly used XML functions
 *
 *  Date Created: Mar 15, 2005
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

import java.io.FileWriter;
import java.io.Writer;

import java.util.*;

//xerces
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

//base
import com.erissoft.base.ESException;

public class XMLUtils {

    public static String getFirstTextNodeVal(Node node, String name) {
        node = getNodeByName(node, name);
        if (node == null)
            return null;

        return getFirstTextNodeVal(node);
    }

    public static String getFirstTextNodeVal(Node node) {
        node = getFirstTextNode(node);
        if (node != null)
            return node.getNodeValue();

        return null;
    }

    public static Node getFirstTextNode(Node node) {
        NodeList nl = ((Element) node).getChildNodes();

        Node ret = null;
        int i = 0;
        while (ret == null && i < nl.getLength()) {
            Node n = nl.item(i++);
            if (n.getNodeType() == Node.TEXT_NODE)
                ret = n;
        }

        return ret;
    }

    public static String getAttr(Node node, String name) throws ESException {
        Attr attr = ((Element) node).getAttributeNode(name);
        if (attr == null)
            throw new ESException("Attribute \"" + name + "\" not "
                    + "found in \"" + node.getNodeName() + "\"");

        return attr.getNodeValue();
    }

    public static String getAttrNoEx(Node node, String name) {
        Attr attr = ((Element) node).getAttributeNode(name);
        if (attr == null)
            return null;

        return attr.getNodeValue();
    }

    public static Map<String, String> getAttrList(Node node) {
        Map<String, String> map = new HashMap<String, String>();

        NamedNodeMap list = node.getAttributes();
        if (list != null) 
            for (int i = 0; i < list.getLength(); i++) {
                Attr attr = (Attr) list.item(i);
                map.put(attr.getName(), attr.getValue());
            }

        return map;
    }

    public static Node getAttrByName(Node node, String name) {
        if (node == null)
            return null;

        return ((Element) node).getAttributeNode(name);
    }

    public static Node getNodeByName(Node node, String name) {
        if (node == null) 
            return null;

        NodeList nl = node.getChildNodes();

        Node n = null;
        int i = 0;
        while (n == null && i < nl.getLength()) {
            if (nl.item(i).getNodeName().equals(name))
                n = nl.item(i);
            i++;
        }

        return n;
    }
     
    public static Node getParentNodeNoEx(Node node, String name) {
        for (String str : name.split("\\."))
            if (node == null)
                return null;
            else
                node = getNodeByName(node, str);

        return node;
    }

    public static Node getParentNode(Node node, String name) throws ESException {
        node = getParentNodeNoEx(node, name);
        if (node == null)
            throw new ESException("Node " + name + " not found");

        return node;
    }
        
    public static Collection<Node> getNodesByName(Node node, String name) {
        Collection<Node> list = new LinkedList<Node>();

        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
            if (nl.item(i).getNodeName().equals(name))
                list.add(nl.item(i));

        return list;
    }

    public static Collection<Node> getChildNodes(Node node) {
        Collection<Node> list = new LinkedList<Node>();

        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
                list.add(nl.item(i));

        return list;
    }

    public static void removeChildNodes(Node node) {
        NodeList nl = node.getChildNodes();

        //for (int i = 0; i < nl.getLength(); i++) 
        while (nl.getLength() > 0)
            node.removeChild(nl.item(0));

    }

    public static void removeNodesByName(Node node, String name) {
        NodeList nl = node.getChildNodes();

        int i = 0;
        while (i < nl.getLength()) 
            if (nl.item(i).getNodeName().equals(name))
                node.removeChild(nl.item(i));
            else 
                i++;
    }

    public static void removeTextNodes(Node node) {
        NodeList nl = node.getChildNodes();

        int i = 0;
        while (i < nl.getLength()) 
            if (nl.item(i).getNodeType() == Node.TEXT_NODE)
                node.removeChild(nl.item(i));
            else
                i++;
    }

    public static void writeXMLFile(Document doc, String fileName) throws ESException {
        try {
            Writer fout = new FileWriter(fileName);
            writeXML(doc, fout);
            fout.close();
        } catch (Exception e) {
            throw new ESException(e);
        }
    }

    public static void writeXML(Document doc, Writer stream) throws ESException {
        try {
            DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) reg.getDOMImplementation("LS");

            LSSerializer writer = impl.createLSSerializer();
            writer.getDomConfig().setParameter("format-pretty-print", true);
            LSOutput out = impl.createLSOutput();

            out.setCharacterStream(stream);
            writer.write(doc, out);
        } catch (Exception e) {
            throw new ESException(e);
        }
    }
}
