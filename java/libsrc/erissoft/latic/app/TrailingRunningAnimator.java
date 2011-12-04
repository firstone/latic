/*********************************************************************
 *
 *  File Name: TrailingRunningAnimator.java
 *
 *  Description: Running light with trail (Knight Rider / Cylon style)
 *
 *  Date Created: Nov 22, 2011
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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

//engine
import erissoft.latic.engine.*;

public class TrailingRunningAnimator extends TrackAnimator {

    public TrailingRunningAnimator(LightString ls, Color color, int trailSize) { 
        this(ls, color, trailSize, 0, new ForwardLightIterator(ls.getCount()));
    }

    public TrailingRunningAnimator(LightString ls, Color color, int trailSize, int delay) { 
        this(ls, color, trailSize, delay, new ForwardLightIterator(ls.getCount()));
    }

    public TrailingRunningAnimator(LightString ls, Color color, int trailSize, int delay, LightIterator it) { 
        super(ls, null, it);

        delay_ = delay;
        trailSize_ = trailSize + 1;
        color_ = color;
        trail_ = new LinkedList<Integer>();
        trailIT_ = null;
        dimStep_ = (double) Light.MAX_BRIGHTNESS / trailSize_;
    }

    public boolean isDone() { 
        return !it_.hasNext() && trail_.isEmpty();
    }

    protected Light getLight_() {
        if (trailIT_ == null) {
            if (it_.hasNext()) {
                int addr = it_.next();
                curBrightness_ = Light.MAX_BRIGHTNESS;
                light_ = new Light().setColor(color_).setBrightness((int) curBrightness_).setAddr(addr);
                trail_.add(0, addr);
                trailIT_ = trail_.listIterator(1);
            } else {
                curBrightness_ = (trail_.size() - 1) * dimStep_;
                light_ = new Light().setColor(color_).setBrightness((int) Math.round(curBrightness_))
                    .setAddr(trail_.remove(0));
                trailIT_ = trail_.listIterator();
            }
        } else { 
            curBrightness_ -= dimStep_;
            light_ = new Light().setColor(color_).setBrightness((int) Math.round(curBrightness_))
                .setAddr(trailIT_.next());

            if (!trailIT_.hasNext()) {
                if (trail_.size() > trailSize_)
                    trail_.remove(trailSize_);
                trailIT_ = null;
                if (delay_ > 0)
                    sleep_(delay_);
            }
        }

        return light_;
    }

    public TrackAnimator make() {
        return new TrailingRunningAnimator(ls_, color_, trailSize_ - 1, delay_, it_);
    }

    private double curBrightness_, dimStep_;
    private List<Integer> trail_;
    private ListIterator<Integer> trailIT_;
    private Light light_;

    private final int trailSize_;
    private final Color color_;
    private final int delay_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.engine");

}
