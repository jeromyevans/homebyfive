package com.blueskyminds.homebyfive.framework.framework.tools;

import junit.framework.TestCase;

import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Unit tests for the ReflectionTools class
 *
 * Date Started: 9/10/2007
 * <p/>
 * History:
 */
public class TestReflectionTools extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestReflectionTools.class);

    public interface InterfaceA {
        String getA();
    }

    public interface InterfaceB extends ParentInterface {
        String getB();
    }

    public interface InterfaceC {
        String getC();
    }

    public interface InterfaceD {
       String getD();
    }

    public interface InterfaceE {
       String getE();
    }

    /**
     * Used to test uniqueness 
     */
    public interface Duplicate {
    }

    /** Use to test interface hierarchy */
    public interface ParentInterface {
    }

    public static class ClassA extends ClassB implements InterfaceA, Duplicate {

        private String a;
        private String z;

        public ClassA(String a, String b, String c, String d, String e, String x, String y, String z) {
            super(b, c, d, e, x, y);
            this.a = a;
            this.z = z;
        }

        public String getA() {
            return a;
        }

        public String getZ() {
            return z;
        }
    }

    public static class ClassB extends ClassC implements InterfaceB, InterfaceC {

        private String b;
        private String c;
        private String y;

        public ClassB(String b, String c, String d, String e, String x, String y) {
            super(d, e, x);
            this.b = b;
            this.c = c;
            this.y = y;
        }

        public String getC() {
            return c;
        }

        public String getB() {
            return b;
        }

        public String getY() {
            return y;
        }
    }

    public static class ClassC implements InterfaceD, InterfaceE, Duplicate {

        private String d;
        private String e;
        private String x;

        public ClassC(String d, String e, String x) {
            this.d = d;
            this.e = e;
            this.x = x;
        }

        public String getD() {
            return d;
        }

        public String getE() {
            return e;
        }

        public String getX() {
            return x;
        }
    }

    public void testListInterfaces() {
        ClassA objectA = new ClassA("a", "b", "c", "d", "e", "x", "y", "z");
        List<Class> interfaces = ReflectionTools.listInterfaces(objectA, false);
        DebugTools.printCollection(interfaces);
        assertEquals(11, interfaces.size());

        List<Class> uniqueInterfaces = ReflectionTools.listInterfaces(objectA, true);
        DebugTools.printCollection(uniqueInterfaces);
        assertEquals(10, interfaces.size()); // duplicate is listed only once
    }

    private int count;

    public void testVisitInterfaces() {
        ClassA objectA = new ClassA("a", "b", "c", "d", "e", "x", "y", "z");

        count = 0;
        assertTrue(ReflectionTools.visitInterfaces(objectA, false, new ReflectionTools.ClassVisitor() {
            /**
             * Called when a new interface/class is encountered
             *
             * @param       aClass the encountered class/interface
             * @return true if the recursion should continue, false to stop recursion immediately
             */
            public boolean visit(Class aClass) {
                LOG.info(aClass);
                count++;
                return true;
            }
        }));

        LOG.info("Count = "+count);
        assertEquals(11, count);

        count = 0;
        assertTrue(ReflectionTools.visitInterfaces(objectA, true, new ReflectionTools.ClassVisitor() {
            /**
             * Called when a new interface/class is encountered
             *
             * @param       aClass the encountered class/interface
             * @return true if the recursion should continue, false to stop recursion immediately
             */
            public boolean visit(Class aClass) {
                LOG.info(aClass);
                count++;               
                return true;
            }
        }));
        LOG.info("Count = "+count);
        assertEquals(10, count);

        // this one breaks out of the recursion early
        assertFalse(ReflectionTools.visitInterfaces(objectA, true, new ReflectionTools.ClassVisitor() {
            /**
             * Called when a new interface/class is encountered
             *
             * @param       aClass the encountered class/interface
             * @return true if the recursion should continue, false to stop recursion immediately
             */
            public boolean visit(Class aClass) {
                LOG.info(aClass);
                return !(InterfaceD.class.equals(aClass));
            }
        }));

    }
}
