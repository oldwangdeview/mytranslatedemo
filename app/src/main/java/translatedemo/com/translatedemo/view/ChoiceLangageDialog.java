package translatedemo.com.translatedemo.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.ChoiceLangvageAdpater;
import translatedemo.com.translatedemo.adpater.DucationAdpater;

/**
 * Created by oldwang on 2019/1/18 0018.
 */

public class ChoiceLangageDialog extends Dialog {

    public ChoiceLangageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Params {
        private final List<ChoiceLangageDialog.BottomMenu> menuList = new ArrayList<>();
        private View.OnClickListener cancelListener;
        private String menuTitle;
        private String cancelText;
        private Context context;
    }

    public static class Builder {
        private boolean canCancel = true;
        private boolean shadow = true;
        private final ChoiceLangageDialog.Params p;
        private ChoiceLangvageAdpater madpater ;
        public Builder(Context context) {
            p = new ChoiceLangageDialog.Params();
            p.context = context;
        }

        public ChoiceLangageDialog.Builder setCanCancel(boolean canCancel) {
            this.canCancel = canCancel;
            return this;
        }

        public ChoiceLangageDialog.Builder setCanCancel(ChoiceLangvageAdpater madpater){
            this.madpater = madpater;
            return this;
        }

        public ChoiceLangageDialog.Builder setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        public ChoiceLangageDialog.Builder setTitle(String title) {
            this.p.menuTitle = title;
            return this;
        }

        public ChoiceLangageDialog.Builder addMenu(String text, View.OnClickListener listener) {
            ChoiceLangageDialog.BottomMenu bm = new ChoiceLangageDialog.BottomMenu(text, listener);
            this.p.menuList.add(bm);
            return this;
        }

        public ChoiceLangageDialog.Builder addMenu(int textId, View.OnClickListener listener) {
            return addMenu(p.context.getString(textId), listener);
        }

        public ChoiceLangageDialog.Builder setCancelListener(View.OnClickListener cancelListener) {
            p.cancelListener = cancelListener;
            return this;
        }

        public ChoiceLangageDialog.Builder setCancelText(int resId) {
            p.cancelText = p.context.getString(resId);
            return this;
        }

        public ChoiceLangageDialog.Builder setCancelText(String text) {
            p.cancelText = text;
            return this;
        }

        public ChoiceLangageDialog create() {
            final ChoiceLangageDialog dialog = new ChoiceLangageDialog(p.context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_bottom_menu1, null);
            MyGridView gridview = view.findViewById(R.id.mygridview);
            TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
//            LinearLayout layContainer = (LinearLayout) view.findViewById(R.id.lay_container);
            ViewGroup.LayoutParams lpItem = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.LayoutParams lpDivider = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            int dip1 = (int) (1 * p.context.getResources().getDisplayMetrics().density + 0.5f);
            int spacing = dip1 * 12;

            boolean hasTitle = !TextUtils.isEmpty(p.menuTitle);
            if (hasTitle) {


            }


//
            if(madpater!=null){
                gridview.setAdapter(madpater);
            }

            if (!TextUtils.isEmpty(p.cancelText)) {
                btnCancel.setText(p.cancelText);
            }

            if (p.cancelListener != null) {
                btnCancel.setOnClickListener(p.cancelListener);
            } else {
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }


            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(canCancel);
            dialog.setCancelable(canCancel);
            return dialog;
        }


    }

    private static class BottomMenu {
        public String funName;
        public View.OnClickListener listener;

        public BottomMenu(String funName, View.OnClickListener listener) {
            this.funName = funName;
            this.listener = listener;
        }
    }
}
