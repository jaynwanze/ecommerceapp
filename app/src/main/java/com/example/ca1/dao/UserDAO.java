package com.example.ca1.dao;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ca1.activity.PurchaseActivity;
import com.example.ca1.activity.SignUpActivity;
import com.example.ca1.pojo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserDAO {

    public void createUserWithEmailAndPassword( String userEmail, String userPassword, SignUpActivity signUpActivity ) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        m_auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(signUpActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignupActivity", "createUserWithEmail:success");
                            FirebaseUser m_user = m_auth.getCurrentUser();
                            Toast.makeText(signUpActivity, "Authentication success.", Toast.LENGTH_SHORT).show();
                            if (m_user != null) {
                                String userId = m_user.getUid();
                                // add user to db
                                addUserToDb( userId, userEmail, userPassword , signUpActivity);
                            }
                            Intent intent = new Intent(signUpActivity, PurchaseActivity.class);
                            intent.putExtra("userEmail", userEmail);
                            signUpActivity.startActivity(intent);
                            signUpActivity.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signUpActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addUserToDb(String userId, String userEmail, String userPassword, SignUpActivity signUpActivity) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        User user = new User(userEmail, userPassword);

        db.child("Users")
                .child(userId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SignupActivity", "Write of user to database is successful");
                        Toast.makeText(signUpActivity, "User added to database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Log.d("SignupActivity", "Write of user to database failed");
                       Toast.makeText(signUpActivity, "User not added to database", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
