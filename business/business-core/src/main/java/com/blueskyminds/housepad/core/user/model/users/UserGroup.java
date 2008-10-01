package com.blueskyminds.housepad.core.user.model.users;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.housepad.core.user.model.applications.ApplicationStatus;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Group-level settings for users
 *
 * Date Started: 16/05/2008
 */
@Entity
@Table(name="UserGroup")
public class UserGroup extends AbstractDomainObject {

    private String key;
    private Set<UserGroupApplicationMap> applicationMaps;

    public UserGroup(String key) {
        this.key = key;
        init();
    }

    public UserGroup() {
        init();
    }

    private void init() {
        applicationMaps = new HashSet<UserGroupApplicationMap>();
    }

    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    public Set<UserGroupApplicationMap> getApplicationMaps() {
        return applicationMaps;
    }

    public void setApplicationMaps(Set<UserGroupApplicationMap> applicationMaps) {
        this.applicationMaps = applicationMaps;
    }

    /**
     * Get the applications mapped to this group and enabled
     * @return
     */
    @Transient
    public Set<Application> getApplications() {
        return new HashSet<Application>(FilterTools.getTransformed(applicationMaps, new Transformer<UserGroupApplicationMap, Application>() {
            public Application transform(UserGroupApplicationMap userGroupApplicationMap) {
                if (userGroupApplicationMap.isEnabled()) {
                    return userGroupApplicationMap.getApplication();
                } else {
                    return null;
                }
            }
        }));
    }

    /**
     * Get the mapping to the specified application if it exists
     *
     * @param application
     * @return
     */
    @Transient
    public UserGroupApplicationMap getApplicationMap(final Application application) {
        return FilterTools.getFirstMatching(applicationMaps, new Filter<UserGroupApplicationMap>() {
            public boolean accept(UserGroupApplicationMap map) {
                return map.getApplication().equals(application);
            }
        });
    }


    /**
     * Enables the specified application for this UserGroup.  If a mapping already exists it will be updated,
     * otherwise a new mapping will be created
     * @param application
     */
    public void enableApplication(Application application) {
        UserGroupApplicationMap existing = getApplicationMap(application);
        if (existing != null) {
            existing.setApplicationStatus(ApplicationStatus.Enabled);
        } else {
            applicationMaps.add(new UserGroupApplicationMap(this, application));
        }
    }
}
