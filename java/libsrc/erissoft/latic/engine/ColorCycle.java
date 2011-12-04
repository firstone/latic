/*********************************************************************
 *
 *  File Name: ColorCycle.java
 *
 *  Description: Cycles through array of colors
 *
 *  Date Created: Sep 30, 2011
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

import java.util.Collection;

public class ColorCycle {

    public ColorCycle(Collection<Color> colors) { 
        colors_ = new Color[colors.size()];

        colors.toArray(colors_);
        curColor_ = 0;
        cycleDone_ = false;
    }

    public ColorCycle(ColorCycle cycle) { 
        colors_ = cycle.colors_;
        curColor_ = cycle.curColor_;
        cycleDone_ = cycle.cycleDone_;
    }

    public Color getNewColor() { return colors_[curColor_]; }
    public Color getCurColor() { 
        return  curColor_ == 0
            ? colors_[colors_.length - 1]
            : colors_[curColor_ - 1];
    }

    public boolean isCycleDone() { return cycleDone_; }

    public ColorCycle next() { 
        if (++curColor_ == colors_.length) {
            curColor_ = 0; 
            cycleDone_ = true; 
        } else 
            cycleDone_ = false;

        return this;
    }

    public ColorCycle make() {
        ColorCycle cycle = new ColorCycle(this);

        cycle.cycleDone_ = false;
        cycle.curColor_ = 0;

        return cycle;
    }

    private final Color colors_[];
    private int curColor_;
    private boolean cycleDone_;

}
