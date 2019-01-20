package translatedemo.com.translatedemo.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Author yichao
 * Time  2017/12/20 13:37
 * Dest  ${TODO}
 */

public class TextChangeListener implements TextWatcher{
    private EditText mEditText;
    private ImageButton mImageView;

    public TextChangeListener(EditText editText, ImageButton imageButton) {
        this.mEditText = editText;
        this.mImageView = imageButton;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mEditText.getText().toString().trim().equals("")) {
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
