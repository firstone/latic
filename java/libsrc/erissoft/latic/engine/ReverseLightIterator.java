/*********************************************************************
 *
 *  File Name: ReverseLightIterator.java
 *
 *  Description: Reverse light string iterator 
 *
 *  Date Created: Oct 12, 2011
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

public class ReverseLightIterator implements LightIterator {

    public ReverseLightIterator(int lightCount) { 
        this(lightCount, 0);
    }

    public ReverseLightIterator(int lightCount, int endLight) { 
        lightCount_ = lightCount;
        endLight_ = endLight;

        curLight_ = lightCount_ - 1;
    }

    public boolean hasNext() { return curLight_ >= endLight_; }

    public Integer next() { return curLight_--; }

    public void remove() {}

    public LightIterator make() {
        return new ReverseLightIterator(lightCount_, endLight_);
    }

    private int curLight_;
    private final int endLight_, lightCount_;

}
