package com.blueskyminds.homebyfive.business.address.tools;

import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvOptions;
import com.blueskyminds.homebyfive.framework.core.tools.csv.CsvTextReader;
import com.blueskyminds.homebyfive.framework.core.tools.DebugTools;

import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads the Australia Post pc-full database from a csv file
 *
 * Date Started: 8/03/2008
 * <p/>
 * History:
 */
public class AusPostCodes {

    private static final Log LOG = LogFactory.getLog(AusPostCodes.class);

    private static final String SUBURB_CATEGORY = "delivery area";
    private static final String PO_BOX_CATEGORY = "post office boxes";

    public static class State {
        String name;
        Set<PostCode> postCodes;
        Set<Suburb> suburbs;

        public State(String name) {
            this.name = name;
            postCodes = new HashSet<PostCode>();
            suburbs = new HashSet<Suburb>();
        }

        public String getName() {
            return name;
        }

        public Set<PostCode> getPostCodes() {
            return postCodes;
        }

        public Set<Suburb> getSuburbs() {
            return suburbs;
        }

        public void addPostCode(PostCode postCode) {
            postCodes.add(postCode);
        }

        public void addSuburb(Suburb suburb) {
            suburbs.add(suburb);
        }
    }

    public static class PostCode {
        private String name;
        private Set<Suburb> suburbs;

        public PostCode(String name) {
            this.name = name;
            suburbs = new HashSet<Suburb>();
        }

        public String getName() {
            return name;
        }

        public Set<Suburb> getSuburbs() {
            return suburbs;
        }

        public void addSuburb(Suburb suburb) {
            suburbs.add(suburb);
        }
    }

    public static class Suburb {
        private String name;
        private PostCode postCode;

        public Suburb(String name, PostCode postCode) {
            this.name = name;
            this.postCode = postCode;
        }

        public String getName() {
            return name;
        }

        public PostCode getPostCode() {
            return postCode;
        }
    }

    /**
     * Loads a CSV file of all the suburbs defined in Australia
     **/
    public static Collection<State> readCsv(InputStream in) {
        Map<String, State> stateHash = new HashMap<String, State>();
        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);

        CsvTextReader csvReader = new CsvTextReader(in, csvOptions);
        Map<String, PostCode> postCodeHash = new HashMap<String, PostCode>();
        State state;

        int suburbs = 0;
        int postCodes = 0;

        while (csvReader.read()) {
            if (csvReader.isNonBlank()) {
                String[] values = csvReader.getAsStrings();
                String postCodeValue = StringUtils.upperCase(values[0]);
                // assert that the postcode is a numeric value
                if (StringUtils.isNumeric(postCodeValue)) {
                    String suburbValue = WordUtils.capitalize(StringUtils.lowerCase(values[1]));
                    String stateValue = StringUtils.upperCase(values[2]);
                    String category = StringUtils.lowerCase(StringUtils.trim(values[9]));

                    if (SUBURB_CATEGORY.equals(category)) {
                        // lookup the state...
                        if (stateHash.containsKey(stateValue)) {
                            state = stateHash.get(stateValue);
                        } else {
                            //state = australia.getStateByAbbr(stateValue);
                            StopWatch stopWatch = new StopWatch();
                            stopWatch.start();
                            state = new State(stateValue);
                            stateHash.put(stateValue, state);
                            stopWatch.stop();
                        }


                        PostCode postCode = postCodeHash.get(postCodeValue);
                        if (postCode == null) {
                            // create a new postcode and add it to the state
                            postCode = new PostCode(postCodeValue);
                            postCodeHash.put(postCodeValue, postCode);
                            state.addPostCode(postCode);
                            postCodes++;
                        }

                        // create the suburb
                        Suburb suburb = new Suburb(suburbValue, postCode);
                        postCode.addSuburb(suburb);
                        state.addSuburb(suburb);

                        suburbs++;
                        if (LOG.isInfoEnabled()) {
                            if (suburbs % 100 == 0) {
                                LOG.info(suburbs+" suburbs "+postCodes+" postcodes");
                                DebugTools.printAvailableHeap();
                            }
                        }
                    }
                }
            }
        }

        LOG.debug("Read "+suburbs+" suburbs and "+postCodes+" postcodes");

        return stateHash.values();
    }

}
