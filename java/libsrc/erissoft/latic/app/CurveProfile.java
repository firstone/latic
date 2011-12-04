/*********************************************************************
 *
 *  File Name: CurveProfile.java
 *
 *  Description: Config file based profile 
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

//base
import com.erissoft.base.Config;
import com.erissoft.base.ESException;

public class CurveProfile implements Profile {

    public CurveProfile(String profileName) {
        profileName_ = profileName;
        data_ = new LinkedList<Double>();
    }

    public CurveProfile(CurveProfile profile) {
        profileName_ = profile.profileName_;
        data_ = new LinkedList<Double>(profile.data_);

        it_ = data_.iterator();
    }

    public boolean hasNext() { return it_ != null && it_.hasNext(); }

    public Double next() {
        if (!it_.hasNext())
            return 0.0;

        return it_.next();
    }

    public void remove() {}

    public void init() throws ESException {
        if (!data_.isEmpty())
            return;

        Collection<String> list = Config.INSTANCE.selectMultVal("app.profiles",
                "profile.name", profileName_, "time_slice");

        Collection<Double> res = new LinkedList<Double>();

        double sum = 0;
        for (String val : list)
            try {
                double d = Double.valueOf(val);
                sum += d;
                res.add(d);
            } catch (NumberFormatException e) {
                throw new ESException("Invalid time slice: " + val);
            }

        for (Double d : res) 
            data_.add(d / sum);

        it_ = data_.iterator();
    }

    public Profile make() {
        return new CurveProfile(this);
    }

    private Collection<Double> data_;
    private Iterator<Double> it_;
    private String profileName_;

    protected static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.app.app");

}
