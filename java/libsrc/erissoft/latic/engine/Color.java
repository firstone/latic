/*********************************************************************
 *
 *  File Name: Color.java
 *
 *  Description: LED Color object
 *
 *  Date Created: Sep 23, 2011
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

//base
import com.erissoft.base.Convert;

public class Color {

    public Color() { 
        this((byte) 0, (byte) 0, (byte) 0);
    }

    private Color(byte red, byte green, byte blue) {
        red_ = new Red(red);
        green_ = new Green(green);
        blue_ = new Blue(blue);
    }

    public Color(Color color) {
        this(color.red_, color.green_, color.blue_);
    }

    public Color(Red red, Green green, Blue blue) {
        red_ = red;
        green_ = green;
        blue_ = blue;
    }

    public Red getRed() { return red_; }
    public Green getGreen() { return green_; }
    public Blue getBlue() { return blue_; }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Color))
            return false;

        Color color = (Color) obj;

        return red_.val_ == color.red_.val_
            && green_.val_ == color.green_.val_
            && blue_.val_ == color.blue_.val_;
    }

    public String toString() {
        return "Color: " + Convert.toHexString(red_.val_)
            + ":" + Convert.toHexString(green_.val_)
            + ":" + Convert.toHexString(blue_.val_);
    }

    private final Red red_;
    private final Green green_;
    private final Blue blue_;

    private static class Channel {
        public Channel(byte val) {
            if (val < 0)
                val_ = 0;
            else if (val > MAX_CHANNEL_VAL_)
                val_ = MAX_CHANNEL_VAL_;
            else
                val_ = val;
        }

        boolean isMin() { return val_ == 0; }
        boolean isMax() { return val_ == MAX_CHANNEL_VAL_; }

        public byte getValue() { return val_; }

        protected final byte val_;

        private final static byte MAX_CHANNEL_VAL_ = 0xF;
    }

    public static class Red extends Channel {
        public Red(byte val) { super(val); }
    }

    public static class Green extends Channel {
        public Green(byte val) { super(val); }
    }

    public static class Blue extends Channel {
        public Blue(byte val) { super(val); }
    }

    public final static Color RED = new Color((byte) 0xF, (byte) 0, (byte) 0),
           GREEN = new Color((byte) 0, (byte) 0xF, (byte) 0),
           BLUE = new Color((byte) 0, (byte) 0, (byte) 0xF),
           YELLOW = new Color((byte) 0xF, (byte) 0xF, (byte) 0),
           CYAN = new Color((byte) 0, (byte) 0xF, (byte) 0xF),
           VIOLET = new Color((byte) 0xF, (byte) 0, (byte) 0xF),
           WHITE = new Color((byte) 0xF, (byte) 0xF, (byte) 0xF),
           BLACK = new Color((byte) 0, (byte) 0, (byte) 0);

}
