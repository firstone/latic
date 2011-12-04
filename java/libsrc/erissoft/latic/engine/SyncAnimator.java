/*********************************************************************
 *
 *  File Name: SyncAnimator.java
 *
 *  Description: Synchroniously moves through light string and animator
 *               at the same time
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

public class SyncAnimator extends TrackAnimator {

    public SyncAnimator(LightString ls, AnimatorInterface animator) { 
        this(ls, animator, null, null);
    }

    public SyncAnimator(LightString ls, AnimatorInterface animator, Condition cond) { 
        this(ls, animator, null, cond);
    }

    public SyncAnimator(LightString ls, AnimatorInterface animator, LightIterator it) { 
        this(ls, animator, it, null);
    }

    public SyncAnimator(LightString ls, AnimatorInterface animator, LightIterator it, Condition cond) { 
        super(ls, cond, it);

        animator_ = animator;
    }

    public boolean isDone() { 
        return !it_.hasNext() || animator_.isDone(); 
    }

    protected Light getLight_() {
        curLight_ = it_.next();
        return animator_.getLight().setAddr(curLight_);
    }

    public TrackAnimator make() {
        return new SyncAnimator(ls_, animator_.make(), it_.make(), 
                cond_ == null ? null : cond_.make());
    }

    protected AnimatorInterface animator_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.app");

}
