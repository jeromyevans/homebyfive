package com.blueskyminds.homebyfive.framework.framework.persistence.spooler;

import java.util.List;

/**
 * A class that can process entries of type T output by a Spooler
 *
 * Date Started: 15/02/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface SpoolerTask<T> {

    void process(List<T> entities) throws SpoolerException;
}
