package com.cp.user.bus;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by yi on 07/07/2017.
 */

public class ApplicationBus {

    private static Bus sBus;

    private ApplicationBus() {
        super();
    }

    public static Bus getBus() {

        if (sBus == null) {
            sBus = new Bus(ThreadEnforcer.ANY);
        }

        return sBus;
    }

    public static void registerContext(Context context) {
        getBus().register(context);
    }

    public static void registerContext(Fragment fragment) {
        getBus().register(fragment);
    }

    public static void unRegisterContext(Context context) {
        getBus().unregister(context);
    }

    public static void unRegisterContext(Fragment fragment) {
        getBus().unregister(fragment);
    }

}
