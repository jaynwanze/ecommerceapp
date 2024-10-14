package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ca1.R;
import com.example.ca1.callback.Callback;
import com.example.ca1.dao.ProductDAO;
import com.example.ca1.pojo.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_checkout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get user email from intent
        Intent intent = getIntent();
        this.email = intent.getStringExtra("userEmail");

        Callback callback = new Callback() {
            @Override
            public void onSuccess(String message, Object... params) {
                Toast.makeText(CheckoutActivity.this, "Purchase was successful", Toast.LENGTH_SHORT).show();
                //go back to purchase activity
                Toast.makeText(CheckoutActivity.this, "Going back to home page...", Toast.LENGTH_SHORT).show();
                goBack();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CheckoutActivity.this, "Unable to complete purchase", Toast.LENGTH_SHORT).show();
            }
            @Override
            public <T> void onObjectsRetrieved(List<T> products) {
                List<Product> productList = new ArrayList<>();
                for (T obj : products) {
                    if (obj instanceof Product) {
                        productList.add((Product) obj);
                    }
                }
                // Calculate and display checkout details
                calculateCheckoutDetails(productList);

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
                performPurchase(callback);
            }
        });

        //set up button and on click listener
        Button homepageBtn = findViewById(R.id.homepage_button);
        homepageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
                Toast.makeText(CheckoutActivity.this, "Going back to home page...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setDisplay(List<Product> productList){
        //Get views from layout
        TextView emptyMessage = findViewById(R.id.empty_shopping_list_message);
        ConstraintLayout checkoutContainer = findViewById(R.id.checkout_container);
        Button homepageBtn = findViewById(R.id.homepage_button);

        //initial reset of views
        emptyMessage.setVisibility(View.GONE);
        homepageBtn.setVisibility(View.GONE);
        checkoutContainer.setVisibility(View.GONE);

        // Check if the product list is empty
        if (productList.isEmpty()) {
            // Hide checkout details
            checkoutContainer.setVisibility(View.GONE);

            // Show empty message and hide checkout details
            emptyMessage.setVisibility(View.VISIBLE);
            homepageBtn.setVisibility(View.VISIBLE);
        } else {
            // Hide empty message and show checkout details
            emptyMessage.setVisibility(View.GONE);
            homepageBtn.setVisibility(View.GONE);
            // Show checkout details
            checkoutContainer.setVisibility(View.VISIBLE);
        }


    }

    private boolean isValidCardNumber(String cardNumber) {
        //16 number validation for credit card
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        return cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        //// MM/YYYY format
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        return expiryDate.matches("\\d{2}-\\d{4}");
    }


    private void calculateCheckoutDetails(List<Product> productList) {
        this.setDisplay(productList);

        // Calculate and display checkout details
        double totalCost = 0.0;
        int totalQuantity = 0;
        for (Product product : productList) {
            totalCost += product.getUnitPrice() * product.getQuantity();
            totalQuantity += product.getQuantity();
        }
        TextView totalValue = findViewById(R.id.total_value_output);
        totalValue.append(String.format(Locale.getDefault(),"%.2f", totalCost));
        TextView numberOfItems = findViewById(R.id.number_of_items_output);
        numberOfItems.append(String.valueOf(totalQuantity));
    }

    private void performPurchase(Callback callback) {
        TextView cardNumberEditText = findViewById(R.id.edit_card_number);
        TextView expiryDateEditText = findViewById(R.id.edit_card_expiry);
        String cardNumber = cardNumberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();

        //check if card number is valid
        if (cardNumber.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        cardNumber =  cardNumber.trim();
        expiryDate = expiryDate.trim();


        if (!isValidCardNumber(cardNumber)) {
            Toast.makeText(this, "Invalid card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidExpiryDate(expiryDate)) {
            Toast.makeText(this, "Invalid expiry date", Toast.LENGTH_SHORT).show();
            return;
        }
        //delete all products from db
        ProductDAO productDAO = new ProductDAO();
        productDAO.removeAllProducts(callback);
    }

    private void goBack() {
        //go back to purchase activity
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra("userEmail", this.email);
        startActivity(intent);
        this.finish();
    }
}
