/*********************************************************************
 *
 *  File Name: StepAnimator.java
 *
 *  Description: Applies animation to each bulb of the string
 *               one by one
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

public class StepAnimator extends TrackAnimator {

    public StepAnimator(LightString ls, AnimatorInterface animator) { 
        this(ls, animator, 0, null, null);
    }

    public StepAnimator(LightString ls, AnimatorInterface animator, Condition cond) { 
        this(ls, animator, 0, null, cond);
    }

    public StepAnimator(LightString ls, AnimatorInterface animator, LightIterator it) {
        this(ls, animator, 0, it, null);
    }

    public StepAnimator(LightString ls, AnimatorInterface animator, int delay) { 
        this(ls, animator, delay, null, null);
    }

    public StepAnimator(LightString ls, AnimatorInterface animator, int delay, LightIterator it, 
            Condition cond) { 
        super(ls, cond, it);

        animator_ = animator;
        delay_ = delay;
        curLight_ = it_.next();
    }

    public boolean isDone() { return !it_.hasNext() && animator_.isDone(); }

    protected Light getLight_() {
        Light light = animator_.getLight().setAddr(curLight_);

        if (animator_.isDone() && it_.hasNext()) {
            animator_ = animator_.make();
            curLight_ = it_.next();
        }

        if (delay_ > 0)
            sleep_(delay_);

        return light;
    }

    public TrackAnimator make() {
        return new StepAnimator(ls_, animator_.make(), delay_, it_.make(), 
                cond_ == null ? null : cond_.make()); 
    }

    private AnimatorInterface animator_;
    private final int delay_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
