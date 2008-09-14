package com.blueskyminds.framework.tools;

import org.apache.commons.lang.time.StopWatch;

import java.util.Map;
import java.util.HashMap;

/**
 * Methods for profiling the performance of an application
 *
 * Date Started: 15/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class ProfilingTools {

    private static Map<String, StopWatch> stopWatches = new HashMap<String, StopWatch>();

    public static void startTimer(String stopwatchName) {
        if (stopWatches.containsKey(stopwatchName)) {
            stopWatches.get(stopwatchName).start();
        } else {
            StopWatch stopWatch = new StopWatch();
            stopWatches.put(stopwatchName, stopWatch);
            stopWatch.start();
        }
    }

    public static void stopTimer(String stopwatchName) {
        stopWatches.get(stopwatchName).stop();
    }

    public static void splitTimer(String stopwatchName) {
        stopWatches.get(stopwatchName).split();
    }

    public static void resetTimer(String stopwatchName) {
        stopWatches.get(stopwatchName).reset();
    }

    public static long getTime(String stopwatchName) {
        return stopWatches.get(stopwatchName).getTime();
    }
       

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the ProfilingTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
