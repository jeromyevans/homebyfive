package com.blueskyminds.housepad.core.user.services;

import com.blueskyminds.housepad.core.user.model.users.*;
import com.blueskyminds.housepad.core.user.model.applications.Application;
import com.blueskyminds.housepad.core.user.model.beans.UserSettings;
import com.blueskyminds.housepad.core.user.dao.UserProfileDAO;
import com.blueskyminds.housepad.core.user.dao.UserGroupDAO;
import com.blueskyminds.homebyfive.framework.core.events.EventRegistry;
import com.blueskyminds.housepad.core.user.services.UserEvents;
import com.blueskyminds.struts2.securityplugin.services.UserAccountService;
import com.google.inject.Inject;

import java.util.Date;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Date Started: 13/05/2008
 */
public class UserServiceImpl implements UserService {

    private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

    private static final String DEFAULT_USER_GROUP = "default";

    private UserProfileDAO userProfileDAO;
    private UserGroupDAO userGroupDAO;
    private EventRegistry eventRegistry;
    private UserAccountService userAccountService;

    public UserServiceImpl(UserProfileDAO userProfileDAO, UserGroupDAO userGroupDAO, EventRegistry eventRegistry) {
        this.userProfileDAO = userProfileDAO;
        this.userGroupDAO = userGroupDAO;
        this.eventRegistry = eventRegistry;
    }

    public UserServiceImpl() {
    }

    /**
     * Lookup the settings for the user identified by their UserId
     *
     * @param userId
     * @return
     */
    public UserSettings lookupUserSettings(Long userId) {
        UserSettings userSettings = null;
        UserProfile userProfile = lookupUserProfile(userId);

        if (userProfile != null) {
            userSettings = buildUserSettings(userProfile);
        }
        return userSettings;
    }

    private UserGroup lookupDefaultUserGroup() {
        return userGroupDAO.lookupUserGroup(DEFAULT_USER_GROUP);
    }

    /**
     * Creates a bean containing all the user settings for the current applications in the UserProfile
     * @param userProfile
     * @return
     */
    private UserSettings buildUserSettings(UserProfile userProfile) {
        Set<Application> applicationsAdded = new HashSet<Application>();
        UserSettings userSettings = new UserSettings(userProfile);
        UserGroup userGroup = userProfile.getUserGroup();
        if (userGroup == null) {
            userGroup = lookupDefaultUserGroup();
        }

        if (userGroup != null) {
            // add the settings for all the enabled group-level applications
            for (UserGroupApplicationMap map : userGroup.getApplicationMaps()) {
                if (map.isEnabled()) {
                    // check that it's not disabled at the user level
                    Application application = map.getApplication();
                    if (!userProfile.isApplicationDisabled(application)) {
                        // add the application with the default settings, overridden by the user's specific settings
                        userSettings.addApplication(application, userProfile.getApplicationSettings(application));
                        applicationsAdded.add(application);
                    }
                }
            }
        }

        // add the settings for other applications enabled at the user-level
        for (UserApplicationMap map : userProfile.getApplicationMaps()) {
            if (map.isEnabled()) {
                Application application = map.getApplication();
                if (!applicationsAdded.contains(application)) {
                    // add the application with the default settings, overridden by the user's specific settings
                    userSettings.addApplication(application, userProfile.getApplicationSettings(application));                    
                    applicationsAdded.add(application);
                }
            }
        }
        return userSettings;
    }


    public UserProfile lookupUserProfile(Long userId) {
        return userProfileDAO.lookupUserProfile(userId);
    }

    public UserProfile lookupUserProfile(String verification) {
        return userProfileDAO.lookupUserProfile(verification);
    }

    private String generateVerificationCode() {
        return StringUtils.replaceChars(UUID.randomUUID().toString(), "-", "");
    }

    public UserProfile registerUser(UserProfile userProfile) {
        if (!userProfile.isPersistent()) {
            // assert that the username is unique
            if (!userAccountService.usernameExists(userProfile.getUsername())) {
                userProfile.setVerification(generateVerificationCode());
                userProfile = userProfileDAO.persist(userProfile);
                if (userProfile != null) {
                    if (eventRegistry != null) {
                        eventRegistry.fire(UserEvents.NEW_ACCOUNT_REGISTERED, userProfile.getId());
                    }
                }
                return userProfile;
            } else {
                LOG.error("Attempted to register a UserProfile for a non-unique username");
            }
        } else {
            LOG.error("Attempted to register a UserProfile for a profile that was already persistent");
            return null;
        }
        return null;
    }

    /**
     *
     * @param verification
     * @return the user account if verified, or null if the account was not Unverified
     * @throws UnknownUserException  if the verification string doesn't correspond to a user
     */
    public UserProfile verifyUser(String verification) throws UnknownUserException {

        UserProfile userProfile = userProfileDAO.lookupUserProfile(verification);
        if (userProfile != null) {
            if (UserProfileStatus.Unverified.equals(userProfile.getProfileStatus())) {
                userProfile.setDateVerified(new Date());
                userProfile.setProfileStatus(UserProfileStatus.Active);
                userProfile = userProfileDAO.persist(userProfile);
            } else {
                LOG.error("Attempted to verify a UserProfile that is not Unverified");
                userProfile = null;
            }
        } else {
            throw new UnknownUserException("No UserAccount corresponds to the Verification string: "+verification);
        }

        return userProfile;
    }

    public UserGroup createUserGroup(UserGroup userGroup) {
        return userGroupDAO.persist(userGroup);        
    }

    public UserProfile verifyUser(UserProfile userProfile) {
        userProfile.setProfileStatus(UserProfileStatus.Active);
        userProfile.setDateVerified(new Date());
        userProfile = userProfileDAO.persist(userProfile);

        if (userProfile != null) {
            if (eventRegistry != null) {
                eventRegistry.fire(UserEvents.NEW_ACCOUNT_VERIFIED, userProfile.getId());
            }
        }

        return userProfile;
    }

    @Inject
    public void setUserProfileDAO(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    @Inject
    public void setEventRegistry(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Inject
    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    @Inject
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
}
