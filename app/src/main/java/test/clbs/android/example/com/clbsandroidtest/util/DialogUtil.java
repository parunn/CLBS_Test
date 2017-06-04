package test.clbs.android.example.com.clbsandroidtest.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import test.clbs.android.example.com.clbsandroidtest.R;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class DialogUtil {
    public static DialogUtil dialogUtil;
    private ProgressDialog dialog;
    private Runnable dialogRunnable;
    private Handler handler;

    public static DialogUtil getInstance() {
        if(dialogUtil == null)
            dialogUtil = new DialogUtil();

        return dialogUtil;
    }

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(context.getString(R.string.clbs_error));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(context.getString(R.string.clbs_ok), null);

        alertDialog.create().show();
    }

    public void showErrorDialog(Context context, String message, final DialogCallback callback) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(context.getString(R.string.clbs_error));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(context.getString(R.string.clbs_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onOKClick();
            }
        });

        alertDialog.create().show();
    }

    public void showSuccessDialog(Context context, String header, String message, final DialogCallback callback) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(context.getString(R.string.clbs_error));
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(context.getString(R.string.clbs_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onOKClick();
            }
        });

        alertDialog.create().show();
    }

    public void showLoadingDialog(final Context context) {
        try {

            if ((this.dialog == null || !this.dialog.isShowing()) && context != null)
                this.dialog = ProgressDialog.show(context, null, "Loading", true, false);

            this.dialogRunnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        if ((dialog != null) && dialog.isShowing() && context != null)
                            dialog.cancel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            this.handler = new Handler();
            this.handler.postDelayed(this.dialogRunnable, 180000);
        } catch (Exception e){}
    }

    public void closeLoadingDialog() {
        try {
            if (this.dialog != null && this.dialog.isShowing()) {
                this.dialog.dismiss();

                if(this.dialogRunnable != null && this.handler != null)
                    this.handler.removeCallbacks(this.dialogRunnable);
            }

            this.dialog = null;
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
    }

    public void showLoadingPercentageDialog(final Context context) {

        try {
                final int totalProgressTime = 100;
                ProgressDialog mProgressDialog;
                // instantiate it within the onCreate method
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("A message");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        } catch (Exception e){}
    }
}
