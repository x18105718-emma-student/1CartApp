package com.example.cartproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartproject.adapters.PlaceOrderAdapter;
import com.example.cartproject.models.Product;
import com.example.cartproject.models.StoreModel;

public class PlaceOrder extends AppCompatActivity {

    private EditText inputName, inputAddress1, inputAddress2, inputCounty, inputPostcode, inputCardNumber, inputExpiry, inputCVV;
    private RecyclerView cartItemsRecyclerView;
    private TextView tvSubtotalAmount, tvDeliveryChargeAmount, tvDeliveryCharge, tvTotalAmount, buttonPlaceYourOrder;
    private SwitchCompat switchDelivery;
    private boolean yestoDelivery; //checks if the user wants delivery or collection
    private PlaceOrderAdapter placeOrderAdapter; //reference adapter to use recyclerview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        //pasted from store product activity
        final StoreModel storeModel = getIntent().getParcelableExtra("StoreModel");
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(storeModel.getName());
        //actionBar.setSubtitle(storeModel.getDesc());
        actionBar.setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.inputName);
        inputAddress1 = findViewById(R.id.inputAddress1);
        inputAddress2 = findViewById(R.id.inputAddress2);
        inputCounty = findViewById(R.id.inputCounty);
        inputPostcode = findViewById(R.id.inputPostcode);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputExpiry = findViewById(R.id.inputExpiry);
        inputCVV = findViewById(R.id.inputCVV);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount);
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        buttonPlaceYourOrder = findViewById(R.id.buttonPlaceYourOrder);
        switchDelivery = findViewById(R.id.switchDelivery);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);

        buttonPlaceYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrderButtonClick(storeModel);
            }
        });

        switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override //applies if the user wants items delivered - system asks for details
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //if the user wants delivery ask for details
                    inputAddress1.setVisibility(View.VISIBLE);
                    inputAddress2.setVisibility(View.VISIBLE);
                    inputCounty.setVisibility(View.VISIBLE);
                    inputPostcode.setVisibility(View.VISIBLE);
                    tvDeliveryChargeAmount.setVisibility(View.VISIBLE);
                    tvDeliveryCharge.setVisibility(View.VISIBLE);
                    yestoDelivery = true; //delivery = true
                    calculateTotalAmount(storeModel);
                } else { //if they don't want items delivered don't ask for details
                    inputAddress1.setVisibility(View.GONE);
                    inputAddress2.setVisibility(View.GONE);
                    inputCounty.setVisibility(View.GONE);
                    inputPostcode.setVisibility(View.GONE);
                    tvDeliveryChargeAmount.setVisibility(View.GONE);
                    tvDeliveryCharge.setVisibility(View.GONE);
                    yestoDelivery = false; // delivery = false
                    calculateTotalAmount(storeModel);
                }
            }
        });
        initRecyclerView(storeModel);
        calculateTotalAmount(storeModel);
    }

    private void calculateTotalAmount(StoreModel storeModel) { //calculates the price of products based on prices set in the store model class
        float subTotalAmount = 0f;

        for (Product p : storeModel.getProducts()) {
            subTotalAmount += p.getPrice() * p.getTotalInCart();
        }

        tvSubtotalAmount.setText("€" + String.format("%.2f", subTotalAmount)); //format the price to 2 decimal places
        if (yestoDelivery) {
            tvDeliveryChargeAmount.setText("€" + String.format("%.2f", storeModel.getShipping()));//gets the shipping charge from store model class
            subTotalAmount += storeModel.getShipping(); //adds total plus shipping
        }
        tvTotalAmount.setText("€" + String.format("%.2f", subTotalAmount));
    }

    private void onPlaceOrderButtonClick(StoreModel storeModel) { //validate delivery details if fields are left empty
        if (TextUtils.isEmpty(inputName.getText().toString())) {
            inputName.setError("Name cannot be left empty ");
            return;
        } else if (yestoDelivery && TextUtils.isEmpty(inputAddress1.getText().toString())) {
            inputAddress1.setError("Address line 1 cannot be left empty");
            return;
        } else if (yestoDelivery && TextUtils.isEmpty(inputAddress2.getText().toString())) {
            inputAddress2.setError("Address line 2 cannot be left empty ");
            return;
        } else if (yestoDelivery && TextUtils.isEmpty(inputCounty.getText().toString())) {
            inputCounty.setError("County cannot be left empty ");
            return;
        } else if (TextUtils.isEmpty(inputCardNumber.getText().toString())) {
            inputCardNumber.setError("Card Number cannot be left empty ");
            return;
        } else if (TextUtils.isEmpty(inputExpiry.getText().toString())) {
            inputExpiry.setError("Expiry date cannot be left empty ");
            return;
        } else if (TextUtils.isEmpty(inputCVV.getText().toString())) {
            inputCVV.setError("CVV cannot be left empty ");
            return;
        }
        //direct user to thank you screen if payment is successful
        Intent intent = new Intent(PlaceOrder.this, ThankYou.class);
        intent.putExtra("StoreModel", storeModel);
        startActivityForResult(intent, 1000); // go back to store list if request code is 1000
    }

    private void initRecyclerView(StoreModel storeModel) {
        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placeOrderAdapter = new PlaceOrderAdapter(storeModel.getProducts());
        cartItemsRecyclerView.setAdapter(placeOrderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1000) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
