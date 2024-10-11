package com.example.ca1.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.CreateCredentialRequest;

import com.example.ca1.R;
import com.example.ca1.dao.UserDAO;
import com.example.ca1.pojo.User;
import com.example.ca1.textchange.TextChangeHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        TextChangeHandler tch = new TextChangeHandler(this);
        EditText editPassword = findViewById(R.id.password_toggle);
        editPassword.addTextChangedListener(tch);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signUp();
            }
        });


    }

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

    public boolean validatePassword() {
        EditText editPassword = findViewById(R.id.password_toggle);
            return editPassword.length() >= 6 && !editPassword.getText().toString().contains(" ") && editPassword.getText().toString().matches(".*\\d.*");
    }

    public boolean validateEmail() {
        EditText editEmail = findViewById(R.id.edit_email);
        // Get the string and remove any extra spaces
        String email = editEmail.getText().toString().trim();
        //Validation
        return email.contains("@") && !email.contains(" ");
    }

    public void signUp() {
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
        userDAO.createUserWithEmailAndPassword(userEmail, userPassword, this);
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