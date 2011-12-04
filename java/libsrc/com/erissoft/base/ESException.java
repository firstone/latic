/*********************************************************************
 *
 *  File Name: ESException.java
 *
 *  Description: Generic exception
 *
 *  Date Created: Mar 9, 2005
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
 *  
 *  Copyright (C) 2005, Erissoft
 *  
 *  This file is part of Erissoft common framework
 *
 *  HAM is free software; you can redistribute it and/or
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
package com.erissoft.base;

public class ESException extends Exception {

    public ESException(String msg, boolean quiet) {
        super(msg);

        errCode_ = -1;
        msg_ = msg;
        quiet_ = quiet;
        print_(msg);
    }

    public ESException(String msg) {
        this(msg, false);
    }

    public ESException(String msg, Exception e, boolean quiet) {
        this((msg == null ? "" : msg + ": ")
            + (e == null || e.getMessage() == null || e.getMessage().isEmpty() 
                ? e.toString() : e.getMessage()), quiet);
    }

    public ESException(String msg, Exception e) {
        this(msg, e, false);
    }

    public ESException(Exception e, boolean quiet) {
        this(null, e, quiet);
    }

    public ESException(Exception e) {
        this(null, e, false);
    }

    public ESException(ESException e) {
        this(e.msg_, false);
    }

    protected ESException() {
        errCode_ = -1;
        quiet_ = false;
    }

    protected void print_(String msg) {
        if (!quiet_) {
            msg_ = msg;
            logger_.error(msg);
        }
    }

    public String toString() { return msg_; }

    public int errCode() { return errCode_; }

    private String msg_;
    private boolean quiet_;
    private int errCode_;

    private org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.lib.com.base");
}
