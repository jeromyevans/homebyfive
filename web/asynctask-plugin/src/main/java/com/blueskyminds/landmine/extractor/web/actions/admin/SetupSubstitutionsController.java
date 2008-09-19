package com.blueskyminds.landmine.extractor.web.actions.admin;

import com.blueskyminds.framework.tools.substitutions.SubstitutionsFileReader;
import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.config.Results;
import org.apache.struts2.config.Result;

import javax.persistence.EntityManager;

/**
 * Installs the initial substitution patterns. this only needs to be executed once ever.
 *
 * Date Started: 19/03/2008
 * <p/>
 * History:
 */
@Results(
        {@Result(name = "success", value = "/success.jsp")}
)
public class SetupSubstitutionsController extends ActionSupport {

    private static final Log LOG = LogFactory.getLog(SetupSubstitutionsController.class);

    private static final String ADDRESS_PATTERNS_FILE_NAME = "addressSubstitutions.csv";
    private static final String MEASUREMENT_SUBSTITUTIONS = "measurementSubstitutions.csv";
    private static final String PROPERTY_TYPE_SUBSTITUTIONS = "propertyTypeSubstitutions.csv";
    private static final String ASKING_PRICE_SUBSTITUTIONS = "askingPriceTypesSubstitutions.csv";
    private static final String PERIOD_TYPE_SUBSTITUTIONS = "periodTypesSubstitutions.csv";

    private EntityManager em;

    @Transactional
    public String index() throws Exception {

        SubstitutionsFileReader.readCsvAndPersist(ADDRESS_PATTERNS_FILE_NAME, em);
        SubstitutionsFileReader.readCsvAndPersist(MEASUREMENT_SUBSTITUTIONS, em);
        SubstitutionsFileReader.readCsvAndPersist(PROPERTY_TYPE_SUBSTITUTIONS, em);
        SubstitutionsFileReader.readCsvAndPersist(ASKING_PRICE_SUBSTITUTIONS, em);
        SubstitutionsFileReader.readCsvAndPersist(PERIOD_TYPE_SUBSTITUTIONS, em);

        return "success";
    }

    @Inject
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
}