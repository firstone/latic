/*********************************************************************
 *
 *  File Name: TrackAnimator.java
 *
 *  Description: Animator for a string of lights
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

public abstract class TrackAnimator extends LightAnimator {

    public TrackAnimator(LightString ls) { 
        this(ls, null, null);
    }

    public TrackAnimator(LightString ls, Condition cond, LightIterator it) { 
        ls_ = ls;
        cond_ = cond;
        curLight_ = 0;
        lastLight_ = new Light(Light.SKIP_LIGHT);
        it_ = it == null ? new ForwardLightIterator(ls.getCount()) : it;
    }

    public void setIterator(LightIterator it) {
        if (it != null)
            it_ = it;
    }

    public LightString getLightString() { return ls_; }

    public Light getLight() { 
        if (cond_ != null && !cond_.isReady())
            return lastLight_.setSkip();

        Light light = super.getLight().setTrack(ls_.getPin()); 
        if (light.shouldSkip())
            light = lastLight_.setSkip();
        else
            lastLight_ = light;
        
        return light;
    }

    public int getCurLightNum() { return curLight_; }

    public abstract TrackAnimator make();

    public void setCondition(Condition cond) { cond_ = cond; }

    protected int curLight_;
    protected Condition cond_;
    protected final LightString ls_;
    protected LightIterator it_;


    private Light lastLight_;

}
