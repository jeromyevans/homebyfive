package com.blueskyminds.homebyfive.business.region;

import org.apache.commons.lang.StringUtils;

import com.blueskyminds.homebyfive.business.address.*;
import com.blueskyminds.homebyfive.business.region.graph.Region;
import com.blueskyminds.homebyfive.business.region.graph.*;
import com.blueskyminds.homebyfive.business.tools.KeyGenerator;
import com.blueskyminds.homebyfive.framework.core.tools.text.StringTools;

/**
 * Date Started: 5/03/2008
 * <p/>
 * History:
 */
public class PathHelper {

    private static final String SEPARATOR = "/";
    public static final String ROOT = SEPARATOR;

    public static String buildPath(String country) {
        return joinPaths(ROOT, country);
    }

    public static String buildPath(String[] components) {
        return joinPaths(ROOT, components, null);
    }

    public static String buildPath(String[] components, String suffix) {
        return joinPaths(ROOT, components, suffix);
    }

    public static String buildPath(String country, String state) {
        return joinPaths(ROOT, country, state);
    }

    public static String buildPath(String country, String state, String suburb) {
        return joinPaths(ROOT, country, state, suburb);
    }

    public static String buildPath(String country, String state, String suburb, String street) {
        return joinPaths(ROOT, country, state, suburb, street);
    }

    public static String buildPath(String country, String state, String suburb, String street, String streetNo) {
        return joinPaths(ROOT, country, state, suburb, street, streetNo);
    }

    public static String buildPath(String country, String state, String suburb, String street, String streetNo, String unitNo) {
        return joinPaths(ROOT, country, state, suburb, street, streetNo, unitNo);
    }

    public static String buildPath(Country countryHandle) {
        return joinPaths(ROOT, KeyGenerator.generateId(countryHandle.getAbbr()));
    }

    public static String buildPath(State stateHandle) {
        return joinPaths(stateHandle.getParentPath(), KeyGenerator.generateId(stateHandle.getAbbr()));
    }

    public static String buildPath(Suburb suburbHandle) {
        return joinPaths(suburbHandle.getParentPath(), KeyGenerator.generateId(suburbHandle.getName()));
    }

    public static String buildPath(PostalCode postCodeHandle) {
        return joinPaths(postCodeHandle.getParentPath(), KeyGenerator.generateId(postCodeHandle.getName()));
    }

    public static String buildPath(Suburb suburbHandle, Street street) {
        return joinPaths(suburbHandle.getParentPath(), KeyGenerator.generateId(suburbHandle.getName()), buildStreetNameKey(street));
    }

    public static String buildPath(Region regionHandle) {
        String path = null;
        switch (regionHandle.getType()) {
            case Country:
                path = buildPath((Country) regionHandle.unproxy().getModel());
                break;
            case PostCode:
                path = buildPath((PostalCode) regionHandle.unproxy().getModel());
                break;
            case Suburb:
                path = buildPath((Suburb) regionHandle.unproxy().getModel());
                break;
            case State:
                path = buildPath((State) regionHandle.unproxy().getModel());
                break;
        }
        return path;
    }

    public static String buildPath(Address address) {
        Suburb suburbHandle = address.getSuburb();
        if (suburbHandle != null) {
            if (address instanceof UnitAddress) {
                return joinPaths(buildPath(address.getSuburb(), address.getStreet()), KeyGenerator.generateId(((UnitAddress)address).getStreetNumber()), KeyGenerator.generateId(((UnitAddress)address).getUnitNumber()));
            } else {
                if (address instanceof LotAddress) {
                    // todo: the lot addresses are a bit crap
                    return joinPaths(buildPath(address.getSuburb(), address.getStreet()), KeyGenerator.generateId(((LotAddress)address).getStreetNumber()), KeyGenerator.generateId("lot"+((LotAddress)address).getLotNumber()));
                } else {
                    if (address instanceof StreetAddress) {
                        return joinPaths(buildPath(address.getSuburb(), address.getStreet()), KeyGenerator.generateId(((StreetAddress)address).getStreetNumber()));
                    }
                }
            }
        }
        return null;        
    }

    /** Concatenates multiple id's into a path */
    public static String joinPaths(String... ids) {
        StringBuilder path = new StringBuilder();
         if ((ids != null) && (ids.length > 0)) {
             for (String id : ids) {
                 if (id != null) {
                     if ((path.length() == 0) || (!(path.charAt(path.length()-1) == '/'))) {
                         path.append(SEPARATOR);
                     }
                     if (!id.startsWith(SEPARATOR)) {
                         path.append(KeyGenerator.generateId(id));
                     } else {
                         path.append(StringUtils.stripStart(id, SEPARATOR));
                     }
                 }
             }
            return path.toString();
         } else {
             return null;
         }
    }

      /** Concatenates multiple id's into a path */
    public static String joinPaths(String prefix, String[] parts, String suffix) {
          String result = joinPaths(parts);          
          result = joinPath(prefix, result);
          return joinPaths(result, suffix);           
      }

    public static String joinPath(String parentPath, String id) {
         return joinPaths(parentPath, id);
    }

    
    public static String generatePath(String parentPath, String key) {
        return joinPath(parentPath, KeyGenerator.generateId(key));
    }

    /**
     * Build a street name that can be used as a key
     * Concatenates the name and type, placing the type at the end except in special cases (and null cases)
     **/
    public static String buildStreetNameKey(String name, StreetType type, StreetSection section) {
        StringBuilder key;
        if (name != null) {
            if (type != null) {
                if (StreetType.The.equals(type)) {
                    key = new StringBuilder(KeyGenerator.generateId(type+" "+name));
                } else {
                    key = new StringBuilder(KeyGenerator.generateId(name+" "+type));
                }
            } else {
                key = new StringBuilder(KeyGenerator.generateId(name));
            }

            if ((section != null) && (!StreetSection.NA.equals(section))) {
                key.append("+"+KeyGenerator.generateId(section.toString()));
            }
            return key.toString();
        } else {
            return null;
        }                
    }

    public static String buildStreetNameKey(Street street) {
        if (street != null) {
            return buildStreetNameKey(street.getName(), street.getStreetType(), street.getSection());
        } else {
            return null;
        }
    }

    /**
     * Get the suburb subsection of a path if possible.  This assumes the suburb is the first three segments.
     * This will mistakenly extract a postcode if supplied with a postcode path
     *  */
    public static String suburbPath(String path) {
        String components[] = StringUtils.split(path, "/", 4);
        if (components.length >= 3) {
            return buildPath(components[0], components[1], components[2]);
        } else {
            return null;
        }
    }
  
}
