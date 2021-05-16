package com.example.cartproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartproject.adapters.StoreListAdapter;
import com.example.cartproject.models.StoreModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class Dashboard extends AppCompatActivity implements StoreListAdapter.StoreListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("1Cart Dashboard");

        List<StoreModel> storeModelList = getStoreData();

        initRecyclerView(storeModelList);
    }

    private void initRecyclerView(List<StoreModel> storeModelList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StoreListAdapter adapter = new StoreListAdapter(storeModelList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<StoreModel> getStoreData() {
        InputStream is = getResources().openRawResource(R.raw.appfile); //appfile is the json file name from raw folder
        Writer writer = new StringWriter();
        char[] buffer = new char[10899999];
        try {
            Reader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int p;
            while ((p = r.read(buffer)) != -1) {
                writer.write(buffer, 0, p);
            }
        } catch (Exception e) {

        }

        String jsonStr = writer.toString(); // get data from json file
        Gson gson = new Gson();
        StoreModel[] storeModels = gson.fromJson(jsonStr, StoreModel[].class);
        List<StoreModel> storeList = Arrays.asList(storeModels);

        return storeList;

    }

    @Override
    public void onItemClick(StoreModel storeModel) {
        Intent intent = new Intent(Dashboard.this, StoreProductActivity.class);
        intent.putExtra("StoreModel", storeModel);
        startActivity(intent);

    }
}