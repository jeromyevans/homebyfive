package com.blueskyminds.business.pricing;

import com.blueskyminds.homebyfive.framework.core.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Provides common methods for implementing a Product - particularly defining a JOIN table for the ORM
 *
 * Date Started: 28/05/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Product extends AbstractDomainObject {
}
