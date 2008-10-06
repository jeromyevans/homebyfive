package com.blueskyminds.homebyfive.business.party;

import com.blueskyminds.homebyfive.framework.core.DomainObject;
import com.blueskyminds.homebyfive.framework.core.MergeUnsupportedException;
import com.blueskyminds.homebyfive.framework.core.tools.ArrayTools;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import javax.persistence.*;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * A individual is a type of party.  eg. a Person
 *
 * Date Started: 29/04/2006
 *
 * History:
 *
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 **/
@Entity
@DiscriminatorValue("Individual")
public class Individual extends Party {

    /**
     * The title given to this person
     */
    private Title title;

    /**
     * The persons first name
     */
    private String firstName;

    /**
     * The persons middle name
     */
    private String middleName;

    /**
     * The persons last name name/surname/christian name
     */
    private String lastName;

    /**
     * The person's initials
     */
    private String initials;

    /**
     * The person's middle initial
     */
    private String middleInitial;

    /**
     * Flag tracking whether the fullname was automatically generated or manually assigned
     */
    private boolean fullNameAssigned;

    /**
     * The fullname of the individual, which may be automatically generated or overrdden
     */
    private String fullName;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new person, initially setting their first and last name */
    public Individual(String firstName, String lastName) {
        super(PartyTypes.Individual);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** Create a new person, initially setting the full name and decomposing the name components
     * automatically */
    public Individual(String fullName) {
        super(PartyTypes.Individual);
        setFullName(fullName);
        decomposeFullName();
    }

    /** Default constructor for ORM */
    public Individual() {
        super(PartyTypes.Individual);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @Basic
    @Column(name="Title")
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    @Basic
    @Column(name="FirstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name="MiddleName")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Basic
    @Column(name="LastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name="Initials")
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Basic
    @Column(name="MiddleInitial")
    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    @Basic
    @Column(name="FullNameAssigned")
    protected boolean isFullNameAssigned() {
        return fullNameAssigned;
    }

    protected void setFullNameAssigned(boolean fullNameAssigned) {
        this.fullNameAssigned = fullNameAssigned;
    }

    @Basic
    @Column(name="FullName")
    protected String getFullNameValue() {
        return fullName;
    }

    protected void setFullNameValue(String fullName) {
        this.fullName = fullName;
    }

    /**
     * todo: this may be locale dependent
     * @return automatically generated full name for an Individual
     */
    private String generateFullName() {
        String result = "";
        if (StringUtils.isNotBlank(firstName)) {
            if (StringUtils.isNotBlank(lastName)) {
                result = firstName + " " + lastName;
            } else {
                result = firstName;
            }
        } else {
            if (StringUtils.isNotBlank(lastName)) {
                result = lastName;
            }
        }
        return result;
    }

    /**
     * Get the full displayable name of the individual.  This value will be automatically generated if it's
     *  never been directly assigned
     *
     * @return full name for the individual
     */
    @Transient
    public String getFullName() {
        if (isFullNameAssigned()) {
            return fullName;
        } else {
            return generateFullName();
        }
    }

    /** Set the full name to the specified value.  This will override the automatically generated full name
     *  if it differs from the automatic value. Null values are also considered.
     *
     * @param fullName for the Individual
     */
    protected void setFullName(String fullName) {
        String defaultFullName = generateFullName();

        if (((fullName != null) && (!fullName.equals(defaultFullName))) ||
            ((fullName == null) && (defaultFullName != null))) {
            this.fullName = fullName;
            this.setFullNameAssigned(true);
        } else {
            // its the same value so clear the flag
            this.fullName = fullName;
            this.setFullNameAssigned(false);
        }
    }

    // ------------------------------------------------------------------------------------------------------


    /**
    * Attempts to decompose an individual's full name into its parts.
     *
    * There are two different algorithms applied:
    *   if the full name contains a comma, the last name is assumed to preceed the first and middle names; otherwise
    *   the first word is the first name, followed by zero or more middle names, followed by the last name.
    *
    * Hyphen's and apostraphies are assumed to be part of the same word
    *
    *
    * Todo: the implementation will be locale dependent
    *
    * todo: a better implementation would recognise names and use the pattern matcher
    */
    private void decomposeFullName() {
        String[] words;
        List<String> namesInOrder = new LinkedList<String>();

        if (fullName.contains(",")) {
            // if there's a comma we assume the last name preceeds the comma
            String[] parts = StringUtils.split(fullName, ",", 2);

            if (parts.length > 0) {
                lastName = WordUtils.capitalize(StringUtils.strip(parts[0]));
            }
            if (parts.length > 1) {
                words = StringUtils.split(parts[1]);

                firstName = WordUtils.capitalize(StringUtils.strip(words[0]));
                namesInOrder.add(firstName);

                if (words.length > 2) {
                    middleName = WordUtils.capitalize(StringUtils.join(ArrayTools.subArray(String.class, 1, words.length-1, words), " "));
                    namesInOrder.add(middleName);
                }
                namesInOrder.add(lastName);
            }
        } else {
            words = StringUtils.split(fullName);

            if (words.length > 1) {
                firstName = WordUtils.capitalize(StringUtils.strip(words[0]));
                namesInOrder.add(firstName);
                if (words.length > 2) {
                    middleName = WordUtils.capitalize(StringUtils.join(ArrayTools.subArray(String.class, 1, words.length-2, words), " "));
                    namesInOrder.add(middleName);
                }

                lastName = WordUtils.capitalize(StringUtils.strip(words[words.length-1]));
                namesInOrder.add(lastName);
            }
        }

        initials = generateInitials(null, namesInOrder);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Generates initials from a list of phrases
     *
     * The initialise are generated from the first letter of each word, in uppercase
     * If a phrase contains words separated by whitespace or hyphen each word will contribute a letter
     *
     * @param separator string to separate the initials. Null means no separator
     * @param phrases
     * @return
     */
    private String generateInitials(String separator, String... phrases) {
        StringBuffer initials = new StringBuffer();
        boolean first = true;
        for (String phrase : phrases) {
            if (StringUtils.isNotBlank(phrase)) {
                if (!first) {
                    if (separator != null) {
                        initials.append(separator);
                    }
                } else {
                    first =  false;
                }
                String[] words = StringUtils.split(phrase, " -");
                if (words.length > 1) {
                    initials.append(generateInitials(separator, words));
                } else {
                    initials.append(StringUtils.upperCase(Character.toString(phrase.charAt(0))));
                }
            }
        }
        return initials.toString();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Generates initials from a list of phrases
     *
     * The initialise are generated from the first letter of each word, in uppercase
     * If a phrase contains words separated by whitespace or hyphen each word will contribute a letter
     *
     * @param separator string to separate the initials. Null means no separator
     * @param phrases
     * @return
     */
    private String generateInitials(String separator, List<String> phrases) {
        String[] array = new String[phrases.size()];        
        return generateInitials(separator, phrases.toArray(array));
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    @Override
    public String getIdentityName() {
        return super.getIdentityName()+" ("+getFullName()+")";
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" Type:"+getType());
        out.println("   First name: "+getFirstName());
        out.println("   Middle name: "+getMiddleName());
        out.println("   Last name: "+getLastName());
        out.println("   Middle Initial: "+getMiddleInitial());
        out.println("   Initials: "+getInitials());
        super.print(out);
    }

    /**
     * Merge the properties of another Individual into this Individual.
     * <p/>
     *
     * @param other the object to extract properties from into this object
     * @throws com.blueskyminds.homebyfive.framework.core.MergeUnsupportedException
     *          when this domain object hasn't implemented the operation
     */
    public <T extends DomainObject> void mergeWith(T other) throws MergeUnsupportedException {
        if (Individual.class.isAssignableFrom(other.getClass())) {
            super.mergeWith(other);
        } else {
            throw new MergeUnsupportedException(this, other);
        }
    }

    
}
