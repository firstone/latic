/*********************************************************************
 *
 *  File Name: ProfileAnimator.java
 *
 *  Description: Sync animator with timed based profile
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

public class ProfileAnimator extends SyncAnimator {

    public ProfileAnimator(LightString ls, AnimatorInterface animator, 
            Profile profile, long time) { 
        this(ls, animator, profile, time, null, null);
    }

    public ProfileAnimator(LightString ls, AnimatorInterface animator, 
            Profile profile, long time, Condition cond) { 
        this(ls, animator, profile, time, null, cond);
    }

    public ProfileAnimator(LightString ls, AnimatorInterface animator, 
            Profile profile, long time, LightIterator it) { 
        this(ls, animator, profile, time, it, null); 
    }

    public ProfileAnimator(LightString ls, AnimatorInterface animator, 
            Profile profile, long time, LightIterator it, Condition cond) { 
        super(ls, animator, it, cond);

        profile_ = profile;
        time_ = time;
    }

    protected Light getLight_() {
        sleep_(Math.round((double) time_ * profile_.next()));
        return super.getLight_();
    }

    public TrackAnimator make() {
        return new ProfileAnimator(ls_, animator_.make(), profile_.make(), time_,
            it_ == null ? null : it_.make(), cond_ == null ? null : cond_.make());
    }

    private Profile profile_;
    private long time_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.app");

}
