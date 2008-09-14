package com.blueskyminds.framework.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collection;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;

/**
 * Tools for viewing debug information
 *
 * Date Started: 2/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class DebugTools {

    private static final Log LOG = LogFactory.getLog(DebugTools.class);

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the DebugTools with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public static void printAvailableHeap() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage memUsage = mem.getHeapMemoryUsage();
        long committed = memUsage.getCommitted()/1000;
        long used = memUsage.getUsed()/1000;
        long max= memUsage.getMax()/1000;
        double percent = ((Long) memUsage.getUsed()).doubleValue()/memUsage.getMax()*100.0;
        Formatter formatter = new Formatter();

        LOG.info(formatter.format("Used heap: "+used+"k/"+committed+"k/"+max+"k (%3.2f%% of available)", percent));
    }

    public static void printArray(Object[] array) {
        printArray(array, true);
    }

    /**
     *
     * @param array
     * @param horizontal true if displayed across the screen, false for vertical
     */
    public static void printArray(Object[] array, boolean horizontal) {
        System.out.println(toString(array, horizontal));
    }

    /**
     *
     * @param array
     * @param horizontal true if displayed across the screen, false for vertical
     */
    public static String toString(Object[] array, boolean horizontal) {
        boolean first = true;

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        if (array != null) {
            out.print("[");
            for (Object obj : array) {
                if (!first) {
                    out.print(",");
                } else {
                    first = false;
                }
                out.print(obj);

                if (!horizontal) {
                    out.println();
                }
            }
            out.print("]");
        } else {
            out.print("[null]");
        }

        return stringWriter.toString();
    }

    public static void printCollection(Collection collection) {
        printCollection(collection, true);
    }

    public static void printCollection(Collection collection, boolean indent) {
        System.out.println(toString(collection, indent));
    }

    public static String toString(Collection collection, boolean indent) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        if (collection != null) {
            out.println("---[Collection size="+ collection.size()+"]---");
            for (Object obj : collection) {
                if (indent) {
                    out.print("   ");
                }
                if (obj.getClass().isArray()) {
                    out.print(toString((Object[]) obj, true));
                } else {
                    out.print(obj);
                }
                out.println();
            }
        } else {
            out.println("---[null collection]---");
        }
        return stringWriter.toString();
    }

    public static void printMap(Map map) {
        System.out.println(toString(map));
    }

    public static String toString(Map map) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        if (map != null) {
            Iterator iterator = map.keySet().iterator();
            out.println("---[Map size="+map.size()+"]---");
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Object value = map.get(key);
                out.println("  " + key + "="+value);
            }
        } else {
            out.println("---[null map]---");
        }

        return stringWriter.toString();
    }

}
