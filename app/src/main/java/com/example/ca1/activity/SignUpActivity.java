package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ca1.R;
import com.example.ca1.callback.Callback;
import com.example.ca1.dao.UserDAO;
import com.example.ca1.textchange.TextChangeHandler;


import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //create callback
        Callback callback = new Callback() {
            @Override
            public void onSuccess(String message, Object... params) {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, PurchaseActivity.class);
                //get email from params
                if (params.length != 0) {
                    String userEmail = (String) params[0];
                    //pass email to purchase activity
                    Log.d("SignUpActivity", userEmail);
                    intent.putExtra("userEmail", userEmail);
                }
                //start purchase activity
                SignUpActivity.this.startActivity(intent);
                SignUpActivity.this.finish();

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public <T> void onObjectsRetrieved(List<T> products) {
            }
        };

        //add text change listener to password field
        TextChangeHandler tch = new TextChangeHandler(this);
        EditText editPassword = findViewById(R.id.password_toggle);
        editPassword.addTextChangedListener(tch);


        //add click listener to sign up button
        Button signUpBtn = findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signUp(callback);
            }
        });

        //add click listener to sign in button
        Button signInBtn = findViewById(R.id.sign_in_button);
        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn(callback);
            }
        });
    }

    //check if password is strong enough
    public void validateWeakOrStrongPassword() {
        EditText editPassword = findViewById(R.id.password_toggle);
        if (editPassword.length() >= 6 && editPassword.length() < 8) {
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText(R.string.weak_password);
        }
        else if (editPassword.length() >= 7) {
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText(R.string.strong_password);
        }
        else{
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText("");
        }
    }

    //check if password is valid
    private boolean validatePassword() {
        EditText editPassword = findViewById(R.id.password_toggle);
            return editPassword.length() >= 6 && !editPassword.getText().toString().contains(" ") && editPassword.getText().toString().matches(".*\\d.*");
    }

    //check if email is valid
    private boolean validateEmail() {
        EditText editEmail = findViewById(R.id.edit_email);
        // Get the string and remove any extra spaces
        String email = editEmail.getText().toString().trim();
        //Validation
        return email.contains("@") && !email.contains(" ");
    }

    //sign up user
    private void signUp(Callback callback) {
        if (!validateEmail()) {
            Toast.makeText(SignUpActivity.this, "Invalid Email: Must contain @ and no spaces", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validatePassword()) {
            Toast.makeText(SignUpActivity.this, "Invalid Password: Must be at least 6 characters, have no spaces and contain at least 1 digits", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editEmail = findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = findViewById(R.id.password_toggle);
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = new UserDAO();
        userDAO.createUserWithEmailAndPassword(userEmail, userPassword, callback);
    }

    //sign in user
    private void signIn(Callback callback) {
        if (!validateEmail()) {
            Toast.makeText(SignUpActivity.this, "Invalid Email: Must contain @ and no spaces", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validatePassword()) {
            Toast.makeText(SignUpActivity.this, "Invalid Password: Must be at least 6 characters, have no spaces and contain at least 1 digits", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editEmail = findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = findViewById(R.id.password_toggle);
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = new UserDAO();
        userDAO.signInWithEmailAndPassword(userEmail, userPassword, callback);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("myActivity", "App is restoring");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("myActivity", "App is starting");

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.i("myActivity", "App is stopping");
    }

    @Override
    protected void onPause () {
        super.onPause();
        Log.i("myActivity", "Switching to another app ");
    }
}