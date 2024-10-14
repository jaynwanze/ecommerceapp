package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.utils.widget.MotionLabel;

import com.example.ca1.R;
import com.example.ca1.dao.ProductDAO;
import com.example.ca1.pojo.Product;
import com.example.ca1.callback.Callback;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        //get user email from intent
        Intent intent = getIntent();
        this.email = intent.getStringExtra("userEmail");
        //Set welcome message with passed in email
        String outputText = "Welcome " + this.email + "!";
        MotionLabel motionLabel = findViewById(R.id.output_text);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(outputText);
        motionLabel.setText(spannableString);

        Callback callback = new Callback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(PurchaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(PurchaseActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }

            @Override
            public <T> void onObjectsRetrieved(List<T> products) {
                List<Product> productList = new ArrayList<>();
                for (T obj : products) {
                    if (obj instanceof Product) {
                        productList.add((Product) obj);
                    }
                }
                displayShoppingList(productList);
            }
        };


        SearchView searchView = findViewById(R.id.search_product);
        searchView.setQueryHint("Search to remove product...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAndRemoveProduct(query, callback);
                return true; // Handle search submission
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes (optional)
                return false;
            }
        });

        //set up button and on click listener
        Button addShoppingListBtn = findViewById(R.id.add_to_shopping_list_button);
        addShoppingListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addProductToShoppingList(callback);
            }
        });

        //set up button and on click listener
        Button viewShoppingListBtn = findViewById(R.id.view_shopping_list_button);
        viewShoppingListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getAllProducts(callback);
            }
        });

        //set up button and on click listener
        Button checkOutBtn = findViewById(R.id.checkout_button);
        checkOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToCheckOut();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Toast.makeText(PurchaseActivity.this, "Information: Ecommerce App", Toast.LENGTH_LONG).show();

        }
        if (id == R.id.action_profile) {
            Toast.makeText(PurchaseActivity.this,"Profile: " + this.email, Toast.LENGTH_LONG).show();
        }
        if (id == R.id.action_logout) {
            goBack();

        }
        return true;
    }

    public void displayShoppingList(List<Product> products) {

        if (products == null || products.isEmpty()) {
            Toast.makeText(this, "No products in shopping list", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Shopping List");

        // Create a ListView to display the products
        ListView productListView = new ListView(this);
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        productListView.setAdapter(adapter);

        // Set the ListView as the dialog's view
        builder.setView(productListView);

        // Add a "Close" button to the dialog
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void addProductToShoppingList(Callback callback) {
        //extract product details from edit text
        EditText productNameEditText = findViewById(R.id.edit_product_name);
        EditText productQuantityEditText = findViewById(R.id.edit_product_quantity);
        EditText productUnitPriceEditText = findViewById(R.id.edit_product_unit_price);
        String productName = productNameEditText.getText().toString();
        String productQuantity= productQuantityEditText.getText().toString();
        String productUnitPrice = productUnitPriceEditText.getText().toString();

        //add pass details to dao to add to db
        ProductDAO productDAO = new ProductDAO();
        productDAO.checkProductExistsAndAddToDb(productName, Integer.parseInt(productQuantity), Double.parseDouble(productUnitPrice), callback);

        //TODO:
        //VALDIATE INPUT - no psaces, letters, numbers,double
        //add names to toasts
        //add state management
        //clear search view after input

        //clear edit text fields
        productNameEditText.setText("");
        productQuantityEditText.setText("");
        productUnitPriceEditText.setText("");
    }

    public void getAllProducts(Callback callback) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.getAllProducts(callback);
    }

    public void searchAndRemoveProduct(String productName, Callback callback) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.searchAndRemoveProduct(productName, callback);
        SearchView searchView = findViewById(R.id.search_product);
        searchView.clearFocus();
    }

    public void goBack() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void goToCheckOut() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
        this.finish();
    }

}
