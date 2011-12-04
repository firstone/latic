/*********************************************************************
 *
 *  File Name: MidLightIterator.java
 *
 *  Description: Middle to ends light string iterator 
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

public class MidLightIterator implements LightIterator {

    public MidLightIterator(int lightCount) { 
        this(lightCount, 0);
    }

    public MidLightIterator(int lightCount, int firstLight) { 
        this(lightCount, firstLight, (lightCount - firstLight) / 2);
    }

    public MidLightIterator(int lightCount, int firstLight, int startLight) { 
        leftEnd_ = firstLight;
        rightEnd_ = lightCount;
        startLight_ = startLight;

        rCurLight_ = startLight_;
        lCurLight_ = startLight_ - 1;
        curLight_ = CUR_LIGHT_.RIGHT;
    }

    public boolean hasNext() { return !rightDone_() || !leftDone_(); }

    public Integer next() {
        int val = 0;
        if (!rightDone_() && (leftDone_() || curLight_ == CUR_LIGHT_.RIGHT)) {
            curLight_ = CUR_LIGHT_.LEFT;
            val = rCurLight_++; 
        } else {
            val = lCurLight_--;
            curLight_ = CUR_LIGHT_.RIGHT;
        }

        return val;
    }

    private boolean rightDone_() { return rCurLight_ >= rightEnd_; }
    private boolean leftDone_() { return lCurLight_ < leftEnd_; }

    public void remove() {}

    public LightIterator make() {
        return new MidLightIterator(rightEnd_, leftEnd_, startLight_);
    }

    private enum CUR_LIGHT_ { LEFT, RIGHT };

    private int rCurLight_, lCurLight_;
    private CUR_LIGHT_ curLight_;
    private final int leftEnd_, rightEnd_, startLight_;

}
