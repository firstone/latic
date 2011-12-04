/*********************************************************************
 *
 *  File Name: LightAnimator.java
 *
 *  Description: Animator for an individual class
 *
 *  Date Created: Sep 26, 2011
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

public abstract class LightAnimator implements AnimatorInterface {

    public LightAnimator() { 
        shouldSleep_ = false;
    }

    public Light getLight() {
        if (shouldSleep_) {
            if (System.currentTimeMillis() < wakeTime_)
                return Light.SKIP_LIGHT;

            shouldSleep_ = false;
        }

        return getLight_();
    }

    public boolean isDone() { return false; }

    protected abstract Light getLight_();

    protected void sleep_(long ms) {
        shouldSleep_ = true;
        wakeTime_ = System.currentTimeMillis() + ms;
    }

    protected boolean shouldSleep_;
    protected long wakeTime_;

}
