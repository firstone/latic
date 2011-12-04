/*********************************************************************
 *
 *  File Name: Light.java
 *
 *  Description: LED inddividual light object
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
import com.erissoft.base.Config;
import com.erissoft.base.Convert;

public class Light {

    public Light() { 
        this(false);
    }

    private Light(boolean skip) {
        color_ = new Color();
        addr_ = 0;
        brightness_ = 0;
        track_ = 0;
        skip_ = skip;
    }

    public Light(Light light) {
        color_ = new Color(light.color_);
        addr_ = light.addr_;
        brightness_ = light.brightness_;
        track_ = light.track_;
        skip_ = light.skip_;
    }

    public Light setAddr(int addr) {
        addr_ = addr;

        return this;
    }

    public int getAddr() { return addr_; }

    public Light setBrightness(int brightness) { 
        brightness_ = brightness;
        return this;
    }

    public int getBrightness() { return brightness_; }

    public Light dim() {
        if (brightness_ > 0)
            brightness_--;

        return this;
    }

    public Light bright() {
        if (brightness_ < 255)
            brightness_++;

        return this;
    }

    public Light setColor(Color color) {
        color_ = color;

        return this;
    }

    public Color getColor() { return color_; }

    public Light setTrack(int track) {
        track_ = track;

        return this;
    }

    public int getTrack() { return track_; }

    public int getData() {
        int data = skip_ ? 1 : 0;
        
        data = (data << 6) | (addr_ & 0x3F);

        data = (data << 8) | (brightness_ & 0xFF);

        data = (data << 4) | (color_.getBlue().getValue() & 0xF);
        data = (data << 4) | (color_.getGreen().getValue() & 0xF);
        data = (data << 4) | (color_.getRed().getValue() & 0xF);

        return data;
    }

    public Light setSkip() { 
        skip_ = true; 
        
        return this;
    }

    public boolean shouldSkip() { return skip_; }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Light))
            return false;

        Light light = (Light) obj;

        return skip_ == light.skip_
            || (addr_ == light.addr_
                && brightness_ == light.brightness_
                && track_== light.track_
                && color_.equals(light.color_));
    }

    public String toString() {
        return "Light: Port: " + Writer.getPort(track_)
            + ", Track: " + track_
            + ", Addr: " + addr_ + ", " + color_ 
            + ", Brightness: " + Convert.toHexString((byte) brightness_)
            + (skip_ ? ", Skip" : "");
    }

    private Color color_;
    private int addr_, brightness_, track_;
    private boolean skip_;

    public static final Light SKIP_LIGHT = new Light(true);

    public static final byte MAX_BRIGHTNESS 
        = (byte) Convert.stringToLong(Config.INSTANCE.getStrValNoEx("app.lights.max_brightness", "0xCC"));

}
