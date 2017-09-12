package com.zp.easilyshot.easilyshot.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zp.easilyshot.easilyshot.R;


public class AlertDialogUtil {
    public static boolean alertIsShow = false;//alert是否展示了

    /**
     * 单个按钮的警告框
     *
     * @param context   上下文对象
     * @param title     标题
     * @param message   提示内容
     * @param buttomMsg 按钮的文案
     * @param runnableb 响应事件
     */
    public static AlertDialog showSingleAlert(Context context, String title, String message, String buttomMsg, Runnable runnableb) {
        return showThreeAlert(context, title, message, buttomMsg, runnableb, null, null, null, null);
    }

    /**
     * 提示框
     *
     * @param context         上下文对象
     * @param title           设置标题，null或者为空的时候不显示标题
     * @param message         提示语
     * @param fristMsg        第一个按钮的文案
     * @param fristRunnableb  第一个按钮的相应事件
     * @param secondMsg       第二个按钮的文案
     * @param secondRunnableb 第二个按钮的相应事件
     */
    public static AlertDialog showDoubleAlert(Context context, String title, String message, String fristMsg, final Runnable fristRunnableb, String secondMsg, final Runnable secondRunnableb) {
        return showThreeAlert(context, title, message, fristMsg, fristRunnableb, secondMsg, secondRunnableb, null, null);
    }

    /**
     * @param context         上下文对象
     * @param title           标题
     * @param message         提示内容
     * @param fristMsg        第一个按钮的文案
     * @param fristRunnableb  第一个按钮的相应事件
     * @param secondMsg       第二个按钮的文案
     * @param secondRunnableb 第二个按钮的相应事件
     * @param threeMsg        第三个按钮的文案
     * @param threeRunnableb  第三个按钮的相应事件
     */
    public static AlertDialog showThreeAlert(Context context, String title, String message, String fristMsg, final Runnable fristRunnableb, String secondMsg, final Runnable secondRunnableb, String threeMsg, final Runnable threeRunnableb) {
        if (((Activity) context).isFinishing())
            return null;
        alertIsShow = true;
        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.NommalDialog).show();
        final Window window = alertDialog.getWindow();

        window.setContentView(R.layout.my_alert_dialog_layout);
        //        setWindowStart(window);
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置宽度
        WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (280 * context.getResources().getDisplayMetrics().density + 0.5f);
        window.setAttributes(p);     //设置生效

        alertDialog.setCancelable(false);
        // 设置标题
        if (!TextUtils.isEmpty(title)) {
            window.findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
            ((TextView) window.findViewById(R.id.tip_title)).setText(title);
        }

        // 设置消息体
        ((TextView) window.findViewById(R.id.tip_msg)).setText(message);

        // 设置第一个按钮
        if (!TextUtils.isEmpty(fristMsg)) {
            Button btFrist = (Button) window.findViewById(R.id.bt_frist);
            btFrist.setText(fristMsg);
            btFrist.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    //                    setWindowStart(window);
                    alertIsShow = false;
                    if (fristRunnableb != null)
                        fristRunnableb.run();
                }
            });
            btFrist.setVisibility(View.VISIBLE);
        }

        // 设置第二个按钮
        if (!TextUtils.isEmpty(secondMsg)) {
            Button btSecond = (Button) window.findViewById(R.id.bt_second);
            btSecond.setText(secondMsg);
            btSecond.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    //                    setWindowStart(window);
                    alertIsShow = false;
                    if (secondRunnableb != null)
                        secondRunnableb.run();

                }
            });
            window.findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
            btSecond.setVisibility(View.VISIBLE);
        }

        // 设置第三个按钮
        if (!TextUtils.isEmpty(threeMsg)) {
            Button btThree = (Button) window.findViewById(R.id.bt_three);
            btThree.setText(threeMsg);
            btThree.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    //                    setWindowStart(window);
                    alertIsShow = false;
                    if (threeRunnableb != null)
                        threeRunnableb.run();
                }
            });
            window.findViewById(R.id.middle_line_tow).setVisibility(View.VISIBLE);
            btThree.setVisibility(View.VISIBLE);
        }
        return alertDialog;
    }

    /**
     * 不打扰沉浸式模式
     *
     * @param window
     */
    //    private static void setWindowStart(Window window) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
    //            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //        int layout = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    //        window.getDecorView().setSystemUiVisibility(layout);
    //    }
}
