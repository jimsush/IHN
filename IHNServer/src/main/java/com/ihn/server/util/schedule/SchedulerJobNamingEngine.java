package com.ihn.server.util.schedule;

import java.util.concurrent.atomic.AtomicLong;

public class SchedulerJobNamingEngine {

    private static final String DEFAULT_SCHEDULER_JOB = "DEFAULT_SCHEDULER_JOB";

    private static AtomicLong count = new AtomicLong(0);

    public static String createSchedulerJobName() {
        return DEFAULT_SCHEDULER_JOB + count.getAndIncrement();
    }

}
