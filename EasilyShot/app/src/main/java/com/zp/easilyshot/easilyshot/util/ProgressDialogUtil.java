package com.zp.easilyshot.easilyshot.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.zp.easilyshot.easilyshot.R;


//import com.ant.liao.GifView;

public class ProgressDialogUtil {
    private Dialog waiteDialog;
    private Context context;
    private AlertDialog.Builder builder;
    public TextView messageText;

    public ProgressDialogUtil(Context context) {
        this.context = context;
        if (waiteDialog == null) {
            waiteDialog = new Dialog(context, R.style.NommalDialog);
            builder = new AlertDialog.Builder(context);
            builder.setOnKeyListener(new OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.i("ProgressDialogUtil", "------------------点击了返回键ProgressDialogUtil");
                    }
                    return false;
                }
            });
            builder.setCancelable(false);
        }
    }

    public void cancelWaiteDialog() {
        if (waiteDialog != null && waiteDialog.isShowing() && !((Activity) context).isFinishing()) {
            try {
                waiteDialog.dismiss();
            } catch (Exception e) {
                Log.e("ProgressDialogUtil", "撤销提示框失败！" + e.getMessage());
            }
        }
    }

    OnKeyListener keyListener = new OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            //cancelWaiteDialog();
            //activity.finish();
            return false;
        }
    };

    public void showWaiteDialog(String message) {
        if (context == null)
            return;

        waiteDialog.setContentView(R.layout.my_progress_view);
        waiteDialog.setCanceledOnTouchOutside(false);
        waiteDialog.setOnKeyListener(keyListener);
        waiteDialog.setCancelable(false);
        // GifView gf1 = (GifView) waiteDialog.findViewById(R.id.gv_loading);
        // gf1.setGifImage(R.drawable.loading);
        messageText = (TextView) waiteDialog.findViewById(R.id.message);
        messageText.setText(message);

        if (context != null && !waiteDialog.isShowing() && !((Activity) context).isFinishing()) {
            waiteDialog.show();
        }

    }

    public void showWaiteDialog() {
        showWaiteDialog("");
    }

    public void showAlertDialog(String message, final Runnable runnable) {
        builder.setMessage(message).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                runnable.run();
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showAlertDialog(Runnable runnable) {
        showAlertDialog("是否放弃本次编辑?", runnable);
    }

    /**
     * 改变进度的提示语
     */
    public void changeTextValue(String message) {
        messageText.setText(message);
    }
}
