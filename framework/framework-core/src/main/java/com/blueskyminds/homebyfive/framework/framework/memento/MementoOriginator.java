package com.blueskyminds.homebyfive.framework.framework.memento;

/**
 * The MementoOriginator is associated with the CaretakerDomainObject, following the GoF memento design pattern
 *
 * The MementoOriginator can have any behaviour but must meet the following constraints:
 *    - it must have a no-parameter constructor so it can be instantiated by reflection
 *    - it must allow its state to be stored in a XMLMemento
 *    - it must allow its state to be restored from a XMLMemento
 *
 * The Caretaker allows any object implementing MementoOriginator to be persisted and restored using the XMLMemento
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface MementoOriginator {

    XMLMemento getMemento();

    void setMemento(XMLMemento memento);

}
