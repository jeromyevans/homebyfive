package com.blueskyminds.housepad.core.user.services;

import com.blueskyminds.housepad.core.user.model.users.UserProfile;
import com.blueskyminds.housepad.core.user.model.users.UserGroup;
import com.blueskyminds.housepad.core.user.model.beans.UserSettings;

/**
 * Date Started: 9/05/2008
 */
public interface UserService {

    /**
     * Lookup the settings for the user identified by their UserId
     *
     * @param userId
     * @return
     */
    UserSettings lookupUserSettings(Long userId);

    /**
     * Lookup the profile for the user identified by their UserId
     *
     * @param userId
     * @return
     */
    UserProfile lookupUserProfile(Long userId);

    /**
     * Lookup UserProfile by the user's verification string 
     * @param verification
     * @return
     */
    UserProfile lookupUserProfile(String verification);

    /** Create a new unverified UserProfile */
    UserProfile registerUser(UserProfile userProfile);

    /**
     * Verify a UserProfile so it becomes active
     *
     * @param verification
     * @return
     */
    UserProfile verifyUser(String verification) throws UnknownUserException;

    /**
     * Persists a new UserGroup
    */
    UserGroup createUserGroup(UserGroup userGroup);
}
