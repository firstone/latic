/*********************************************************************
 *
 *  File Name: RainbowAnimator.java
 *
 *  Description: Rainbow color animator
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

public class RainbowAnimator extends LightAnimator {

    public RainbowAnimator() { 
        this(0, 1, false);
    }

    public RainbowAnimator(int start) { 
        this(start, 1, false);
    }

    public RainbowAnimator(int start, int step) { 
        this(start, step, false);
    }

    public RainbowAnimator(boolean reverse) { 
        this(0, 1, reverse);
    }

    public RainbowAnimator(int start, int step, boolean reverse) { 
        step_ = step;
        reverse_ = reverse;
        start_ = start;

        rain_ = reverse_ ? new ReverseRainbow(step_) : new Rainbow(step_);
        for (int i = 0; i < start; i++)
            rain_.next();
    }

    public boolean isDone() { return !rain_.hasNext(); }

    protected Light getLight_() {
        return new Light().setBrightness(Light.MAX_BRIGHTNESS).setColor(rain_.next());
    }

    public AnimatorInterface make() {
        return new RainbowAnimator(start_, step_, reverse_);
    }

    private Rainbow rain_;
    private final int step_, start_;
    private final boolean reverse_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
