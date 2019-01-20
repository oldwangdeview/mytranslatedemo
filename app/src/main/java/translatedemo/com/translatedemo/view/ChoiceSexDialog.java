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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class ChoiceSexDialog extends Dialog {

    public ChoiceSexDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Params {
        private final List<ChoiceSexDialog.BottomMenu> menuList = new ArrayList<>();
        private View.OnClickListener cancelListener;
        private String menuTitle;
        private String cancelText;
        private Context context;
    }

    public static class Builder {
        private boolean canCancel = true;
        private boolean shadow = true;
        private final ChoiceSexDialog.Params p;

        public Builder(Context context) {
            p = new ChoiceSexDialog.Params();
            p.context = context;
        }

        public ChoiceSexDialog.Builder setCanCancel(boolean canCancel) {
            this.canCancel = canCancel;
            return this;
        }

        public ChoiceSexDialog.Builder setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        public ChoiceSexDialog.Builder setTitle(String title) {
            this.p.menuTitle = title;
            return this;
        }

        public ChoiceSexDialog.Builder addMenu(String text, View.OnClickListener listener) {
            ChoiceSexDialog.BottomMenu bm = new ChoiceSexDialog.BottomMenu(text, listener);
            this.p.menuList.add(bm);
            return this;
        }

        public ChoiceSexDialog.Builder addMenu(int textId, View.OnClickListener listener) {
            return addMenu(p.context.getString(textId), listener);
        }

        public ChoiceSexDialog.Builder setCancelListener(View.OnClickListener cancelListener) {
            p.cancelListener = cancelListener;
            return this;
        }

        public ChoiceSexDialog.Builder setCancelText(int resId) {
            p.cancelText = p.context.getString(resId);
            return this;
        }

        public ChoiceSexDialog.Builder setCancelText(String text) {
            p.cancelText = text;
            return this;
        }

        public ChoiceSexDialog create() {
            final ChoiceSexDialog dialog = new ChoiceSexDialog(p.context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_bottom_menu, null);

            TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            LinearLayout layContainer = (LinearLayout) view.findViewById(R.id.lay_container);
            ViewGroup.LayoutParams lpItem = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.LayoutParams lpDivider = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            int dip1 = (int) (1 * p.context.getResources().getDisplayMetrics().density + 0.5f);
            int spacing = dip1 * 12;

            boolean hasTitle = !TextUtils.isEmpty(p.menuTitle);
            if (hasTitle) {
                TextView tTitle = new TextView(p.context);
                tTitle.setLayoutParams(lpItem);
                tTitle.setGravity(Gravity.CENTER);
                tTitle.setTextColor(0xFF8F8F8F);
                tTitle.setText(p.menuTitle);
                tTitle.setTextSize(17);
                tTitle.setPadding(0, spacing, 0, spacing);
                tTitle.setBackgroundResource(R.drawable.common_dialog_selection_selector_top);
                layContainer.addView(tTitle);

                View viewDivider = new View(p.context);
                viewDivider.setLayoutParams(lpDivider);
                viewDivider.setBackgroundColor(0xFFCED2D6);
                layContainer.addView(viewDivider);
             /*   View viewDividers = new View(p.context);
                viewDividers.setLayoutParams(lpDivider);
                viewDividers.setBackgroundColor(0xFFCED2D6);
                layContainer.addView(viewDividers);*/

            }

            for (int i = 0; i < p.menuList.size(); i++) {
                ChoiceSexDialog.BottomMenu bottomMenu = p.menuList.get(i);
                TextView bbm = new TextView(p.context);
                bbm.setLayoutParams(lpItem);
                int backgroundResId = R.drawable.common_dialog_selection_selector_center;
                if (p.menuList.size() > 1) {
                    if (i == 0) {
                        if (hasTitle) {
                            backgroundResId = R.drawable.common_dialog_selection_selector_center;
                        } else {
                            backgroundResId = R.drawable.common_dialog_selection_selector_top;
                        }
                    } else if (i == p.menuList.size() - 1) {
                        backgroundResId = R.drawable.common_dialog_selection_selector_bottom;
                    }
                } else if (p.menuList.size() == 1) {
                    backgroundResId = R.drawable.common_dialog_selection_selector_singleton;
                }
                bbm.setBackgroundResource(backgroundResId);
                bbm.setPadding(0, spacing, 0, spacing);
                bbm.setGravity(Gravity.CENTER);
                bbm.setText(bottomMenu.funName);
                bbm.setTextColor(0xFF007AFF);
                bbm.setTextSize(19);
                bbm.setOnClickListener(bottomMenu.listener);
                layContainer.addView(bbm);

                if (i != p.menuList.size() - 1) {
                    View viewDivider = new View(p.context);
                    viewDivider.setLayoutParams(lpDivider);
                    viewDivider.setBackgroundColor(0xFFCED2D6);
                    layContainer.addView(viewDivider);
                }
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
