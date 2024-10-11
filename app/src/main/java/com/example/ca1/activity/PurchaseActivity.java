package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionLabel;

import com.example.ca1.R;
import com.example.ca1.dao.ProductDAO;

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
        motionLabel.setText(outputText);

        //set up button and on click listener
        Button button = findViewById(R.id.add_to_basket_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addProductToBasket();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void addProductToBasket() {
        //extract product details from edit text
        EditText productNameEditText = findViewById(R.id.edit_product_name);
        EditText productQuantityEditText = findViewById(R.id.edit_product_quantity);
        EditText productUnitPriceEditText = findViewById(R.id.edit_product_unit_price);
        String productName = productNameEditText.getText().toString();
        String productQuantity= productQuantityEditText.getText().toString();
        String productUnitPrice = productUnitPriceEditText.getText().toString();
        //add pass details to dao to add to db
        ProductDAO productDAO = new ProductDAO();
        productDAO.addProductToDb(productName, Integer.parseInt(productQuantity), Double.parseDouble(productUnitPrice), this);

        //TODO:
        //VALDIATE INPUT
        //CHECK PRODUCT NAME IS UNIQUE
        //clear edit text fields
        productNameEditText.setText("");
        productQuantityEditText.setText("");
        productUnitPriceEditText.setText("");
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

    public void goBack() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

}
