/*********************************************************************
 *
 *  File Name: SleepAnimator.java
 *
 *  Description: Stops execution for a number of milliseconds
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

public class SleepAnimator extends LightAnimator {

    public SleepAnimator(int ms) { 
        ms_ = ms;

        startSleep_ = false;
    }


    public boolean isDone() { return !startSleep_ || System.currentTimeMillis() >= wakeTime_; }

    protected Light getLight_() {
        if (!startSleep_) {
            startSleep_ = true;
            wakeTime_ = System.currentTimeMillis() + ms_;
        }

        return Light.SKIP_LIGHT;
    }

    public AnimatorInterface make() { return new SleepAnimator(ms_); }

    private boolean startSleep_;
    private long wakeTime_;
    private int ms_;

}
