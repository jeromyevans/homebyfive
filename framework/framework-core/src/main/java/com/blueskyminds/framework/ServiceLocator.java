package com.blueskyminds.framework;

import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.tools.PropertiesContext;
import com.blueskyminds.framework.tools.ReflectionTools;
import com.blueskyminds.framework.tools.ReflectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * The service locator is a well-known interface to access services.
 *
 * Interface Seggregation Priciple
 *  Clients should not be forced to depend upon interfaces that they do not use
 *
 * This is a Singleton Registry.
 *
 * Important: The ServiceLocator should be configured by creating an instance with the appropriate parameters
 * and calling the static load method.  The ServiceLocator has a soleIsntance, but it is not statically
 * instantiated.
 *
 * Automatic instantiation occurs if a service is accessed before calling load.  In this case, the
 * registery is loaded for default attributues.
 *
 * The ServiceLocator loads its configuration from servicelocator.properties
 *
 * EXTENSIONS TO THE SERVICE LOCATOR:
 * The ServiceLocator is designed to be extended to provide additional services.
 *  The ServiceLocator tracks a SoleInstance for itself (the parent class) and a Map of sole
 *   instances of any of the extensions that have been accessed.
 *   When a service is requested, the ServiceLocator will return the appropriate instance
 *
 * IMPLEMENTING AN EXTENSION:
 *   1. override the template method templateInit to perform any initialisation
 *   2. call instance(className) when accessing the service locator's sole instance.  By specifying className
 *  the service locator knows that an extension is being accessed instead of the root instance.  The
 *  service locator root instance will instantiate the extension if required.
 *
 * <code>
 * class MyServiceLocator extends ServiceLocator {
 *   private Service aService;
 *
 *   protected void templateInit() {
 *      aService = new Service();
 *   }
 *
 *   public static Service service() {
 *       return instance(MyServiceLocator.class).aService;
 *   }
 * }
 * </code>
 *
 * This strategy means the sole instances of the services are instantiated only a single time even if there
 *   are multiple types of ServiceLocators.  The services of the superclass are accessed directly and not
 *   overwritten when an extended class is instantiated
 *
 * Date Started: 5/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Deprecated
public class ServiceLocator {

    private static Log LOG = LogFactory.getLog(ServiceLocator.class);

    /** The file that contains the configuration of the service locator */
    private static final String PROPERTIES_FILE = "servicelocator.properties";

    /** The property that names the persistence layer gateway implementation */
    private static final String PERSISTENCE_SERVICE = "persistence.service";

    /** The sole instance of the primary service locator */
    private static ServiceLocator soleInstance = null;

    /** Extended instaces of the service locator */
    private static Map<Class<? extends ServiceLocator>, ServiceLocator> extendedInstances;

    /** Instance of the persistence service */
    private PersistenceService persistenceService;

    private PropertiesContext propertiesContext;

    // ------------------------------------------------------------------------------------------------------

    /**
     * The private constructor is used to create the FIRST OF CLASS ServiceLocator instance.
     *   It loads the properties for the root service locator if init is true
     */
    private ServiceLocator(boolean init) {
        if (init) {
            doInit();
        }
    }

     // ------------------------------------------------------------------------------------------------------

    /**
     * The default constructor for extensions to the ServiceLactor.  Constructors cannot have
     *  any parameters.  Override the templateInit method to perform initialisation
     */
    public ServiceLocator() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the service locator with default attributes.
     * Calls the init method that may be overrided by the subclass
     */
    private void doInit() {
        propertiesContext = new PropertiesContext(PROPERTIES_FILE);
        String gateway = propertiesContext.getProperty(PERSISTENCE_SERVICE);
        if (gateway != null) {
            persistenceService = (PersistenceService) newInstance(gateway);
        } else {
            LOG.error("Configuration is incomplete as '"+ PERSISTENCE_SERVICE +"' is not defined.  ServiceLocator could not be initialised.");
        }
        extendedInstances = new HashMap<Class<? extends ServiceLocator>, ServiceLocator>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Attempts to create a new instance of the class specified by name.  This is used to load
     * services into the service locator.
     *
     * Calls the default zero parameter constructor of the class
     *
     * @param className
     * @return the instance, or null if it failed
     */
    private static Object newInstance(String className) {
        Object instance = null;
        try {
            instance = ReflectionTools.createInstanceOf(className);
        } catch(ReflectionException e) {
            LOG.error("Attempted create an instance of a class with 'null' className");
        }
        return instance;

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determine if the sole instance has been initialised
     * @return true if it has been
     */
    private static boolean isInitialised() {
        return soleInstance != null;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Determine if the extension instance has been initialised
     * @return true if it has been
     */
    private static boolean isInitialised(Class<? extends ServiceLocator> clazz) {
        return extendedInstances.containsKey(clazz);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * If not initialised, forces initialisation of the root instance using default attributes
     */
    private static void forceInit() {
        if (!isInitialised()) {
            // initialsie the root service locator with default attributes
            load(new ServiceLocator(true));
        }
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * If not initialised, forces initialisation using default attributes
     */
    private static void forceInit(Class<? extends ServiceLocator> clazz) {
        ServiceLocator instance;
        // ensure the root instance is initialised
        forceInit();

        // check if the extended instance is initialised
        if (!isInitialised(clazz)) {
            // initialise the extension with default attributes
            instance = (ServiceLocator) newInstance(clazz.getName());
            if (instance != null) {
                // initialise the extension
                instance.templateInit();
                extendedInstances.put(clazz, instance);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /**
     * Configure the service locator.  If the service locator has already been initialized then this
     * method well fail as auto-initialization would have occured.
     *
     * @param arg
     * @return true if loaded, or false if already initialized (load not permitted)
     */
    public static boolean load(ServiceLocator arg) {
        if (!isInitialised()) {
            soleInstance = arg;
            return true;
        }
        else {
            // already initialized
            return false;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Default implementation of the template initialisation method.  Subclasses may override this method
     * to perform initialsiation.
     */
    protected void templateInit() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the service locator.  If it hasn't been loaded, initializes with default
     * attributes
     * @return soleInstance of the serviceLocator
     */
    protected static ServiceLocator instance() {
        forceInit();
        return soleInstance;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an Extended instance of the service locator, specify the desired type.
     *  If it hasn't been loaded, initializes the extended instance with default attributes
     * @return soleInstance of the serviceLocator
     */
    protected static <S extends ServiceLocator> S instance(Class<S> clazz) {
        forceInit(clazz);
        return (S) extendedInstances.get(clazz);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of a PersistanceLayerGateway */
    public static PersistenceService persistenceService() {
        return instance().persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------
}
