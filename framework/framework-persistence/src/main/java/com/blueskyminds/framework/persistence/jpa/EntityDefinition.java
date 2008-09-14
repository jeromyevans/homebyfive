package com.blueskyminds.framework.persistence.jpa;

import com.blueskyminds.framework.tools.ResourceLocator;

import java.util.List;
import java.util.LinkedList;
import java.net.URL;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * Reads configuration files defining JPA/EJB3 Entities to be used by a Provider
 * </p>
 * Date Started: 26/05/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class EntityDefinition {

    private static final Log LOG = LogFactory.getLog(EntityDefinition.class);
    private String[] definitionFiles;
    private static final String ENTITY_LIST_TAG = "entities";
    private static final String ENTITY_TAG = "entities/mapping";

    public EntityDefinition(String[] definitionFiles) {
        this.definitionFiles = definitionFiles;
        init();
    }

    public EntityDefinition(String definitionFile) {
        this.definitionFiles = new String[1];
        this.definitionFiles[0] = definitionFile;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the EntityDefinition with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Read an entity definition file and return the list of class names defined in it
     **/
    private List<String> readEntityDefinition(URL defnFile) {
        EntityBeanList entities;
        List<String> classNames = new LinkedList<String>();
         // find all of the entity definitions on the classpath
        Digester digester = new Digester();
        digester.setValidating(false);
        // create an instance of an enumeration definition when the ENUMERATION_TAG is encountered
        digester.addObjectCreate(ENTITY_LIST_TAG, EntityBeanList.class.getName());
        // when an attribute is encountered, set the property with the same name
        digester.addSetProperties(ENTITY_TAG);
        // create an instance of an enumeration definition when the ENUMERATION_TAG is encountered
        digester.addObjectCreate(ENTITY_TAG, EntityBean.class.getName());
        // when an attribute is encountered, set the property with the same name
        digester.addSetProperties(ENTITY_TAG);
        // call defineItem when the next pattern is encountered to save the item
        digester.addSetNext(ENTITY_TAG, "addEntity", EntityBean.class.getName());

        try {
            entities = (EntityBeanList) digester.parse(defnFile.openStream());
            if (entities != null) {
                // transfer from the list of entities beansinto a simple list of class names
                for (EntityBean bean : entities.getEntityBeans()) {
                    if (!classNames.contains(bean.getClassName())) {
                        classNames.add(bean.getClassName());
                    }
                }
            }
        }
        catch (SAXException e) {
           LOG.error("Error parsing entity definition file "+defnFile);
        }
        catch (IOException e) {
            LOG.error("Error reading entity definition file "+defnFile);
        }

        return classNames;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Reads all of the entity definitions found on the classpath and loads them into the hibernate
     * configuration programmatically
     *
     * @return array of entity class names
     */
    public String[] loadEntityDefinitions() {
        List<String> consolidatedEntityNames = new LinkedList<String>();
        List<String> classNames;
        String[] listOfEntities;

        if (definitionFiles != null) {
            for (String name : definitionFiles) {
                URL[] entityDefinitions = ResourceLocator.locateResources(name);

                if (entityDefinitions.length > 0) {
                    // found one or more defintions - read them
                    for (URL url : entityDefinitions) {
                        LOG.info("Found entity definitions: "+url);
                        classNames = readEntityDefinition(url);

                        if (classNames != null) {
                            // add the classname to the consolidated list (if not already listed)
                            for (String className : classNames) {
                                if (!consolidatedEntityNames.contains(className)) {
                                    consolidatedEntityNames.add(className);
                                } else {
                                    LOG.warn("An entity with the classname '"+className+"' was defined multiple times (ignoring last)");
                                }
                            }
                        }
                    }
                }
            }
        }

        listOfEntities = new String[consolidatedEntityNames.size()];
        if (listOfEntities.length > 0) {
            return consolidatedEntityNames.toArray(listOfEntities);
        } else {
            return listOfEntities; // empty array
        }
    }
}
