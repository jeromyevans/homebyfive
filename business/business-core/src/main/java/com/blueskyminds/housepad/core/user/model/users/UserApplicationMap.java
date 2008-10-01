package com.blueskyminds.housepad.core.user.model.users;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.housepad.core.user.model.applications.ApplicationStatus;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Enable an application for a user
 *
 * Date Started: 16/05/2008
 */
@Entity
@Table(name="UserApplicationMap")
public class UserApplicationMap extends AbstractDomainObject {

    private UserProfile userProfile;
    private Application application;
    private ApplicationStatus applicationStatus;
    private Set<UserApplicationSetting> settings;

    public UserApplicationMap(UserProfile userProfile, Application application) {
        this.userProfile = userProfile;
        this.application = application;
        this.settings = new HashSet<UserApplicationSetting>();
        this.applicationStatus = ApplicationStatus.Enabled;
    }

    public UserApplicationMap() {
    }

    @ManyToOne
    @JoinColumn(name="UserProfileId")
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @ManyToOne
    @JoinColumn(name="ApplicationId")
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Enumerated
    @Column(name="ApplicationStatus")
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @OneToMany(mappedBy = "userApplicationMap", cascade = CascadeType.ALL)
    public Set<UserApplicationSetting> getSettings() {
        return settings;
    }

    public void setSettings(Set<UserApplicationSetting> settings) {
        this.settings = settings;
    }

    @Transient
    public boolean isEnabled() {
        return ApplicationStatus.Enabled.equals(applicationStatus);
    }
}
