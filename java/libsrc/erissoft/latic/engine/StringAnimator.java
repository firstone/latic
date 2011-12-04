/*********************************************************************
 *
 *  File Name: StringAnimator.java
 *
 *  Description: Applies animation steps to all bulbs together
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

public class StringAnimator extends TrackAnimator {

    public StringAnimator(LightString ls, AnimatorInterface animator) { 
        this(ls, animator, 0, null, null);
    }

    public StringAnimator(LightString ls, AnimatorInterface animator, Condition cond) { 
        this(ls, animator, 0, null, cond);
    }

    public StringAnimator(LightString ls, AnimatorInterface animator, int delay) { 
        this(ls, animator, delay, null, null);
    }

    public StringAnimator(LightString ls, AnimatorInterface animator, int delay, 
            LightIterator it, Condition cond) { 
        super(ls, cond, it);

        animator_ = animator;
        delay_ = delay;
        light_ = animator_.getLight();
    }

    public boolean isDone() { 
        return animator_.isDone() && !it_.hasNext();
    }

    protected Light getLight_() {
        if (!it_.hasNext()) {
            light_ = animator_.getLight();
            curLight_ = 0;
            it_ = it_.make();

            if (delay_ > 0)
                sleep_(delay_);
        }

        curLight_ = it_.next();
        light_.setAddr(curLight_);

        return light_;
    }

    public TrackAnimator make() {
        return new StringAnimator(ls_, animator_.make(), delay_, it_,
                cond_ == null ? null : cond_.make());
    }

    private AnimatorInterface animator_;
    private Light light_;
    private final int delay_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
