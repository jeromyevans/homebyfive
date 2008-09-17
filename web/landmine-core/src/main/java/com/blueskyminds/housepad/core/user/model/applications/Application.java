package com.blueskyminds.housepad.core.user.model.applications;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.framework.tools.Named;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.framework.tools.filters.Filter;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * An application in the HousePad system
 *
 * Date Started: 16/05/2008
 */
@Entity
@Table(name="Application")
public class Application extends AbstractDomainObject implements Named {

    private String token;
    private String name;
    private String description;
    private Set<ApplicationSetting> settings;

    public Application(String token, String name, String description) {
        this.token = token;
        this.name = name;
        this.description = description;
        init();
    }

    public Application() {
        init();
    }

    private void init() {
        settings = new HashSet<ApplicationSetting>();
    }

    @Basic
    @Column(name="Token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name="Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)    
    public Set<ApplicationSetting> getSettings() {
        return settings;
    }

    public void setSettings(Set<ApplicationSetting> settings) {
        this.settings = settings;
    }

    /**
     * Get the settings as a simple map
     *
     * @return
     */
    @Transient
    public Map<String, String> getSettingsMap() {
        Map<String, String> settingsMap = new HashMap<String, String>();
        for (ApplicationSetting setting : settings) {
            settingsMap.put(setting.getName(), setting.getDefaultValue());
        }
        return settingsMap;
    }

    @Transient
    public ApplicationSetting getSetting(final String name) {
        return FilterTools.getFirstMatching(settings, new Filter<ApplicationSetting>() {
            public boolean accept(ApplicationSetting setting) {
                return setting.getName().equals(name);
            }
        });
    }

    /**
     * Add a new application setting. If it already exists the existing default value is changed
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public ApplicationSetting addSetting(String name, String defaultValue) {
        ApplicationSetting existing = getSetting(name);
        if (existing != null) {
            existing.setDefaultValue(defaultValue);
            return existing;
        } else {
            ApplicationSetting applicationSetting = new ApplicationSetting(this, name, defaultValue);
            settings.add(applicationSetting);
            return applicationSetting;
        }
    }
}
