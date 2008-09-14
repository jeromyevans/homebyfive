package com.blueskyminds.framework.tasks.builder;

import java.util.concurrent.CompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.Queue;
import java.util.WeakHashMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.ref.WeakReference;

/**
 * Date Started: 1/08/2008
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class CompletionServiceBuilder<E> {

    private CompletionServiceThreadPoolBuilder threadPoolBuilder;

    public static Set<WeakReference> completionServices; 

    protected CompletionServiceBuilder(CompletionServiceThreadPoolBuilder completionServiceThreadPoolBuilder) {
        this.threadPoolBuilder = completionServiceThreadPoolBuilder;
    }

    public CompletionService<E> build() {
        ThreadPoolExecutor executor = threadPoolBuilder.build();
        CompletionService<E> ecs = new ExecutorCompletionService<E>(executor);
        return ecs;
    }

    public static Set<CompletionService> list() {
        Set<CompletionService> list = new HashSet<CompletionService>();
        for (WeakReference weakReference : completionServices) {
            CompletionService cs = (CompletionService) weakReference.get();
            if (cs != null) {
                list.add(cs);
            }
        }
        return list;
    }
}
