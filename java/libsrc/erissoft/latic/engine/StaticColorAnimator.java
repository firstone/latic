/*********************************************************************
 *
 *  File Name: StaticColorAnimator.java
 *
 *  Description: Returns light of a static color
 *
 *  Date Created: Sep 27, 2011
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

public class StaticColorAnimator extends LightAnimator {

    public StaticColorAnimator() {
        color_ = null;
        done_ = false;
        curLight_ = 0;
        lightCount_ = 1;
    }

    public StaticColorAnimator(Color color) { 
        this(color, 1);
    }

    public StaticColorAnimator(Color color, LightString ls) { 
        this(color, ls.getCount());
    }

    public StaticColorAnimator(Color color, int lightCount) { 
        color_ = color;
        done_ = false;
        lightCount_ = lightCount;
        curLight_ = 0;
    }

    public boolean isDone() { return done_; }

    protected Light getLight_() {
        done_ = ++curLight_ >= lightCount_;
        return new Light().setBrightness(Light.MAX_BRIGHTNESS).setColor(color_);
    }

    public AnimatorInterface make() {
        return new StaticColorAnimator(color_, lightCount_);
    }

    private final Color color_;
    private boolean done_;
    private final int lightCount_;
    private int curLight_;

}
