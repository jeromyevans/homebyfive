package com.blueskyminds.homebyfive.framework.core.tools.substitutions;

import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvOptions;
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvTextReader;
import com.blueskyminds.homebyfive.framework.core.tools.ResourceTools;
import com.blueskyminds.homebyfive.framework.core.persistence.PersistenceServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a CSV file defining substitutions
 *
 * Date Started: 12/01/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class SubstitutionsFileReader {

    private static final Log LOG = LogFactory.getLog(SubstitutionsFileReader.class);
    private static final String NULL_VALUE = "\\N";

    public SubstitutionsFileReader() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the SubstitutionsFileReader with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Reads a CSV file of substitutions */
    public List<Substitution> readCsv(String filename) throws IOException {
        return readCsv(ResourceTools.openStream(filename));
    }

    /** Reads a CSV file of substitutions */
    public List<Substitution> readCsv(InputStream inputStream) throws IOException {
        List<Substitution> substitutions = new LinkedList<Substitution>();

        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);

        CsvTextReader csvReader = null;

        csvReader = new CsvTextReader(inputStream, csvOptions);
        int patterns = 0;

        while (csvReader.read()) {
            if (csvReader.isNonBlank()) {
                String[] values = csvReader.getAsStrings();

                if (values.length > 0) {

                    // convert '\N' to null
                    for (int index = 0; index < values.length; index++) {
                        if (NULL_VALUE.equals(values[index])) {
                            values[index] = null;
                        }
                    }

                    try {
                        boolean exclusive = true;
                        int groupNo = -1;

                        String groupName = values[0];
                        String pattern = values[1];
                        String subst = values[2];
                        String description = values[3];
                        String seqNo = values[5];
                        String exclusiveFlag = values[6];
                        String groupString = values[7];
                        String metadata = values[8];

                        if (exclusiveFlag.equals("0")) {
                            exclusive = false;
                        }

                        try {
                            groupNo = Integer.parseInt(groupString);
                        } catch (NumberFormatException e) {
                            // couldn't get a valid group number
                        }

                        substitutions.add(new Substitution(groupName, description, pattern, subst, exclusive, groupNo, metadata));
                        patterns++;
                    }
                    catch (ArrayIndexOutOfBoundsException e2) {
                        LOG.warn("Couldn't parse CSV line - skipped line = '"+ StringUtils.join(values,",")+"'");
                    }
                }
            }
        }

        return substitutions;
    }

    /** Helper that can load a CSV file of substitutions and persist them
     * @return number of substitutions successfully persisted */
    public static int readCsvAndPersist(String fileName, EntityManager em) throws PersistenceServiceException {

        List<Substitution> substitutions;
        int patterns = 0;
        SubstitutionsFileReader substitutionsFileReader = new SubstitutionsFileReader();

        try {
            substitutions = substitutionsFileReader.readCsv(fileName);
        } catch (IOException e) {
            throw new PersistenceServiceException(e);
        }

        for (Substitution substitution : substitutions) {
            em.persist(substitution);
            patterns++;
        }
        em.flush();

        return patterns;
    }
}
