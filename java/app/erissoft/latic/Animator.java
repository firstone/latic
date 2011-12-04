/*********************************************************************
 *
 *  File Name: Animator.java
 *
 *  Description: Main animation program
 *
 *  Date Created: Jun 30, 2011
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

import java.util.Collection;
import java.util.LinkedList;

//base
import com.erissoft.base.ESException;

//latic.app
import erissoft.latic.app.*;

//latic.engine
import erissoft.latic.engine.*;

public class Animator {

    public static boolean animate() throws ESException {

        movingPicture_(50, 50);

        rainbow_();

        runningColor_();

        redGreenSplit_(50);

        static7Colors_();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return true;
    }

    private static void rainbow_() throws ESException {

        MultiAnimator multi = new MultiAnimator();
        multi.addAnimator(new RainbowAnimator());
        multi.addAnimator(new RainbowAnimator(true));
        multi.addAnimator(new erissoft.latic.engine.StaticColorAnimator(Color.BLACK));
        animateAllString_(multi);

    }

    private static void redGreenSplit_(int lightCount) throws ESException {
        Collection<Color> colors = new LinkedList<Color>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);

        for (int count = 1; count <= lightCount / 2; ++count) {
            animateAllSync_(new ColorCycleAnimator(new ColorCycle(colors), count));

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void staticColor_(Color color) throws ESException {
        animateAllString_(new StaticColorAnimator(color));
    }

    private static void static7Colors_() throws ESException {
        Collection<Color> colors = new LinkedList<Color>();

        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.BLUE);
        colors.add(Color.VIOLET);
        colors.add(Color.WHITE);

        animateAllSync_(new ColorCycleAnimator(new ColorCycle(colors), 1));
    }

    private static void runningColor_() throws ESException {

        Collection<Color> colors = new LinkedList<Color>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.BLUE);
        colors.add(Color.VIOLET);
        colors.add(Color.BLACK);

        ColorCycle cycle = new ColorCycle(colors);

        int fadeStep = 1;
        MultiAnimator anim = null;

        while (!cycle.isCycleDone()) {
            anim = new MultiAnimator();
            anim.addAnimator(new StaticColorAnimator(cycle.getNewColor()));
            anim.addAnimator(new SleepAnimator(50));

            //anim = new FadeToColorAnimator(fadeStep, cycle.getCurColor(), cycle.getNewColor());

            //anim = new FlipAnimator(fadeStep, cycle.getCurColor(), cycle.getNewColor(), Color.WHITE, 100);

            animateSeq_(anim);
            cycle.next();
        }
    }

    private static void animateAllString_(LightAnimator lm) throws ESException {
        Collection<TrackAnimator> list = new LinkedList<TrackAnimator>();

        for (LightString ls : LightStringManager.INSTANCE.getAll())
            list.add(new StringAnimator(ls, lm.make()));

        AnimationExecutor.INSTANCE.animate(list);

    }

    private static void animateAllSync_(LightAnimator lm) throws ESException {
        Collection<TrackAnimator> list = new LinkedList<TrackAnimator>();

        for (LightString ls : LightStringManager.INSTANCE.getAll())
            list.add(new SyncAnimator(ls, lm.make()));

        AnimationExecutor.INSTANCE.animate(list);

    }

    private static void animateSeq_(LightAnimator lm) throws ESException {

        Collection<TrackAnimator> list = new LinkedList<TrackAnimator>();

        LightString lowRoofString  = LightStringManager.INSTANCE.get("low_roof");
        TrackAnimator lowRoof = new StepAnimator(lowRoofString, lm);
        list.add(lowRoof);

        TrackAnimator highRoof = new StepAnimator(LightStringManager.INSTANCE.get("high_roof"),
                lm.make(), new AnimatorCondition(lowRoof, 19));
        list.add(highRoof);

        LightString tree1String  = LightStringManager.INSTANCE.get("tree1");
        TrackAnimator tree1 = new StepAnimator(tree1String, lm.make(), new AnimatorCondition(lowRoof, 11));
        list.add(tree1);

        list.add(new StepAnimator(LightStringManager.INSTANCE.get("tree2"),
                    lm.make(), new AnimatorCondition(lowRoof, 28)));

        list.add(new StepAnimator(LightStringManager.INSTANCE.get("tree3"),
                    lm.make(), new AnimatorCondition(lowRoof, lowRoofString.getCount() - 1)));

        LightString groundString = LightStringManager.INSTANCE.get("ground");
        TrackAnimator ground = new StepAnimator(groundString, lm.make(),
                new MidLightIterator(groundString.getCount(), 0, 20),
                new AnimatorCondition(tree1, tree1String.getCount() - 1));
        list.add(ground);

        list.add(new StepAnimator(LightStringManager.INSTANCE.get("bush"),
                    lm.make(), new AnimatorCondition(ground, 8)));

        list.add(new StepAnimator(LightStringManager.INSTANCE.get("round_tree"),
                    lm.make(), new AnimatorCondition(ground, 35)));

        list.add(new StepAnimator(LightStringManager.INSTANCE.get("door"),
                    lm.make(), new AnimatorCondition(ground, groundString.getCount() - 1)));

        AnimationExecutor.INSTANCE.animate(list);

    }

    private static void movingPicture_(int count, int delay) throws ESException {

        Collection<TrackAnimator> list = new LinkedList<TrackAnimator>();

        Collection<Color> roofColors = new LinkedList<Color>();
        roofColors.add(Color.BLUE);
        roofColors.add(Color.WHITE);

        Collection<Color> edgeColors = new LinkedList<Color>();
        edgeColors.add(Color.VIOLET);
        edgeColors.add(Color.YELLOW);

        Collection<Color> treeColors = new LinkedList<Color>();
        treeColors.add(Color.GREEN);
        treeColors.add(Color.BLACK);

        Collection<Color> bushColors = new LinkedList<Color>();
        bushColors.add(Color.VIOLET);
        bushColors.add(Color.BLACK);

        Collection<Color> roundTreeColors = new LinkedList<Color>();
        roundTreeColors.add(Color.RED);
        roundTreeColors.add(Color.BLACK);

        int lenArray[] = { 2, 2 };

        for (int i = 0; i < count; i++) {
            for (int start = 0; start < 4; start++) {
                list.clear();

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("high_roof"),
                            new ColorCycleAnimator(new ColorCycle(roofColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("low_roof"),
                            new ColorCycleAnimator(new ColorCycle(roofColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("tree1"),
                            new ColorCycleAnimator(new ColorCycle(treeColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("tree2"),
                            new ColorCycleAnimator(new ColorCycle(treeColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("tree3"),
                            new ColorCycleAnimator(new ColorCycle(treeColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("round_tree"),
                            new ColorCycleAnimator(new ColorCycle(roundTreeColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("bush"),
                            new ColorCycleAnimator(new ColorCycle(bushColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("ground"),
                            new ColorCycleAnimator(new ColorCycle(edgeColors), lenArray, start)));

                list.add(new SyncAnimator(LightStringManager.INSTANCE.get("door"),
                            new ColorCycleAnimator(new ColorCycle(edgeColors), lenArray, start)));

                AnimationExecutor.INSTANCE.animate(list);

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }

    }

}
