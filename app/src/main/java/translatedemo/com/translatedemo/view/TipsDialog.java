package translatedemo.com.translatedemo.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import translatedemo.com.translatedemo.R;

/**
 * 提示弹框
 *
 * @author fengzhen
 * @version v1.0, 2018/3/29
 */
public class TipsDialog extends Dialog {

    /**
     * 绑定Dialog标签
     */
    private Object tag;

    public interface OnRightClickListener {
        void onRightClick(Object args);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置弹框默认风格
     */
    private TipsDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public TipsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public TipsDialog setContent(String newContent) {
        Builder builder = (Builder) getTag();
        return builder.setContentView(newContent).build();
    }
    /**
     * 创建Dialog
     *
     * @author fengzhen
     * @version v1.0, 2018/3/29
     */
    public static class Builder {
        @BindView(R.id.tv_content_dialog_tips)
        TextView mTvContentDialogTips;
        @BindView(R.id.tv_title_dialog_tips)
        TextView mTvTitleDialogTips;
        @BindView(R.id.btn_left_dialog_tips)
        Button mBtnLeftDialogTips;
        @BindView(R.id.btn_right_dialog_tips)
        Button mBtnRightDialogTips;

        private TipsDialog dialog;
        private View layout;

        private String content;
        private String title;
        private String leftButtonText;
        private int rightmipmap = 0;
        private String rightButtonText;
        private View.OnClickListener leftClickListener;
        private View.OnClickListener rightClickListener;
        private OnCancelListener cancelListener;
        private int lefttextcolor = 0;
        private int righttextcolor = 0;
        private int tittextsize = 0;
        private Context mContext;
        private int contentcolor = -1;
        private int titlecolor = -1;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new TipsDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_tips, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }

        public Builder setContentColor(int contentcolor){
            this.contentcolor = contentcolor;
            return this;
        }
        public Builder settitlecolor(int titlecolor){
            this.titlecolor = contentcolor;
            return this;
        }

        public TipsDialog build() {
            mTvContentDialogTips.setText(content);
            if(contentcolor>-1) {
                mTvContentDialogTips.setTextColor(contentcolor);
            }
            mTvTitleDialogTips.setText(title);
            if(contentcolor>-1) {
                mTvTitleDialogTips.setTextColor(titlecolor);
            }
            /**
             *  处理文本信息排列方式
             *  文字只有一行：文字居中展示
             *  文字超过一行：文字居左展示
             *
             * @author fengzhen
             * @version v1.0, 2018/3/29
             */
            mTvContentDialogTips.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = mTvContentDialogTips.getLineCount();
                    if (lineCount > 1) {
                        mTvContentDialogTips.setGravity(Gravity.LEFT);
                    } else {
                        mTvContentDialogTips.setGravity(Gravity.CENTER);
                    }
                }
            });
            mBtnLeftDialogTips.setText(leftButtonText);
            mBtnRightDialogTips.setText(rightButtonText);
            if(rightmipmap>0){
                mBtnRightDialogTips.setBackgroundResource(rightmipmap);
            }
            if((lefttextcolor>0)){
                mBtnLeftDialogTips.setTextColor(mContext.getResources().getColor(lefttextcolor));
            }
            if(righttextcolor>0){
                mBtnRightDialogTips.setTextColor(mContext.getResources().getColor(lefttextcolor));
            }
            if(tittextsize>0){
                mTvTitleDialogTips.setTextSize(UIUtils.dip2px(tittextsize));
            }

            mBtnLeftDialogTips.setOnClickListener(leftClickListener);
            mBtnRightDialogTips.setOnClickListener(rightClickListener);
            dialog.setOnCancelListener(cancelListener);
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

        /**
         * 设置弹框提示内容
         *
         * @author fengzhen
         * @version v1.0, 2018/3/29
         */
        public Builder setContentView(String msg) {
            this.content = msg;
            return this;
        }

        /**
         * 设置弹框标题
         *
         * @author fengzhen
         * @version v1.0, 2018/3/29
         */
        public Builder setTitle(String msg) {
            this.title = msg;
           mTvTitleDialogTips.setVisibility(View.VISIBLE);
            return this;
        }



        /**
         * 设置左侧按键的文字、点击事件
         *
         * @author fengzhen
         * @version v1.0, 2018/3/29
         */
        public Builder setLeftButton(String leftButtonText, View.OnClickListener listener) {
            this.leftButtonText = leftButtonText;
            this.leftClickListener = listener;
            return this;
        }

        /**
         * 设置右侧按钮背景
         * @param rightmipmap
         * @return
         */
        public Builder seRightButtonBackgourd(int rightmipmap){
            this.rightmipmap = rightmipmap;
            return this;
        }

        /**
         * 设置title大小
         * @param tittextsize
         * @return
         */
        public Builder setTittextSize(int tittextsize){
            this.tittextsize = tittextsize;
            return this;
        }

        /**
         * 设置左右按钮文字颜色
         * @param lefttextcolor
         * @param righttextcolor
         * @return
         */

        public Builder setTextColor(int lefttextcolor,int righttextcolor){
            this.lefttextcolor = lefttextcolor;
            this.righttextcolor = righttextcolor;
            return this;
        }


        /**
         * 设置右侧按键文字、点击事件
         *
         * @author fengzhen
         * @version v1.0, 2018/3/29
         */
        public Builder setRightButton(String rightButtonText, View.OnClickListener listener) {
            this.rightButtonText = rightButtonText;
            this.rightClickListener = listener;
            return this;
        }



        /**
         * 点击取消事件处理
         *
         * @author fengzhen
         * @version v1.0, 2018/3/29
         */
        public Builder setCancelClickListener(OnCancelListener listener) {
            this.cancelListener = listener;
            return this;
        }
    }
}
