package com.blueskyminds.homebyfive.business.address;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

import com.blueskyminds.homebyfive.framework.core.tools.ArrayTools;

/**
 * Parses an AddressPath back into its components
 *
 * Date Started: 20/05/2008
 */
public class AddressPathParser {
    private static final char PATH_SEPARATOR = '/';

    private static final int INVALID_PATH = -1;
    private static final int SUBURB_PATH = 0;
    private static final int POSTCODE_PATH = 1;

    public static final String COUNTRY = "countryKey";
    public static final String STATE = "stateKey";
    public static final String SUBURB = "suburbKey";
    public static final String STREET = "streetKey";
    public static final String STREETNO = "streetNoKey";
    public static final String UNITNO = "unitNoKey";
    public static final String POSTCODE = "postCodeKey";

    /**
     * Allowed sequences of the components in the path
     */
    private static final String[][] PATH_TYPES = {
            { COUNTRY, STATE, SUBURB, STREET, STREETNO, UNITNO },
            { COUNTRY, STATE, POSTCODE }
    };

    private static final String[] METHOD_PATTERNS = { "new", "edit" };
    private static final String[] METHOD_NAMES = { "editNew", "edit" };

    private static final String[][] ACTIONS = {
            {"country", "state", "suburb", "street", "property", "property" },
            {"country", "state", "postCode", "property"}
    };

    /** Track information about each word in the path so they can be used to locate an action reference and
     * be quickly removed */
    private static final class Word {
        private String value;
        private int index;
        private String paramName;

        private Word(String value, int index) {
            this.value = value;
            this.index = index;
        }

        public boolean isParam() {
            return paramName != null;
        }

        public String getParamName() {
            return paramName;
        }

        public void setParamName(String paramName) {
            this.paramName = paramName;
        }

        public String getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }

    private static boolean equalsAny(String str, String[] values) {
        for (String value : values) {
            if (str.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean equalsAny(String str, Collection<String> values) {
        if (values != null) {
            for (String value : values) {
                if (str.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Composite of the current state so it can be passed between methods together */
    private static final class MatcherState {

        private String path;
        private Collection<String> actions;

        private Map<String, String> params;
        private int pathType;
        private int sectionNo;
        private int lastSectionUsed;

        private StringBuilder namespace;
        private StringBuilder action;
        private Stack<Word> wordStack;

        private StringBuilder current;

        private boolean leftOfAction;

        private MatcherState(String path, Collection<String> actions) {
            this.path = path;
            this.actions = actions;
            params = new HashMap<String, String>();
            pathType = 0;
            sectionNo = 0;
            lastSectionUsed = -1;

            namespace = new StringBuilder("");
            wordStack = new Stack<Word>();
            current = new StringBuilder();

            leftOfAction = true;
        }

        private boolean isMethod(String str) {
            return (equalsAny(str, METHOD_PATTERNS));
        }

        /** Map the method pattern to a name */
        private String getMethodName(String methodPattern) {
            int index = ArrayUtils.indexOf(METHOD_PATTERNS, methodPattern);
            if (index >= 0) {
                return METHOD_NAMES[index];
            } else {
                return null;
            }
        }

        private boolean isAction(String str) {
            return (equalsAny(str, actions));
        }

        /** Complete the current word in the path */
        public void completeSection() {
            if (current.length() > 0) {
                Word word = new Word(current.toString(), namespace.length());
                wordStack.push(word);

                if (isMethod(current.toString())) {
                    // this is a method name
                    params.put("method", getMethodName(current.toString()));
                } else {
                    if (isAction(current.toString())) {
                        action = new StringBuilder(current.toString());
                        leftOfAction = false;
                    } else {
                        if (leftOfAction && sectionNo < PATH_TYPES[pathType].length) {
                            // this is likely to be part of the path
                            // set the param name and value
                            params.put(PATH_TYPES[pathType][sectionNo], word.getValue());
                            namespace.append("/{"+PATH_TYPES[pathType][sectionNo]+"}");
                            word.setParamName(PATH_TYPES[pathType][sectionNo]);
                            lastSectionUsed = sectionNo;
                        } else {
                            if (leftOfAction) {
                                // this is part of the namespace
                                namespace.append("/"+word.getValue());
                            } else {
                                // this is part of the action name
                                action.append("/"+word.getValue());
                            }
                        }
                    }
                }

                sectionNo++;
                current = new StringBuilder();
            }
        }

        /**
         * Append the character to the current word.
         *
         * Checks whether the current character means our path type has changed or the path matching is aborted
         *
         * @param ch
         * @return true if parsing should continue
         */
        public boolean append(char ch) {
            current.append(ch);

            // check that the current word is still okay for the path
            int newPathType = pathType;
            if (sectionNo == 0) {
                // if the country section exceeds tho chars then abort
                if (current.length() > 2) {
                    newPathType = INVALID_PATH;
                }
            } else {
                if (pathType == SUBURB_PATH) {
                    if (sectionNo == 2) {
                        if (Character.isDigit(ch)) {
                            // switch to postcode path as digits are not permitted in the 3rd section
                            newPathType = POSTCODE_PATH;
                        }
                    }
                }
            }
            pathType = newPathType;

            // if an invalid path is encountered break out
            return (pathType != INVALID_PATH);
        }


        /**
         * Extract the parameters from the path.  If known action names are provided this method will detect
         * the action name in accordance with the REST-plugin URLs:
         * This check assumes one of the following REST-plugin URLs:
         *   namespace/action/id/method
         *   namespace/action/id
         *   namespace/action
         *
         * Processing is performed from right to left (hence the wordStack)
         *  
         * @param actions
         * @return
         */
        public Map<String, String> extractParameters(Collection<String> actions) {
            boolean actionFound = false;

            List<Word> actionWords = new ArrayList<Word>(3);
            /*
            if (actions != null && !wordStack.isEmpty()) {

                int wordNo = 0;
                Word thisWord;
                while ((!actionFound) && (wordNo < 3) && !wordStack.isEmpty()) {
                    thisWord = wordStack.pop();

                    actionWords.add(thisWord);

                    for (String action : actions) {
                        if (action.equals(thisWord.getValue())) {
                            // this is an action invocation
                            actionFound = true;

                            // if used as a parameter, remove it
//                            if (sectionNo < PATH_TYPES[pathType].length) {
//                                params.remove(PATH_TYPES[pathType][sectionNo-1]);
//                            }
                            for (Word word : actionWords) {
                                if (word.isParam()) {
                                    params.remove(word.getParamName());
                                }
                            }

                            params.put("action", join(actionWords));
                            params.put("namespace", namespace.substring(0, thisWord.getIndex()));
                        }
                    }
                    wordNo++;
                }
            }   */

//            if (!actionFound) {
                if (action != null) {
                    params.put("action", action.toString());
                } else {
                    // use the action from the path type
                    if (lastSectionUsed >= 0) {
                        params.put("action", ACTIONS[pathType][lastSectionUsed]);
                    }
                }
                params.put("namespace", namespace.toString());
//            }

            // use the path before the extension (and query string if present)
            String pathOnly = StringUtils.substringBeforeLast(path, "?");
            String pathMinusEx = StringUtils.substringBeforeLast(pathOnly, ".");
            if (pathOnly.length() > pathMinusEx.length()) {
                params.put("extension", StringUtils.substringAfter(pathOnly, "."));
            }

            return params;
        }

        /**
         * Join the words in a stack back into a path (in order popped off)
         *
         * @param words
         * @return
         */
        private String join(List<Word> words) {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (int index = words.size()-1; index >= 0; index--) {
                if (!first) {
                    result.append("/");
                } else {
                    first = false;
                }
                result.append(words.get(index).getValue());                
            }
            return result.toString();
        }
    }

    /**
     * Process the path to extract the individual address path components.
     *
     * @param path
     * @return map of components, or null if it's not an address path
     */
    public static Map<String, String> process(String path) {
        return process(path, new LinkedList<String>());
    }

    /**
     * Process the path to extract the individual address path components.
     *
     * @param path
     * @return map of components, or null if it's not an address path
     */
    public static Map<String, String> process(String path, Collection<String> actions) {

        // use the path before the extension (and query string if present)
        String pathOnly = StringUtils.substringBeforeLast(path, "?");
        String pathMinusEx = StringUtils.substringBeforeLast(pathOnly, ".");
        char[] charArray = pathMinusEx.toCharArray();

        boolean firstChar = true;
        boolean abort = false;
        MatcherState state = new MatcherState(path, actions);

        for (char ch : charArray) {
            if (ch == PATH_SEPARATOR) {
                if (firstChar) {
                    continue;  // leading slash
                }
                // add this word
                state.completeSection();
            } else {
                // append this character
                if (!state.append(ch)) {
                    abort = true;
                    break;
                }
            }
            if (firstChar) {
                firstChar = false;
            }                       
        }

        if (!abort) {
            // finish final incomplete section
            state.completeSection();

            // extract the parameters
            return state.extractParameters(actions);
        } else {
            return null;
        }
    }

}
