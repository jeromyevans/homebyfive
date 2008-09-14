package com.blueskyminds.framework.memento;

import com.thoughtworks.xstream.XStream;

/**
 * XMLMemento <-> XML
 *
 * Date Started: 9/09/2006
 * <p/>
 * History:
 */
public class XMLMementoFactory {

    /** This is a bit lazy - would be better to have an interface to the serializer and allow it to be
     *  overridden */
    private static final XStream xStream = new XStream();

    // ------------------------------------------------------------------------------------------------------

    /** Convert this XMLMemento to XML */
    public static String serialize(XMLMemento memento) {
        return xStream.toXML(memento);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new XMLMemento instance from a String */
    public static XMLMemento createMemento(String xml) {
        XMLMemento memento = null;

        if (xml != null) {
            memento = (XMLMemento) xStream.fromXML(xml);
        }
        return memento;
    }
}
