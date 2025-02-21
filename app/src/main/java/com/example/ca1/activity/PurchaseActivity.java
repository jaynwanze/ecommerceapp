package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.utils.widget.MotionLabel;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_purchase), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //get user email from intent
        Intent intent = getIntent();
        this.email = intent.getStringExtra("userEmail");
        //Set welcome message with passed in email
        String outputText = "Welcome " + this.email + "!";
        MotionLabel motionLabel = findViewById(R.id.output_text);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(outputText);
        motionLabel.setText(spannableString);

        //create callback
        Callback callback = new Callback() {
            @Override
            public void onSuccess(String message, Object... params) {
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


        //set up search view and on query text listener
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

    //display shopping list
    private void displayShoppingList(List<Product> products) {

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

    //validation for inputs
    private boolean isValidProductName(String productName) {
        return productName != null && !productName.isEmpty() && productName.matches("[a-zA-Z0-9 ]+");
    }

    private boolean isValidProductQuantity(String productQuantity) {
        try {
            int quantity = Integer.parseInt(productQuantity);
            return quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidProductUnitPrice(String productUnitPrice) {
        try {
            double unitPrice = Double.parseDouble(productUnitPrice);
            return unitPrice > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    //add product to shopping list
    private void addProductToShoppingList(Callback callback) {
        //extract product details from edit text
        EditText productNameEditText = findViewById(R.id.edit_product_name);
        EditText productQuantityEditText = findViewById(R.id.edit_product_quantity);
        EditText productUnitPriceEditText = findViewById(R.id.edit_product_unit_price);
        String productName = productNameEditText.getText().toString();
        String productQuantity= productQuantityEditText.getText().toString();
        String productUnitPrice = productUnitPriceEditText.getText().toString();

        //check if fields are empty
        if (productName.isEmpty() || productQuantity.isEmpty() || productUnitPrice.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Trim strings before validation
        productName = productName.trim();
        productQuantity = productQuantity.trim();
        productUnitPrice = productUnitPrice.trim();


        if (!isValidProductName(productName)) {
            Toast.makeText(this, "Invalid product name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidProductQuantity(productQuantity)) {
            Toast.makeText(this, "Invalid product quantity: Must be a positive integer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidProductUnitPrice(productUnitPrice)) {
            Toast.makeText(this, "Invalid product unit price: Must be a positive number", Toast.LENGTH_SHORT).show();
            return;
        }

        //add pass details to dao to add to db
        ProductDAO productDAO = new ProductDAO();
        productDAO.checkProductExistsAndAddToDb(productName, Integer.parseInt(productQuantity), Double.parseDouble(productUnitPrice), callback);

        //TODO:
        //VALDIATE INPUT - no psaces, letters, numbers,double
        //add state management

        //clear edit text fields
        productNameEditText.setText("");
        productQuantityEditText.setText("");
        productUnitPriceEditText.setText("");
    }

    //get all products from db
    private void getAllProducts(Callback callback) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.getAllProducts(callback);
    }

    // search and remove product from db
    private void searchAndRemoveProduct(String productName, Callback callback) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.searchAndRemoveProduct(productName, callback);
        SearchView searchView = findViewById(R.id.search_product);
        searchView.clearFocus();
    }

    //go back to sign up activity
    private void goBack() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

    //go to checkout activity
    private void goToCheckOut() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("userEmail", this.email);
        startActivity(intent);
        this.finish();
    }

}
