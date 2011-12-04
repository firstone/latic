/*********************************************************************
 *
 *  File Name: EndsLightIterator.java
 *
 *  Description: ends to middle light string iterator 
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

public class EndsLightIterator implements LightIterator {

    public EndsLightIterator(int lightCount) { 
        this(lightCount, 0);
    }

    public EndsLightIterator(int lightCount, int firstLight) { 
        this(lightCount, firstLight, (lightCount - firstLight) / 2);
    }

    public EndsLightIterator(int lightCount, int firstLight, int endLight) { 
        endLight_ = endLight;
        lightCount_ = lightCount;
        firstLight_ = firstLight;

        rCurLight_ = lightCount_ - 1;
        lCurLight_ = firstLight_;
        curLight_ = CUR_LIGHT_.RIGHT;
    }

    public boolean hasNext() { return !rightDone_() || !leftDone_(); }

    public Integer next() {
        int val = 0;
        if (!rightDone_() && (leftDone_() || curLight_ == CUR_LIGHT_.RIGHT)) {
            curLight_ = CUR_LIGHT_.LEFT;
            val = rCurLight_--; 
        } else {
            val = lCurLight_++;
            curLight_ = CUR_LIGHT_.RIGHT;
        }

        return val;
    }

    private boolean rightDone_() { return rCurLight_ <= endLight_; }
    private boolean leftDone_() { return lCurLight_ > endLight_; }

    public void remove() {}

    public LightIterator make() {
        return new EndsLightIterator(lightCount_, firstLight_, endLight_);
    }

    private enum CUR_LIGHT_ { LEFT, RIGHT };

    private int rCurLight_, lCurLight_;
    private CUR_LIGHT_ curLight_;
    private final int endLight_, lightCount_, firstLight_;

}
