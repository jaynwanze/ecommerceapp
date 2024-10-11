package com.example.ca1.textchange;


import android.text.Editable;
import android.text.TextWatcher;
import com.example.ca1.activity.SignUpActivity;

public class TextChangeHandler implements TextWatcher
{

    private final SignUpActivity mainActivity;

    public TextChangeHandler(SignUpActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void afterTextChanged (Editable e) {
        mainActivity.validateWeakOrStrongPassword();
    }
    public void beforeTextChanged (CharSequence s, int start, int count, int
            after ) {
    }
    public void onTextChanged (CharSequence s, int start, int before, int after
    ){
    }
}
