package com.blueskyminds.landmine.core.property;

/**
 * Types of real property
 *
 * Date Created: 15 Feb 2007
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */

public enum PropertyTypes {

   Unknown,
   Land,
   House,
   Unit,
   Villa,
   Semi,
   Apartment,
   Complex;

    /**
     * Provides the type that is a parent of this type.  This is really only applicable for units, villas
     * and apartments
     *
     * @return
     */
    public PropertyTypes getParentType() {
        PropertyTypes parent = null;
        switch (this) {
            case Apartment:
                parent = Complex;
                break;
            case Complex:
                parent = null;
                break;
            case House:
                parent = null;
                break;
            case Land:
                parent = null;
                break;
            case Semi:
                parent = null;
                break;
            case Unit:
                parent = Complex;
                break;
            case Unknown:
                parent = null;
                break;
            case Villa:
                parent = Complex;
                break;
        }

        return parent;
    }

    public boolean isHome() {
        return this.equalsAny(Apartment, House, Semi, Unit, Villa);
    }

    public boolean isLandIncluded() {
        return this.equalsAny(Land, House, Villa, Semi);
    }

    public boolean isComplex() {
        return this.equals(Complex);
    }

    public boolean isBuilding() {
        return this.isHome() || isComplex();
    }

    public boolean equalsAny(PropertyTypes... propertyTypes) {
        if (propertyTypes != null) {
            for (PropertyTypes type : propertyTypes) {
                if (this.equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

}