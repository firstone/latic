/*********************************************************************
 *
 *  File Name: ColorFad3eAnimator.java
 *
 *  Description: Fades between 2 colors
 *
 *  Date Created: Oct 10, 2011
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
package erissoft.latic.app;

//latic.engine
import erissoft.latic.engine.*;

public class ColorFadeAnimator extends LightAnimator {

    public ColorFadeAnimator(Color startColor, Color endColor, LightString ls) {
        this(startColor, endColor, ls.getCount());
    }

    public ColorFadeAnimator(Color startColor, Color endColor, int stepCount) {
        startColor_ = startColor;
        stepCount_ = stepCount;

        redStep_ = (double) (endColor.getRed().getValue() - startColor.getRed().getValue()) / stepCount;
        greenStep_ = (double) (endColor.getGreen().getValue() - startColor.getGreen().getValue()) / stepCount;
        blueStep_ = (double) (endColor.getBlue().getValue() - startColor.getBlue().getValue()) / stepCount;

        init_();
    }

    public ColorFadeAnimator(ColorFadeAnimator anim) {
        startColor_ = anim.startColor_;
        stepCount_ = anim.stepCount_;

        redStep_ = anim.redStep_;
        greenStep_ = anim.greenStep_;
        blueStep_ = anim.blueStep_;
    }

    private void init_() {
        curStep_ = 0;
        curRed_ = startColor_.getRed().getValue();
        curGreen_ = startColor_.getGreen().getValue();
        curBlue_ = startColor_.getBlue().getValue();
    }

    public boolean isDone() { return curStep_ >= stepCount_; }

    protected Light getLight_() {
        Light light = new Light().setColor(new Color(new Color.Red((byte) Math.round(curRed_)), 
                    new Color.Green((byte) Math.round(curGreen_)), 
                    new Color.Blue((byte) Math.round(curBlue_))))
            .setBrightness(Light.MAX_BRIGHTNESS);

        if (++curStep_ < stepCount_) {
            curRed_ += redStep_;
            curGreen_ += greenStep_;
            curBlue_ += blueStep_;
        }

        return light;
    }

    public AnimatorInterface make() {
        ColorFadeAnimator anim = new ColorFadeAnimator(this);
        anim.init_();

        return anim;
    }

    private int curStep_;
    private final int stepCount_;
    private final double redStep_, greenStep_, blueStep_;
    private double curRed_, curGreen_, curBlue_;
    private Color startColor_;

}
