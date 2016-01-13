package me.tomalka.usosportfolio;

import android.app.Application;
import android.content.Context;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by Ritave on 2016-01-13.
 */
public class UsosApplication extends Application {
    private static Scheduler scheduler;

    public static UsosApplication get(Context context) {
        return (UsosApplication)context.getApplicationContext();
    }

    public Scheduler getScheduler() {
        if (scheduler == null)
            return Schedulers.io();
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
