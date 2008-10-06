package com.blueskyminds.business.user.services;

import com.blueskyminds.homebyfive.framework.core.test.JPATestCase;
import com.blueskyminds.homebyfive.framework.core.tools.CryptoTools;
import com.blueskyminds.business.user.dao.UserProfileDAO;
import com.blueskyminds.business.user.dao.UserGroupDAO;
import com.blueskyminds.business.user.dao.ApplicationDAO;
import com.blueskyminds.business.user.model.users.UserProfile;
import com.blueskyminds.business.user.model.users.UserGroup;
import com.blueskyminds.business.user.model.applications.Application;
import com.blueskyminds.business.user.model.beans.UserSettings;
import com.blueskyminds.business.user.model.token.TokenProvider;
import com.blueskyminds.business.user.model.UserAccount;

import java.util.Map;

import junit.framework.Assert;

/**
 * Date Started: 17/05/2008
 */
public class UserServiceTest extends JPATestCase {


    private static final String PERSISTENCE_UNIT = "TestPremisePersistenceUnit";

    private ApplicationService applicationService;
    private UserService userService;
    private UserProfileDAO userProfileDAO;
    private UserGroupDAO userGroupDAO;
    private ApplicationDAO applicationDAO;

    private Application application1;
    private UserProfile userProfile1;

    public UserServiceTest() {
        super(PERSISTENCE_UNIT);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        userProfileDAO = new UserProfileDAO(em);
        userGroupDAO = new UserGroupDAO(em);
        applicationDAO = new ApplicationDAO(em);
        userService = new UserServiceImpl(userProfileDAO, userGroupDAO, null);
        applicationService = new ApplicationServiceImpl(applicationDAO);

        application1 = new Application(TokenProvider.nextToken(), "app1", "an example application");
        application1.addSetting("x", "1");
        application1.addSetting("y", "2");

        Application application2 = new Application(TokenProvider.nextToken(), "app12", "another example application");
        application2.addSetting("a", "1");
        application2.addSetting("b", "2");

        Application application3 = new Application(TokenProvider.nextToken(), "app13", "third example application");
        application3.addSetting("m", "1");
        application3.addSetting("n", "2");

        Application application4 = new Application(TokenProvider.nextToken(), "app14", "fourth example application");
        application3.addSetting("o", "1");
        application3.addSetting("p", "2");

        applicationService.createApplication(application1);
        applicationService.createApplication(application2);
        applicationService.createApplication(application3);
        applicationService.createApplication(application4);

        UserGroup userGroup = new UserGroup("default");
        userGroup.enableApplication(application1);
        userGroup.enableApplication(application2);

        userService.createUserGroup(userGroup);

        UserAccount userAccount = new UserAccount("user", CryptoTools.hashSHA("pwd"));
        userProfile1 = new UserProfile(userAccount);
        userProfile1.enableApplication(application3);

        em.persist(userProfile1);
    }

    public void testLookupUserProfile() {
        UserProfile userProfile = userProfileDAO.lookupUserProfile(userProfile1.getId());
        assertNotNull(userProfile);
    }

    public void testLookupUserSettings() {
        UserSettings userSettings = userService.lookupUserSettings(userProfile1.getId());
        assertNotNull(userSettings);
        Assert.assertEquals(3, userSettings.getSettings().size()); // 3 applications enabled
        Map<String, String> app1Settings = userSettings.getSettings(application1);
        assertNotNull(app1Settings);
    }
}
