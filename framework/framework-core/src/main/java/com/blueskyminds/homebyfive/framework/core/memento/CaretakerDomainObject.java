package com.blueskyminds.homebyfive.framework.core.memento;

import javax.persistence.MappedSuperclass;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;
import com.blueskyminds.homebyfive.framework.core.tools.ReflectionTools;
import com.blueskyminds.homebyfive.framework.core.tools.ReflectionException;

/**
 * The CaretakerDomainObject is a DomainObject with three parts:
 *    - a persistent originator, extending this Class
 *    - an originator whose state can be stored and recovered from a Menento
 *    - a serializable XMLMemento that stores the state of the originator
 *
 * When persisted, a CaretakerDomainObject will include:
 *    a string property that contains the serialized memento; and
 *    a string property that contains the classname of the originator
 *
 * Whent this domain object is restored from persistence the memento and originator properties are used to
 *  create a new instance of the originator and restore its state
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@MappedSuperclass
public class CaretakerDomainObject extends AbstractDomainObject {

    private static final Log LOG = LogFactory.getLog(CaretakerDomainObject.class);

    private XMLMemento memento;
    private String originatorClassName;
    private MementoOriginator originator;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new Caretaker for the MementoOriginator and XMLMemento */
    protected CaretakerDomainObject(MementoOriginator originator, XMLMemento memento) {
        this.memento = memento;
        this.originator = originator;
        init();
    }

    /** Default constructor for ORM */
    public CaretakerDomainObject() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the CaretakerDomainObject with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Get the memento as a string
     *
     * Note this is limited to a max of 4k at the moment
     **/
    @Basic
    @Column(name="Memento", length = 4096)
    public String getSerializedMemento() {
        if (memento != null) {
            return XMLMementoFactory.serialize(memento);
        } else {
            return null;
        }
    }

    /** Restore the memento from a string */
    protected void setSerializedMemento(String str) {
        this.memento = XMLMementoFactory.createMemento(str);
        // restore the originator's state, if possible
        restoreOriginatorState();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the memento for the originator */
    @Transient
    public XMLMemento getMemento() {
        return memento;
    }

    public void setMemento(XMLMemento memento) {
        this.memento = memento;
    }

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    /** Get the name of the class that implements the Taskable taskable for this task */
    @Basic
    @Column(name="MementoOriginator", length = 1024)
    protected String getOriginatorName() {
        if (originator != null) {
            return originator.getClass().getName();
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Calling this set method will create an instance of the originator with the reflectively (if it
     *  hasn't already been instantiated).  Note that the state of the originator has not been set yet
     *
     * @param className
     */
    protected void setOriginatorName(String className) {
        this.originatorClassName = className;
        createImpl();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the MementoOriginator implementation */
    @Transient
    public MementoOriginator getOriginator() {
        return originator;
    }

    public void setOriginator(MementoOriginator originator) {
        this.originator = originator;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Attempt to create an instance of the originator and restore its state from the memento.
     * If the originator is already set it will not be changed
     * If the originator's class is the same as this class then a new instance isn't created - it's
     *   assumed that the originator and this caretaker are the same instance */
    private void createImpl() {
        if (originatorClassName != null) {
            if (originator == null) {
                try {
                    // NOTE: if the originator is this class then don't create another instance
                    // (the Caretake is permitted to be the Originator as well)
                    if (!getClass().getName().equals(originatorClassName)) {
                        originator = (MementoOriginator) ReflectionTools.createInstanceOf(originatorClassName);
                    }

                    // restore the originator's state, if possible
                    restoreOriginatorState();
                } catch (ReflectionException e) {
                    LOG.error("Couldn't restore Originator ("+originatorClassName+")", e);
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Restore the state of the originator from the memento if both the originator and memento have
     *  been set */
    private void restoreOriginatorState() {
        if ((originator != null) && (memento != null)) {
            originator.setMemento(memento);
        }
    }

    // ------------------------------------------------------------------------------------------------------

}
