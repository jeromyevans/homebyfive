package com.blueskyminds.business.user.dao;

import com.blueskyminds.business.user.model.UserAccount;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface UserAccountDAO {
    UserAccount lookupUserAccount(String username, String hashedPassword);

    boolean usernameExists(String username);

    UserAccount lookupUserAccount(String username);

    UserAccount persist(UserAccount userAccount);
}
