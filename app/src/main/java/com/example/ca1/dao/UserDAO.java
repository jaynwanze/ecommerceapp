package com.example.ca1.dao;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.ca1.callback.Callback;
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

    public void createUserWithEmailAndPassword(String userEmail, String userPassword, Callback callback) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        m_auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignupActivity", "createUserWithEmail:success");
                            FirebaseUser m_user = m_auth.getCurrentUser();
                            if (m_user != null) {
                                String userId = m_user.getUid();
                                // add user to db
                                addUserToDb( userId, userEmail, userPassword , callback);
                            }
                            callback.onSuccess("Authentication successful", userEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
                            callback.onError("Authentication failed");
                        }
                    }
                });
    }

    public void signInWithEmailAndPassword(String userEmail, String userPassword, Callback callback) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        m_auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignupActivity", "SignInUserWithEmail:success");
                            callback.onSuccess("User signed in", userEmail);
                            Log.d("UserDAO", userEmail);
                        } else {
                           // If sign in fails, display a message to the user.
                            Log.w("SignupActivity", "SignInUserWithEmail:failure", task.getException());
                            callback.onError("Authentication failed");
                        }
                    }
                });
    }

    public void addUserToDb(String userId, String userEmail, String userPassword, Callback callback) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        User user = new User(userEmail, userPassword);

        db.child("Users")
                .child(userId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SignupActivity", "Write of user to database is successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Log.d("SignupActivity", "Write of user to database failed");
                       callback.onError("Error adding user to database");
                    }
                });
    }

}
