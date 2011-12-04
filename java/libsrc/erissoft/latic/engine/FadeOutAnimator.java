/*********************************************************************
 *
 *  File Name: FadeOutAnimator.java
 *
 *  Description: Animates fade from some value to 0
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

public class FadeOutAnimator extends LightAnimator {

    public FadeOutAnimator(int step, Color color) { 
        step_ = step;
        color_ = color;

        brightness_ = Light.MAX_BRIGHTNESS;
    }

    public boolean isDone() { return  brightness_ <= 0; }

    protected Light getLight_() {
        brightness_ -= step_;

        if (brightness_ < 0)
            brightness_ = 0;

        return new Light().setBrightness(brightness_).setColor(color_);
    }

    public AnimatorInterface make() { 
        return new FadeOutAnimator(step_, color_);
    }

    private final int step_;
    private int brightness_;
    private final Color color_;

}
