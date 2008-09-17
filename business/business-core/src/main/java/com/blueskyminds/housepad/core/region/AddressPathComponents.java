package com.blueskyminds.housepad.core.region;

import com.blueskyminds.enterprise.address.StreetType;
import com.blueskyminds.enterprise.address.StreetSection;
import com.blueskyminds.enterprise.tools.KeyGenerator;
import com.blueskyminds.framework.tools.text.StringTools;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * Converts an AddressPath into its path components
 *
 * Date Started: 5/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class AddressPathComponents {

    private String countryPath;
    private String statePath;
    private String suburbPath;
    private String streetPath;
    private String postCodePath;
    private String streetNoPath;
    private String unitNoPath;

    private String streetName;
    private StreetType streetType;
    private StreetSection streetSection;
    private String streetNo;
    private String unitNo;


    public AddressPathComponents(String path) {

        Map<String, String> components = AddressPathParser.process(path);

        if (components.containsKey(AddressPathParser.COUNTRY)) {
            countryPath = PathHelper.buildPath(components.get(AddressPathParser.COUNTRY));

            if (components.containsKey(AddressPathParser.STATE)) {
                statePath = PathHelper.buildPath(countryPath, components.get(AddressPathParser.STATE));

                if (components.containsKey(AddressPathParser.SUBURB)) {
                    suburbPath = PathHelper.buildPath(statePath, components.get(AddressPathParser.SUBURB));

                    if (components.containsKey(AddressPathParser.STREET)) {
                        // this processing of the street components is a bit rough.  It assumes the street
                        // has already been processed into well-formed components and concatenated

                        String streetComponents = components.get(AddressPathParser.STREET);
                        streetPath = PathHelper.buildPath(suburbPath, streetComponents);

                        // determine if there's a street section
                        String lastWord = StringUtils.substringAfterLast(streetComponents, "+");
                        streetSection = StreetSection.valueOfIgnoreCase(lastWord);
                        if (streetSection != null) {
                            streetComponents = StringUtils.substringBeforeLast(streetComponents, "+");
                        }
                        
                        // extract street type and name
                        if (streetComponents.startsWith("the+")) {
                            streetName = WordUtils.capitalize(KeyGenerator.unescapeId(StringUtils.substringAfter(streetComponents, "the+")));
                            streetType = StreetType.The;
                        } else {
                            String streetNamePart = KeyGenerator.unescapeId(StringUtils.substringBeforeLast(streetComponents, "+"));
                            streetName = WordUtils.capitalize(streetNamePart);
                            streetType = StreetType.valueOfIgnoreCase(KeyGenerator.unescapeId(StringUtils.substringAfter(streetComponents, streetNamePart)));
                        }

                        if (components.containsKey(AddressPathParser.STREETNO)) {
                            streetNo = KeyGenerator.unescapeId(components.get(AddressPathParser.STREETNO));
                            streetNoPath = PathHelper.buildPath(streetPath, streetNo);

                            if (components.containsKey(AddressPathParser.UNITNO)) {
                                unitNo = KeyGenerator.unescapeId(components.get(AddressPathParser.UNITNO));
                                unitNoPath = PathHelper.buildPath(streetNoPath, unitNo);
                            }
                        }
                    }
                }

                if (components.containsKey(AddressPathParser.POSTCODE)) {
                    postCodePath = PathHelper.buildPath(statePath, components.get(AddressPathParser.POSTCODE));
                }
            }
        }

    }

    public String getCountryPath() {
        return countryPath;
    }

    public String getStatePath() {
        return statePath;
    }

    public String getSuburbPath() {
        return suburbPath;
    }

    public String getStreetPath() {
        return streetPath;
    }

    public String getPostCodePath() {
        return postCodePath;
    }

    public String getStreetNoPath() {
        return streetNoPath;
    }

    public String getUnitNoPath() {
        return unitNoPath;
    }

    public String getStreetName() {
        return streetName;
    }

    public StreetType getStreetType() {
        return streetType;
    }

    public StreetSection getStreetSection() {
        return streetSection;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public String getUnitNo() {
        return unitNo;
    }
}
