package com.example.ca1.dao;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ca1.activity.PurchaseActivity;
import com.example.ca1.pojo.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDAO {

    public void addProductToDb(String name, int quantity, double price, PurchaseActivity purchaseActivity) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        FirebaseUser m_user = m_auth.getCurrentUser();

        // check if user is logged in
        if (m_user == null) {
            Toast.makeText(purchaseActivity, "User not logged in: Failed to add to product to basket", Toast.LENGTH_SHORT).show();
            return;
        }

        //get generated key for product
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Products");
        String key = db.push().getKey();
        // check if user is logged in
        if (key == null) {
            Log.d("PurchaseActivity", "Failed generate key for product");
            Toast.makeText(purchaseActivity, "Failed to add to product to basket", Toast.LENGTH_SHORT).show();
            return;
        }

        //add product to db
        String userId = m_user.getUid();
        Product product = new Product(name, quantity, price, userId);
        db.child(key)
                .setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PurchaseActivity", "Write of product to database is successful");
                        Toast.makeText(purchaseActivity, "Product added to basket", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PurchaseActivity", "Write of product to database failed");
                        Toast.makeText(purchaseActivity, "Unable to add product to basket", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}








