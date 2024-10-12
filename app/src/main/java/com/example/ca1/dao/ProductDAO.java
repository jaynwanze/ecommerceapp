package com.example.ca1.dao;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ca1.activity.PurchaseActivity;
import com.example.ca1.pojo.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void checkProductExistsAndAddToDb(String productName, int productQuantity, double productUnitPrice, PurchaseActivity purchaseActivity) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        FirebaseUser m_user = m_auth.getCurrentUser();
        // check if user is logged in
        if (m_user == null) {
            Toast.makeText(purchaseActivity, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        //check if product already exists
        String userId = m_user.getUid();
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean productExists = false;
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product productObj = productSnapshot.getValue(Product.class);
                    if (productObj != null && productObj.getName().equalsIgnoreCase(productName)) {
                        productExists = true;
                        break; // Exit loop if product is found
                    }
                }

                if (productExists) {
                    Toast.makeText(purchaseActivity, "Product already exists: Cannot add to shopping list", Toast.LENGTH_SHORT).show();
                } else {
                    addProductToDb(productName, productQuantity, productUnitPrice, purchaseActivity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Log.d("PurchaseActivity", "Database error: " + error.getMessage());
            }
        });
    }

    public void addProductToDb(String name, int quantity, double price, PurchaseActivity purchaseActivity) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        FirebaseUser m_user = m_auth.getCurrentUser();

        // check if user is logged in
        if (m_user == null) {
            Toast.makeText(purchaseActivity, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        //get generated key for product
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        String key = productsRef.push().getKey();
        // check if user is logged in
        if (key == null) {
            Log.d("PurchaseActivity", "Failed generate key for product");
            Toast.makeText(purchaseActivity, "Failed to add to product to shopping list", Toast.LENGTH_SHORT).show();
            return;
        }

        //add product to db
        String userId = m_user.getUid();
        Product product = new Product(name, quantity, price, userId);
        productsRef.child(key)
                .setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PurchaseActivity", "Write of product to database is successful");
                        Toast.makeText(purchaseActivity, "Product added to shopping list", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PurchaseActivity", "Write of product to database failed");
                        Toast.makeText(purchaseActivity, "Unable to add product to shopping list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void searchAndRemoveProduct(String productName, PurchaseActivity purchaseActivity) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        FirebaseUser m_user = m_auth.getCurrentUser();

        // check if user is logged in
        if (m_user == null) {
            Toast.makeText(purchaseActivity, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = m_user.getUid();
        //gets products related to logged in user and removes product by name
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products") ;
        productsRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product productObj = productSnapshot.getValue(Product.class);
                    if (productObj != null && productObj.getName().equalsIgnoreCase(productName)) {
                        productsRef.child(productSnapshot.getKey()).removeValue(); // Remove by key
                        Toast.makeText(purchaseActivity, "Product removed from shopping list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(purchaseActivity, "Product not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DBError", "Cancel Access DB");
                Toast.makeText(purchaseActivity, "Unable to remove product by name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllProducts(PurchaseActivity purchaseActivity) {
        FirebaseAuth m_auth = FirebaseAuth.getInstance();
        FirebaseUser m_user = m_auth.getCurrentUser();


        // check if user is logged in
        if (m_user == null) {
            Toast.makeText(purchaseActivity, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = m_user.getUid();

        //gets products related to logged in user
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                productsRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Product> products = new ArrayList<>();
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            Product productObj = productSnapshot.getValue(Product.class);
                            if (productObj != null) {
                                products.add(productObj);
                            }
                        }
                        purchaseActivity.displayShoppingList(products);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("DBError", "Cancel Access DB");
                        Toast.makeText(purchaseActivity, "Unable to remove product by name", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}








