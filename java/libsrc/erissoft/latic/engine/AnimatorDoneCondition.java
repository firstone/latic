/*********************************************************************
 *
 *  File Name: AnimatorDoneCondition.java
 *
 *  Description: Starts when another animator is done
 *
 *  Date Created: Nov 29, 2011
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

public class AnimatorDoneCondition implements Condition {

    public AnimatorDoneCondition(TrackAnimator animator) {
        animator_ = animator;
        ready_ = false;
    }

    public boolean isReady() { 
        if (ready_)
            return true;

        return (ready_ = animator_.isDone());
    }

    public Condition make() { return new AnimatorDoneCondition(animator_); }

    private boolean ready_;
    private final TrackAnimator animator_;

}
