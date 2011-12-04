/*********************************************************************
 *
 *  File Name: Rainbow.java
 *
 *  Description: Rainbow color iterator 
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

import java.util.Iterator;

public class Rainbow implements Iterator<Color> {

    public Rainbow() { 
        this(1);
    }

    public Rainbow(int step) { 
        this(step, new Color(Color.RED), RANGE_.BEGIN_);
    }

    protected Rainbow(int step, Color color, RANGE_ range) { 
        curColor_ =  color;
        range_ = range;
        step_ = step;
    }

    public boolean hasNext() { return range_ != RANGE_.VIOLET_; }

    public Color next() {
        switch (range_) {

            case BEGIN_:
                range_ = RANGE_.RED_;
                break;

            case RED_:
                curColor_ = new Color(curColor_.getRed(), 
                        new Color.Green((byte) (curColor_.getGreen().getValue() + step_)),
                        curColor_.getBlue());
                if (curColor_.getGreen().isMax())
                    range_ = RANGE_.YELLOW_;

                break;

            case YELLOW_:
                curColor_ = new Color(new Color.Red((byte) (curColor_.getRed().getValue() - step_)),
                        curColor_.getGreen(),
                        curColor_.getBlue());
                if (curColor_.getRed().isMin())
                    range_ = RANGE_.GREEN_;

                break;

            case GREEN_:
                curColor_ = new Color(curColor_.getRed(), 
                        curColor_.getGreen(),
                        new Color.Blue((byte) (curColor_.getBlue().getValue() + step_)));
                if (curColor_.getBlue().isMax())
                    range_ = RANGE_.CYAN_;

                break;

            case CYAN_:
                curColor_ = new Color(curColor_.getRed(), 
                        new Color.Green((byte) (curColor_.getGreen().getValue() - step_)),
                        curColor_.getBlue());
                if (curColor_.getGreen().isMin())
                    range_ = RANGE_.BLUE_;

                break;

            case BLUE_:
                curColor_ = new Color(new Color.Red((byte) (curColor_.getRed().getValue() + step_)),
                        curColor_.getGreen(),
                        curColor_.getBlue());
                if (curColor_.getRed().isMax())
                    range_ = RANGE_.VIOLET_;
        }

        return curColor_;
    }

    public void remove() {}

    protected enum RANGE_ { BEGIN_, RED_, YELLOW_, GREEN_, CYAN_, BLUE_, VIOLET_, END_ };

    protected RANGE_ range_;
    protected int step_;
    protected Color curColor_;

}
