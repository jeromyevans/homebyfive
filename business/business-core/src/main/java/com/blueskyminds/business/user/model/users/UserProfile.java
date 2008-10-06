package com.blueskyminds.business.user.model.users;

import com.blueskyminds.business.party.Individual;
import com.blueskyminds.business.tag.Tag;
import com.blueskyminds.business.tag.TagConstants;
import com.blueskyminds.business.tag.service.TagService;
import com.blueskyminds.business.contact.EmailAddress;
import com.blueskyminds.business.contact.POCRole;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.tools.filters.Filter;
import com.blueskyminds.business.user.model.UserAccount;
import com.blueskyminds.business.user.model.applications.Application;
import com.blueskyminds.business.user.model.applications.ApplicationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

/**
 * Date Started: 14/05/2008
 */
@Entity
@Table(name="UserProfile")
public class UserProfile extends AbstractDomainObject {

    private UserGroup userGroup;
    private UserAccount userAccount;
    private Individual party;
    private Date dateVerified;
    private UserProfileStatus userProfileStatus;
    private String verification;
    private Set<UserApplicationMap> applicationMaps;

     public UserProfile(UserAccount userAccount) {
        this.userAccount = userAccount;
        this.userProfileStatus = UserProfileStatus.Unverified;
        this.party = new Individual();
        init();
    }

    public UserProfile(UserProfileStatus userProfileStatus) {
        userAccount = new UserAccount();
        this.userProfileStatus = userProfileStatus;
        this.party = new Individual();
        init();
    }

    public UserProfile() {
        userAccount = new UserAccount();
        userProfileStatus = UserProfileStatus.Unverified;
        init();
    }

    private void init() {
        applicationMaps = new HashSet<UserApplicationMap>();
    }

    /**
     * The group that this user belongs to. A null value indicates the default group.
     *
     * @return
     */
    @ManyToOne
    @JoinColumn(name="UserGroupId")
    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserAccountId")
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "PartyId")
    public Individual getParty() {
        return party;
    }

    public void setParty(Individual party) {
        this.party = party;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DateVerified")
    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    /** Account verification token */
    @Basic
    @Column(name="Verification")
    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    @Enumerated
    @Column(name="ProfileStatus")
    public UserProfileStatus getProfileStatus() {
        return userProfileStatus;
    }

    public void setProfileStatus(UserProfileStatus status) {
        this.userProfileStatus = status;
    }

    @Transient
    public String getPrimaryPersonalEmail() {
        return party.getPrimaryPersonalEmail();
    }

    public void setPrimaryPersonalEmail(String email, TagService tagService) {
        Tag primaryTag = null;
        if (tagService != null) {
            primaryTag = tagService.lookupOrCreateTag(TagConstants.PRIMARY);
        }
        getParty().addEmailAddress((EmailAddress) new EmailAddress(email, POCRole.Personal).withTag(primaryTag));
    }

    /**
     * Applications mapped for the User
     * 
     * @return
     */
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    public Set<UserApplicationMap> getApplicationMaps() {
        return applicationMaps;
    }

    public void setApplicationMaps(Set<UserApplicationMap> applicationMaps) {
        this.applicationMaps = applicationMaps;
    }

    @Transient
    public String getUsername() {
        return userAccount.getUsername();
    }

    /**
     * Checks whether the specified application is explicitly disabled
     *
     * @param application
     * @return
     */
    @Transient
    public boolean isApplicationDisabled(Application application) {
        for (UserApplicationMap map : applicationMaps) {
            if (map.getApplication().equals(application)) {
                if (ApplicationStatus.Disabled.equals(map.getApplicationStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the user's settings for the specified application
     *
     * @param application
     * @return
     */
    @Transient
    public Set<UserApplicationSetting> getApplicationSettings(Application application) {
        for (UserApplicationMap map : applicationMaps) {
            if (map.getApplication().equals(application)) {
                return map.getSettings();
            }
        }

        return new HashSet<UserApplicationSetting>();
    }

    /**
     * Get the mapping to the specified application if it exists
     *
     * @param application
     * @return
     */
    @Transient
    public UserApplicationMap getApplicationMap(final Application application) {
        return FilterTools.getFirstMatching(applicationMaps, new Filter<UserApplicationMap>() {
            public boolean accept(UserApplicationMap map) {
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
        UserApplicationMap existing = getApplicationMap(application);
        if (existing != null) {
            existing.setApplicationStatus(ApplicationStatus.Enabled);
        } else {
            applicationMaps.add(new UserApplicationMap(this, application));
        }
    }
}

