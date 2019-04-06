package com.tecno.udemy.fernando.cityworld.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tecno.udemy.fernando.cityworld.R;
import com.tecno.udemy.fernando.cityworld.adapters.CityAdapter;
import com.tecno.udemy.fernando.cityworld.model.City;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<City>>{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CityAdapter cityAdapter;

    private FloatingActionButton fabButton;
    private RealmResults<City> cities;

    private Realm realmDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initComponents();

        //dummyCity();
    }

    private void initData(){
        realmDB = Realm.getDefaultInstance();
        cities = realmDB.where(City.class).findAll();
        cities.addChangeListener(this);
        layoutManager = new LinearLayoutManager(this);
        cityAdapter = new CityAdapter(cities, this, R.layout.recycle_item, buttonClickListener, itemClickListener);
    }

    private void dummyCity(){
        realmDB.beginTransaction();
        realmDB.copyToRealm(new City("Ciudad 1", "Ciudad fea", "https://ichef.bbci.co.uk/news/660/cpsprodpb/553F/production/_104632812_hong076.jpg", 4.5F));
        realmDB.commitTransaction();
    }

    private void initComponents(){
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cityAdapter);

        fabButton = findViewById(R.id.fabButton);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAddCity = new Intent(MainActivity.this, AddCityActivity.class);
                startActivity(goToAddCity);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    fabButton.hide();
                else
                    fabButton.show();
            }
        });
    }

    /*------------------------------------ CRUD ACTIONS----------------------------- **/

    private void removeCity(City city){
        realmDB.beginTransaction();
        city.deleteFromRealm();
        realmDB.commitTransaction();
    }

    // -------------------------------------------------------------------------------------


    /*------------------------------------ LISTENERS ----------------------------- **/
    private CityAdapter.OnButtonClickListener buttonClickListener = new CityAdapter.OnButtonClickListener() {
        @Override
        public void onButtonClick(City city, int position) {
            showAlertForRemovinCity(position);
        }
    };

    private CityAdapter.OnClickListener itemClickListener = new CityAdapter.OnClickListener() {
        @Override
        public void onClickListener(City city, int position) {
            Intent goToAddCity = new Intent(MainActivity.this, AddCityActivity.class);
            goToAddCity.putExtra("id", cities.get(position).getId());
            startActivity(goToAddCity);
        }
    };

    @Override
    public void onChange(RealmResults<City> cities) {
        cityAdapter.notifyDataSetChanged();
    }

    /*-------------------------------------------------------------------------------- **/

    private void showAlertForRemovinCity(final int position){
        City city = cities.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove " + city.getName() + "?");
        builder.setTitle("Delete City");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               removeCity(cities.get(position));
            }
        });

        builder.create().show();
    }
}
