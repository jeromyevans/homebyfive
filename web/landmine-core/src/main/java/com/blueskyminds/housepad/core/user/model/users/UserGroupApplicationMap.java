package com.blueskyminds.housepad.core.user.model.users;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.housepad.core.user.model.applications.ApplicationStatus;

import javax.persistence.*;

/**
 * Map an application for a UserGroup
 *
 * Date Started: 16/05/2008
 */
@Entity
@Table(name="UserGroupApplicationMap")
public class UserGroupApplicationMap extends AbstractDomainObject {

    private Application application;
    private UserGroup userGroup;
    private ApplicationStatus applicationStatus;

    public UserGroupApplicationMap(UserGroup userGroup, Application application) {
        this.application = application;
        this.userGroup = userGroup;
        applicationStatus = ApplicationStatus.Enabled;
    }

    public UserGroupApplicationMap() {
    }

    @ManyToOne
    @JoinColumn(name="UserGroupId")
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
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

    @Transient
    public boolean isEnabled() {
        return ApplicationStatus.Enabled.equals(applicationStatus);
    }
}
