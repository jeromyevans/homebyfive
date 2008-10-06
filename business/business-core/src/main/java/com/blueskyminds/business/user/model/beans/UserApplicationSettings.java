package com.blueskyminds.business.user.model.beans;

import com.blueskyminds.business.user.model.beans.UserSettingsEntry;
import com.blueskyminds.business.user.model.applications.Application;
import com.blueskyminds.business.user.model.applications.ApplicationSetting;

import java.util.Set;
import java.util.HashSet;

/**
 * Date Started: 16/05/2008
 */
public class UserApplicationSettings {

    private Set<UserSettingsEntry> settings;

    /**
     * Create new UserApplicationSettings populated with the defaults from the application
     *
     * @param application
     */
    public UserApplicationSettings(Application application) {
        settings = new HashSet<UserSettingsEntry>();
        for (ApplicationSetting setting : application.getSettings()) {
            if (setting.isValid()) {
                settings.add(new UserSettingsEntry(setting.getName(), setting.getDefaultValue()));
            }
        }
    }

    public Set<UserSettingsEntry> getSettings() {
        return settings;
    }

    public void setSettings(Set<UserSettingsEntry> settings) {
        this.settings = settings;
    }

    public UserApplicationSettings withSettings(Set<? extends Setting> appSettings) {
         for (Setting setting : appSettings) {            
            UserSettingsEntry existing = getSetting(setting.getName());
            if (existing != null) {
                // override an existing entry
                existing.setValue(setting.getValue());
            } else {
                // add a new entry
                settings.add(new UserSettingsEntry(setting.getName(), setting.getValue()));
            }

        }
        return this;
    }

    /** Get a setting by its name
     * @return the setting if found, otherwise null */
    public UserSettingsEntry getSetting(String name) {
        for (UserSettingsEntry entry : settings) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        return null;
    }
}
