package com.blueskyminds.housepad.core.user.model.beans;

import java.io.Serializable;

/**
 * Date Started: 16/05/2008
 */
public class UserSettingsEntry implements Serializable {

    private String name;
    private String value;

    public UserSettingsEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
