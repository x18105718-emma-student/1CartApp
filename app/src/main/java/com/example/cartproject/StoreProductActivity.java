package com.example.cartproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartproject.adapters.ProductListAdapter;
import com.example.cartproject.models.Product;
import com.example.cartproject.models.StoreModel;

import java.util.ArrayList;
import java.util.List;

public class StoreProductActivity extends AppCompatActivity implements ProductListAdapter.ProductListClickListener {

    private List<Product> productList = null;
    private ProductListAdapter productListAdapter;
    private List<Product> itemsInCartList;
    private int totalItemInCart = 0;
    private TextView buttonCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_product);

        final StoreModel storeModel = getIntent().getParcelableExtra("StoreModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(storeModel.getName());
        actionBar.setSubtitle(storeModel.getDesc());
        actionBar.setDisplayHomeAsUpEnabled(true);


        productList = storeModel.getProducts();
        initRecyclerView();


        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //validation
                if (itemsInCartList != null && itemsInCartList.size() <= 0) { //if there is no items in the cart when the user tries to checkout, display a message.
                    Toast.makeText(StoreProductActivity.this, "Add items to your 1Cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                storeModel.setProducts(itemsInCartList); //update the model
                //direct the user to the place order screen
                Intent intent = new Intent(StoreProductActivity.this, PlaceOrder.class);
                intent.putExtra("StoreModel", storeModel);
                startActivityForResult(intent, 1000);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productListAdapter = new ProductListAdapter(productList, this);
        recyclerView.setAdapter(productListAdapter);
    }

    @Override
    public void onAddToCartClick(Product product) { //calculates how many items are in the cart
        if (itemsInCartList == null) {
            itemsInCartList = new ArrayList<>();
        }
        itemsInCartList.add(product);
        totalItemInCart = 0;

        for (Product p : itemsInCartList) { //display how many items in cart on checkout button
            totalItemInCart = totalItemInCart + p.getTotalInCart();
        }
        buttonCheckout.setText("Checkout with (" + totalItemInCart + ") items");
    }

    @Override
    public void onUpdateCartClick(Product product) { //update the list when item quantity changes
        if (itemsInCartList.contains(product)) {
            int index = itemsInCartList.indexOf(product);
            itemsInCartList.remove(index);
            itemsInCartList.add(index, product);

            totalItemInCart = 0;

            for (Product p : itemsInCartList) { //get the total number of items and update text on the button
                totalItemInCart = totalItemInCart + p.getTotalInCart();
            }
            buttonCheckout.setText("Proceed To Checkout With (" + totalItemInCart + ") Items");
        }
    }

    @Override
    public void onRemoveFromCartClick(Product product) { //remove items from the menu and update interface
        if (itemsInCartList.contains(product)) {
            itemsInCartList.remove(product);
            totalItemInCart = 0;

            for (Product p : itemsInCartList) {
                totalItemInCart = totalItemInCart + p.getTotalInCart();
            }
            buttonCheckout.setText("Checkout (" + totalItemInCart + ") items");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                //nothing is done as the default
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //check request code
        super.onActivityResult(requestCode, resultCode, data);

        //request code set to 1000 in place order activity also, these should match to be successful - back to stores
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            //go back to stores
            finish();
        }
    }
}


