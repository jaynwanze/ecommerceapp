package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ca1.R;
import com.example.ca1.callback.Callback;
import com.example.ca1.dao.ProductDAO;
import com.example.ca1.pojo.Product;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_checkout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Callback callback = new Callback() {
            @Override
            public void onSuccess(String message, Object... params) {
                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CheckoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public <T> void onObjectsRetrieved(List<T> products) {
                List<Product> productList = new ArrayList<>();
                for (T obj : products) {
                    if (obj instanceof Product) {
                        productList.add((Product) obj);
                    }
                    // calculate checkout details
                   calculateCheckoutDetails(productList);
                }
            }
        };

        //Get products from db on init
        ProductDAO productDAO = new ProductDAO();
        productDAO.getAllProducts(callback);

        //set up button and on click listener
        Button cancelPurchaseBtn = findViewById(R.id.cancel_purchase_button);
        cancelPurchaseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
            }
        });

        //set up button and on click listener
        Button confirmPurchaseBtn = findViewById(R.id.confirm_purchase_button);
        confirmPurchaseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //perfromPurchase();
            }
        });

    }

    public void calculateCheckoutDetails(List<Product> productList) {
        double totalCost = 0.0;
        int totalQuantity = 0;
        for (Product product : productList) {
            totalCost += product.getUnitPrice() * product.getQuantity();
        }

    }







    public void goBack() {
        Intent intent = new Intent(this, PurchaseActivity.class);
        startActivity(intent);
        this.finish();
    }
}
