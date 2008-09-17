package com.blueskyminds.housepad.core.user.model.beans;

import com.blueskyminds.housepad.core.user.model.beans.UserApplicationSettings;
import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.housepad.core.user.model.users.UserProfile;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Application/component specific settings for a user, filtered for the current set of applications for the user
 * Intended for serialization
 *
 * Date Started: 8/05/2008
 */
public class UserSettings implements Serializable {

    private String username;
    private String[] roles;
    /** Settings keyed by application token, then name and value pairs */
    private Map<String, Map<String, String>> settings;

    public UserSettings(UserProfile userProfile) {
        this.username = userProfile.getUsername();
        this.roles = userProfile.getUserAccount().getRoles();
        this.settings = new HashMap<String, Map<String, String>>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Current roles for this user
     * @return
     */
    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Map<String, Map<String, String>> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Map<String, String>> settings) {
        this.settings = settings;
    }

    /**
     * Adds an application and default settings
     *
     * @param application
     * @return
     */
    public void addApplication(Application application, Set<? extends Setting> appSettings) {
        Map<String, String> settingsMap = application.getSettingsMap();

        for (Setting setting : appSettings) {
            // override an existing entry / set new value
            settingsMap.put(setting.getName(), setting.getValue());
        }

        settings.put(application.getToken(), settingsMap);
    }


    /**
     * Get settings for a specific application
     *
     * @param application
     * @return the map, or null if the application is not defined
     */
    public Map<String, String> getSettings(Application application) {
        if (application != null) {
            return settings.get(application.getToken());
        } else {
            return null;
        }
    }
}
