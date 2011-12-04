/*********************************************************************
 *
 *  File Name: latic.java
 *
 *  Description: Sends controlling messages to
 *               GE Color Effects light strings
 *
 *  Date Created: Jun 27, 2011
 * 
 *  Revision History:
 *  
 *  NNN - MMM YY - Name - Change
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
package erissoft.latic;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

//log4j
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//SunriseSunset
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

//base
import com.erissoft.base.Config;
import com.erissoft.base.Convert;
import com.erissoft.base.ESException;

//latic.engine
import erissoft.latic.engine.AnimationExecutor;
import erissoft.latic.engine.LightStringManager;
import erissoft.latic.engine.Writer;

//xml
import com.erissoft.xml.XMLConfigReader;

public class latic {

    public final static int LIGHT_COUNT = 50;
    public final static int MAX_BRIGHTNESS = 0xCC;
    public final static int TRACK = 5;

    public static void main(String args[]) {
        if (args.length < 1 && args.length > 2)
            printUsage_();

        new latic(args);
    }

    public latic(String args[]) {
        try {
            if (args[0].equals("-h") || (args.length > 1 && args[1].equals("-h")))
                printUsage_();

            if (args.length > 1 && (args[1].equals("-lc") || args[1].equals("-lw")))
                printLicense_(args[1].equals("-lc"), args[1].equals("-lw"));

            XMLConfigReader reader = new XMLConfigReader(args[0], false);
            Config.setReader(reader);

            logger_.info("LATIC version 1.0, Copyright (C) 2011, Erissoft");
            logger_.info("LATIC comes with ABSOLUTELY NO WARRANTY; for details");
            logger_.info("type `plum -lw'.  This is free software, and you are welcome");
            logger_.info("to redistribute it under certain conditions; type `plum -lc'");
            logger_.info("for details");

            if (args.length > 1 && args[1].equals("-pins")) {
                Writer.printPinConfig();
                System.exit(0);
            }

            logger_.info("Starting LATIC");

            logger_.debug("Config file: " + args[0]);

            LightStringManager.INSTANCE.init();

            Runtime.getRuntime().addShutdownHook(new ShutdownHook());

            if (Config.INSTANCE.getBoolVal("app.delayed_start.enable"))
                delayStart_();

            logger_.info("Starting animation");
            
            while (Animator.animate()) 
                ;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void delayStart_() throws ESException {
        int offset = Config.INSTANCE.getIntValNoEx("app.delayed_start.start.offset");
        String strTime = Config.INSTANCE.getStrVal("app.delayed_start.start");
        Calendar time = Calendar.getInstance();

        if (strTime.equalsIgnoreCase("SUNSET"))
            time = (new SunriseSunsetCalculator(new Location(Config.INSTANCE.getStrVal("app.delayed_start.location.latitude"), Config.INSTANCE.getStrVal("app.delayed_start.location.longitude")), Config.INSTANCE.getStrVal("app.delayed_start.location.time_zone"))).getOfficialSunsetCalendarForDate(time);
        else
            try {
                Calendar start = Calendar.getInstance();
                start.setTime((new SimpleDateFormat("HH:mm:ss")).parse(strTime));

                time.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
                time.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
                time.set(Calendar.SECOND, start.get(Calendar.SECOND));
                time.set(Calendar.MILLISECOND, 0);

            } catch (ParseException e) {
                throw new ESException("Invalid start delay value: " + strTime);
            }

        
        time.add(Calendar.SECOND, offset);

        logger_.debug("Delaying animation until " + time.getTime());

        long timeMS = time.getTime().getTime();

        while (System.currentTimeMillis() < timeMS)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

    }

    private static void printUsage_() {
        System.err.println("Usage: latic<config file> [ -pins | -lc | -lw | -h ]");
        System.exit(-1);
    }

    private void printLicense_(boolean printCR, boolean printWAR) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("license.txt"));
            String line;
            boolean warRunning = false, crRunning = false, allDone = false;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION") >= 0)
                    crRunning = true;
                else if (line.indexOf("NO WARRANTY") >= 0) {
                    crRunning = false;
                    warRunning = true;
                } else if (line.indexOf("END OF TERMS AND CONDITIONS") >= 0)
                    allDone = true;

                if (!allDone
                        && ((crRunning && printCR)
                            || (warRunning && printWAR)))
                    System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading license file");
            System.exit(-1);
        }

        System.exit(0);
    }

    private static class ShutdownHook extends Thread {
        public void run() {
            try {
                logger_.info("Stopping LATIC");
                AnimationExecutor.INSTANCE.idle();
            } catch (ESException e) {}
        }
    }

    private static org.apache.log4j.Logger logger_
        = org.apache.log4j.Logger.getLogger("erissoft.app.letic");

}
