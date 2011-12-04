/*********************************************************************
 *
 *  File Name: MultiAnimator.java
 *
 *  Description: Animator container object
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

import java.util.List;
import java.util.LinkedList;

public class MultiAnimator extends LightAnimator {

    public MultiAnimator() { 
        list_ = new LinkedList<AnimatorInterface>();
        curAnimator_ = 0;
    }

    public void addAnimator(AnimatorInterface animator) {
        list_.add(animator);
    }

    public boolean isDone() { return curAnimator_ == list_.size(); }

    protected Light getLight_() {
        Light light = list_.get(curAnimator_).getLight();

        if (list_.get(curAnimator_).isDone()) curAnimator_++;

        return light;
    }

    public AnimatorInterface make() {
        MultiAnimator multi = new MultiAnimator();

        for (AnimatorInterface anim : list_)
            multi.addAnimator(anim.make());

        return multi;
    }

    private List<AnimatorInterface> list_;
    private int curAnimator_;

}
