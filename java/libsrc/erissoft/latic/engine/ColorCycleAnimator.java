/*********************************************************************
 *
 *  File Name: ColorCycleAnimator.java
 *
 *  Description: Color array animator
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

public class ColorCycleAnimator extends LightAnimator {

    public ColorCycleAnimator(ColorCycle cycle, int lightCount) {
        this(cycle, new int[]{lightCount}, 0);
    }

    public ColorCycleAnimator(ColorCycle cycle, int lightCount[], int start) {
        cycle_ = cycle;
        lightCount_ = lightCount;
        curCount_ = 0;
        countIndex_ = 0;
        start_ = start;

        if (start_ > 0) {
            int count = 0;
            for (int val : lightCount) 
                count += val;

            for (int i = 0; i < count - start; i++)
                getLight_();
        }
    }

    protected Light getLight_() {
        Light light = new Light().setColor(cycle_.getNewColor()).setBrightness(Light.MAX_BRIGHTNESS);

        if (++curCount_ >= lightCount_[countIndex_]) {
            curCount_ = 0;
            cycle_.next();
            if (++countIndex_ >= lightCount_.length)
                countIndex_ = 0;
        }

        return light;
    }

    public AnimatorInterface make() { 
        return new ColorCycleAnimator(cycle_.make(), lightCount_, start_);
    }

    private final ColorCycle cycle_;
    private final int lightCount_[];
    private final int start_;
    private int curCount_, countIndex_;
}
