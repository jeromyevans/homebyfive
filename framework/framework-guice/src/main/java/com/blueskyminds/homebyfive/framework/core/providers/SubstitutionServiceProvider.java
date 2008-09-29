package com.blueskyminds.homebyfive.framework.core.providers;

import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionServiceImpl;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.service.SubstitutionService;
import com.blueskyminds.homebyfive.framework.core.tools.substitutions.dao.SubstitutionDAO;
import com.google.inject.Provider;
import com.google.inject.Inject;

/**
 * Provides a simple SubstitutionService created with the provided SubstutionDAO
 *
 * Date Started: 5/05/2008
 * <p/>
 * History:
 */
public class SubstitutionServiceProvider implements Provider<SubstitutionService> {

    private Provider<SubstitutionDAO> substitutionDAO;

    public SubstitutionService get() {
        return new SubstitutionServiceImpl(substitutionDAO.get());
    }

    @Inject
    public void setSubstitutionDAO(Provider<SubstitutionDAO> substitutionDAOProvider) {
        this.substitutionDAO = substitutionDAOProvider;
    }
}