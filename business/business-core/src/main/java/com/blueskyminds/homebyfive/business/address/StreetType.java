package com.blueskyminds.homebyfive.business.address;

import java.util.List;
import java.util.ArrayList;

/**
 * A type of Street.  eg. Road, Avenue.
 * <p/>
 * Date Created: 15 Feb 2007
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */

public enum StreetType {

    Access,
    Alley,
    Approach,
    Arcade,
    Avenue,
    Banan,
    Bend,
    Brace,
    Brae,
    Break,
    Brow,
    Boulevard,
    Bypass,
    Causeway,
    Circuit,
    Chase,
    Circle,
    Circus,
    Close,
    Corner,
    Concourse,
    Court,
    Cove,
    Copse,
    Crescent,
    Crossing,
    Cross,
    Crest,
    Centre,
    Courtyard,
    Dale,
    Dip,
    Drive,
    Edge,
    Elbow,
    End,
    Entrance,
    Esplanade,
    Fairway,
    Follow,
    Formation,
    Frontage,
    Freeway,
    Gap,
    Gardens,
    Glade,
    Gully,
    Glen,
    Green,
    Grove,
    Grange,
    Gate,
    Heights,
    Hill,
    Highway,
    Interchange,
    Junction,
    Key,
    Lane,
    Line,
    Link,
    Lookout,
    Loop,
    Mall,
    Meander,
    Mews,
    Nook,
    Outlook,
    Parade,
    Parkway,
    Pass,
    Path,
    Pocket,
    Place,
    Plaza,
    Point,
    Port,
    Promenade,
    Pathway,
    Quadrant,
    Quays,
    Ramble,
    Retreat,
    Rest,
    Ridge,
    Rise,
    Road,
    Rotary,
    Row,
    Shunt,
    Spur,
    Square,
    Street,
    Tarn,
    Terrace,
    Top,
    Tor,
    Track,
    The,
    Trail,
    Turn,
    Underpass,
    Vale,
    View,
    Vista,
    Walk,
    Walkway,
    Wharf,
    Way,
    Wynd;

    /**
     * Get the entire list of street types as an array of strings
     *
     * @return
     */
    public static String[] asList() {
        StreetType[] streetTypes = values();
        List<String> list = new ArrayList<String>(streetTypes.length);

        for (StreetType type : streetTypes) {
            list.add(type.toString());
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * Similar to the valueOf method but accepts null values, invalid values and ignores case
     *
     * @param str
     * @return the StreetType if matched, otherwise null
     */
    public static StreetType valueOfIgnoreCase(String str) {
        if (str != null) {
            for (StreetType value : values()) {
                if (str.equalsIgnoreCase(value.name())) {
                    return value;
                }
            }
        }
        return null;
    }

    public static boolean equalsAnyIgnoreCase(String str) {
        return valueOfIgnoreCase(str) != null;
    }
}