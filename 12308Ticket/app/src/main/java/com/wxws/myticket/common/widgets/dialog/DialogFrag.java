package com.wxws.myticket.common.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.wxws.myticket.R;
import com.wxws.myticket.common.interfaces.DialogCallBackListener;
import com.wxws.myticket.common.utils.StringUtils;

/**
 * 对话框使用方法：<br/>
 * <p/>
 * <pre>
 * 1、在需要弹出对话框的类实现DialogCallBackListerner接口
 * 2、在需要弹出对话框的地方
 *  DialogFrag dialog = DialogFrag.newInstance("lalala", R.layout.customdialog_ok);
 *  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
 *  dialog.show(ft, TAG);
 * </pre>
 *
 * @author sheny
 */
public class DialogFrag extends DialogFragment implements OnClickListener {
    private int layoutId;

    private SparseArray<String> textMap;
    private DialogCallBackListener callback;
    private OnDismissListener onDismiss;
    private EditText ett;
    private static final String DIALOG = "dialog";
    public static boolean show;
    public static boolean doDismiss = true;

    public static DialogFrag newInstance(Context context, int msg, int resid) {
        return newInstance(context, context.getString(msg), resid);
    }

    public static DialogFrag newInstance(Context context, String msg, int resid) {
        DialogFrag f = new DialogFrag();
        Bundle args = new Bundle();
        args.putString("msgStr", msg);
        args.putInt("layout", resid);
        f.setArguments(args);
        show = true;
        return f;
    }

    public static DialogFrag newInstance(Context context, String title, String msg, int resid) {
        DialogFrag f = new DialogFrag();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msgStr", msg);
        args.putInt("layout", resid);
        f.setArguments(args);
        show = true;
        return f;
    }

    public static DialogFrag newInstance(Context context, String title, String msg, int resid, boolean doDismiss) {
        DialogFrag f = new DialogFrag();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msgStr", msg);
        args.putInt("layout", resid);
        args.putBoolean("doDismiss", doDismiss);
        f.setArguments(args);
        return f;
    }

    public static DialogFrag newInstance(int resid) {
        DialogFrag f = new DialogFrag();
        Bundle args = new Bundle();
        args.putString("msgStr", "");
        args.putInt("layout", resid);
        f.setArguments(args);
        show = true;
        return f;
    }

    /**
     * 创建dialogFrag并显示
     *
     * @param context
     * @param msg      dialog上显示的文本内容id
     * @param resid    dialog的布局资源id
     * @param callback dialog的回调
     */
    public static void showDialog(Context context, int msg, int resid, DialogCallBackListener callback) {
        showDialog(context, context.getString(msg), resid, callback);
    }

    public static void showDialog(Context context, int title, int msg, int resid, DialogCallBackListener callback) {
        showDialog(context, context.getString(title), context.getString(msg), resid, callback);
    }

    /**
     * 创建dialogFrag并显示
     *
     * @param context
     * @param msg      dialog上显示的文本内容
     * @param resid    dialog的布局资源id
     * @param callback dialog的回调
     */
    public static void showDialog(Context context, String msg, int resid, DialogCallBackListener callback) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        if (((FragmentActivity) context).isFinishing()) {
            return;
        }
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        DialogFrag prev = (DialogFrag) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        DialogFrag newFrag = DialogFrag.newInstance(context, msg, resid);
        newFrag.setOnDialogCallBack(callback);
        ft.add(newFrag, DIALOG);
        ft.commitAllowingStateLoss();
        show = true;
    }

    public static void showDialog(Context context, String title, String msg, int resid, DialogCallBackListener callback) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        if (((FragmentActivity) context).isFinishing()) {
            return;
        }
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        DialogFrag prev = (DialogFrag) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        DialogFrag newFrag = DialogFrag.newInstance(context, title, msg, resid);
        newFrag.setOnDialogCallBack(callback);
        ft.add(newFrag, DIALOG);
        ft.commitAllowingStateLoss();
        show = true;
    }

    public static void showDialog(Context context, String title, String msg, int resid, boolean dismiss, DialogCallBackListener callback, OnDismissListener onDismiss) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        if (((FragmentActivity) context).isFinishing()) {
            return;
        }
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        DialogFrag prev = (DialogFrag) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        DialogFrag newFrag = DialogFrag.newInstance(context, title, msg, resid, dismiss);
        newFrag.setOnDialogCallBack(callback);
        newFrag.setOnDismissListener(onDismiss);
        ft.add(newFrag, DIALOG);
        ft.commitAllowingStateLoss();

    }

    public static void removeDialog(Context context) {
        if (((FragmentActivity) context).isFinishing()) {
            return;
        }
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commitAllowingStateLoss();
    }

    public static DialogFrag getDialogFrag(Context context, int layout, DialogCallBackListener callback) {
        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        DialogFrag prev = (DialogFrag) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(DIALOG);
        if (prev != null && prev.getLayoutId() != layout) {
            ft.remove(prev);
            prev = DialogFrag.newInstance(layout);
        } else if (prev == null) {
            prev = DialogFrag.newInstance(layout);
        }
        prev.setOnDialogCallBack(callback);
        ft.add(prev, DIALOG);
        ft.commitAllowingStateLoss();
        return prev;
    }

    public DialogFrag setText(int resid, String str) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            View view = dialog.findViewById(resid);
            if (view instanceof TextView) {
                ((TextView) view).setText(str);
            }
        } else {
            if (textMap == null) {
                textMap = new SparseArray<String>();

            }
            textMap.put(resid, str);
        }
        return this;
    }

    public DialogFrag setText(Context context, int resid, int strResid) {
        return setText(resid, context.getString(strResid));
    }


    public void setOnDialogCallBack(DialogCallBackListener callback) {
        this.callback = callback;
    }

    public void setOnDismissListener(OnDismissListener onDismiss) {
        this.onDismiss = onDismiss;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String msgStr = getArguments().getString("msgStr");
        String title = getArguments().getString("title");
        layoutId = getArguments().getInt("layout");
        doDismiss = getArguments().getBoolean("doDismiss", true);

        Dialog dialog = new Dialog(getActivity(), com.wxws.myticket.R.style.dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(layoutId, null, false);

        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        if (tvTitle != null && !StringUtils.isNullOrEmpty(title)) {
            tvTitle.setText(title);
        }

        TextView tv = (TextView) v.findViewById(R.id.dialog_message);
        if (tv != null) {
            tv.setText(msgStr);
        }

        // 2、注册三个button的按键监听listener
        Button nagtiveBtn = (Button) v.findViewById(R.id.dialog_btn_cancel);
        if (nagtiveBtn != null) {
            nagtiveBtn.setOnClickListener(this);
        }

        Button positiveBtn = (Button) v.findViewById(R.id.dialog_btn_ok);
        if (positiveBtn != null) {
            positiveBtn.setOnClickListener(this);
        }
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        if (textMap != null) {
            for (int i = 0, j = textMap.size(); i < j; i++) {
                View view = dialog.findViewById(textMap.keyAt(i));
                if (view instanceof TextView) {
                    ((TextView) view).setText(textMap.valueAt(i));
                }
            }
        }
        return dialog;
    }

    public int getLayoutId() {
        return layoutId;
    }

    @Override
    // 在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        // 可以设置dialog的显示风格,theme为0，表示由系统选择合适的theme。
        int style = R.style.dialog, theme = 0;
        setStyle(DialogFragment.STYLE_NORMAL, theme); // 直接使用系统默认dialog 风格
    }

    @Override
    // 仅用于状态跟踪
    public void onCancel(DialogInterface dialog) {
        if (callback != null) {
            callback.doNegativeClick();
        }
        super.onCancel(dialog);
    }

    @Override
    // 仅用户状态跟踪
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismiss != null) {
            onDismiss.onDismiss(getDialog());
        }
        show = false;
    }

    @Override
    // Button按键触发的回调函数
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.dialog_btn_cancel:
                    if (callback != null) {
                        callback.doNegativeClick();
                    }

                    dismiss();


                    break;
                case R.id.dialog_btn_ok:
                    if (callback != null) {
                        if (ett != null) {
                            callback.doPositiveClick(ett.getText().toString().trim());
                        } else {
                            callback.doPositiveClick();
                        }
                    }
                    if (doDismiss) {
                        dismiss(); // 关闭对话框，并触发onDismiss()回调函数。
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
        }
    }
}
