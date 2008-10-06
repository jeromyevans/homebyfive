package com.blueskyminds.homebyfive.business.user.dao;

import com.blueskyminds.homebyfive.business.user.model.UserRole;

import java.util.List;

/**
 * Date Started: 14/09/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public interface UserRoleDAO {
    List<UserRole> listRoles();

    UserRole create(String name);

    UserRole lookupByName(String name);

    boolean roleExists(String name);
}
