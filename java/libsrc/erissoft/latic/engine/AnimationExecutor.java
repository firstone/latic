/*********************************************************************
 *
 *  File Name: AnimationExecutor.java
 *
 *  Description: Animation program executor
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

import java.util.Collection;
import java.util.LinkedList;

//base 
import com.erissoft.base.ESException;

public enum AnimationExecutor {

    INSTANCE;

    AnimationExecutor() {
        writer_ = Writer.PROTOCOL.equals("native") ? new NativeWriter() : new E131Writer();
        animators_ = new LinkedList<TrackAnimator>();
    }

    public void animate(TrackAnimator animator) throws ESException { 
        animators_.clear();
        animators_.add(animator);

        animate_();
    }

    public void animate(Collection<TrackAnimator> animators) throws ESException { 
        animators_.clear();
        animators_.addAll(animators);

        animate_();
    }

    private void animate_() throws ESException {
        writer_.init();

        sentCount_ = 0;

        while (!isDone()) {
            for (TrackAnimator animator : animators_)
                if (!animator.isDone()) {
                    Light light = animator.getLight();
                    /*
                    if (!light.shouldSkip())
                        logger_.debug(light);
                    */
                    writer_.addLight(light);//animator.getLight());
                }

            sentCount_ += writer_.write();
        }

        logger_.debug("" + sentCount_ + " light commands sent");
    }

    public boolean isDone() {
        for (TrackAnimator animator : animators_) 
            if (!animator.isDone())
                return false;

        return true;

    }

    public void idle() throws ESException {
        if (writer_ != null)
            writer_.idle();
    }

    private int sentCount_;
    private Writer writer_;
    private Collection<TrackAnimator> animators_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
