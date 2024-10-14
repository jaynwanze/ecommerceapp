package com.example.ca1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.utils.widget.MotionLabel;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
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

import java.util.ArrayList;
import java.util.List;

import com.example.ca1.R;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


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


    public void goBack() {
        Intent intent = new Intent(this, PurchaseActivity.class);
        startActivity(intent);
        this.finish();
    }
}
