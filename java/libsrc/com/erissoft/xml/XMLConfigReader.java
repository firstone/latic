/*********************************************************************
 *
 *  File Name: XMLConfigReader.java
 *
 *  Description: Implementation of ConfigReader reading from xml file
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

//xerces
import org.w3c.dom.*;

//base
import com.erissoft.base.*;

public class XMLConfigReader implements ConfigReader {

    public XMLConfigReader(String fileName, boolean validate) throws ESException {
        fileName_ = fileName;
        parser_ = new Parser(fileName, validate);
    }

    public Document getDocument() { return parser_.getDocument(); }

    public Element getDocumentElement() { return parser_.getElement(); }

    public List<String> getMultVals(String parentName, String childName) throws ConfigValNotFoundException {
        Collection<Node> parentNodes = getParentNode_(parser_.getElement(), parentName);
        if (parentNodes.isEmpty())
            throw new ConfigValNotFoundException(parentName);

        int pos = childName.indexOf('.');
        if (pos < 0)
            throw new ConfigValNotFoundException(childName);

        String multName = childName.substring(0, pos);
        String endNodeName = childName.substring(pos + 1);

        List<String> ret = new LinkedList<String>();

        for (Node parentNode : parentNodes) {
            NodeList nl = ((Element) parentNode).getChildNodes();

            for (int i = 0; i < nl.getLength(); i++)
                if (nl.item(i).getNodeName().equals(multName))
                    try {
                        ret.add(getVal_(nl.item(i), endNodeName));
                    } catch (ConfigValNotFoundException e) {
                        ret.add(null);
                    }
        }

        return ret;
    }

    public List<String> selectMultVal(String parentName, String selectName, 
            String selectVal, String childName) throws ConfigValNotFoundException {

        int pos = selectName.indexOf('.');
        if (pos < 0)
            throw new ConfigValNotFoundException(selectName);

        String multName = selectName.substring(0, pos);
        String endNodeName = selectName.substring(pos + 1);

        Collection<Node> parentNodes = new LinkedList<Node>();
        for (Node node : getParentNode_(parser_.getElement(), parentName)) {
            NodeList nl = node.getChildNodes();

            int i = 0;
            while (i < nl.getLength()) {
                Node n = nl.item(i++);
                if (n.getNodeName().equals(multName)
                        && getVal_(n, endNodeName).equals(selectVal))
                    parentNodes.add(n);
            }
        }

        if (parentNodes.isEmpty())
            throw new ConfigValNotFoundException(parentName + "." + selectName);

        List<String> ret = new LinkedList<String>();
        for (Node parentNode : parentNodes)
            ret.addAll(getVals_(parentNode, childName));

        return ret;
    }

    public String getVal(String name) throws ConfigValNotFoundException {
        return getVal_(parser_.getElement(), name);
    }

    private String getVal_(Node node, String name) throws ConfigValNotFoundException {
        return getVals_(node, name).get(0);
    }

    private List<String> getVals_(Node node, String name) throws ConfigValNotFoundException {

        String parentName = null, endNodeName = null;
        Collection<Node> parentNodes = null;

        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            parentName = name.substring(0, pos);
            endNodeName = name.substring(pos + 1);
            parentNodes = getParentNode_(node, parentName);

            if (parentNodes.isEmpty())
                throw new ConfigValNotFoundException(name);
        } else {
            endNodeName = name;
            parentNodes = new LinkedList<Node>();
            parentNodes.add(node);
        }

        List<String> ret = new LinkedList<String>();
        for (Node parentNode : parentNodes) {
            Collection<Node> nodes = XMLUtils.getNodesByName(parentNode, endNodeName);
            if (!nodes.isEmpty())
                for (Node n : nodes)
                    ret.add(XMLUtils.getFirstTextNodeVal(n));
            else {
                Node n = XMLUtils.getAttrByName(parentNode, endNodeName);
                if (n != null)
                    ret.add(n.getNodeValue().trim());
            }
        }

        if (ret.isEmpty())
            throw new ConfigValNotFoundException(name);

        return ret;
    }

    private Collection<Node> getParentNode_(Node node, String name) throws ConfigValNotFoundException {
        int pos = name.lastIndexOf('.');
        if (pos >= 0) {
            try {
                node = XMLUtils.getParentNode(node, name.substring(0, pos));
            } catch (ESException e) {
                throw new ConfigValNotFoundException(name);
            }
            name = name.substring(pos + 1);
        }

        return XMLUtils.getNodesByName(node, name);
    }

    public void saveFile() throws ESException {
        XMLUtils.writeXMLFile(parser_.getDocument(), fileName_);
    }

    private String fileName_;
    private Parser parser_;
}
