package com.blueskyminds.framework;

/**
 * Date Started: 8/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class DomainObjectExample extends AbstractDomainObject {
    private String a;
    private String b;
    private String c;
    private String d;


    public DomainObjectExample(String a, String b, String c, String d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    /**
     * Merge the simple properties of another DomainObject into this DomainObject.
     *
     * @param other the object to extract properties from into this object
     * @throws com.blueskyminds.framework.MergeUnsupportedException
     *          when this domain object hasn't implemented the operation
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        super.mergeSimpleProperties(other);
    }
}
