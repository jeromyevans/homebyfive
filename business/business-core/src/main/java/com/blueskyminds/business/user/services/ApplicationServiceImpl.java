package com.blueskyminds.business.user.services;

import com.blueskyminds.business.user.model.applications.Application;
import com.blueskyminds.business.user.dao.ApplicationDAO;
import com.google.inject.Inject;

/**
 * Date Started: 17/05/2008
 */
public class ApplicationServiceImpl implements ApplicationService {

    private ApplicationDAO applicationDAO;

    public ApplicationServiceImpl(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }

    public ApplicationServiceImpl() {
    }

    public Application createApplication(Application application) {
        application = applicationDAO.persist(application);
        return application;
    }

    @Inject
    public void setApplicationDAO(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }
}
