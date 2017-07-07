package com.cp.user.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cp.user.R;

/**
 * Created by yi on 07/07/2017.
 */

public class DialogUtil {

    public static void alertDialog(Context context, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);

        if (positive != null)
            builder.setPositiveButton(context.getString(R.string.alert_button_ok), positive);

        if (positive != null)
            builder.setNegativeButton(context.getString(R.string.alert_button_cancel), negative);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }


}
