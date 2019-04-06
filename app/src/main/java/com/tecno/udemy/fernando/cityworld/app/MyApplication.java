package com.tecno.udemy.fernando.cityworld.app;

import android.app.Application;

import com.tecno.udemy.fernando.cityworld.model.City;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger CityID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setupConfigurations();

        Realm realmDB = Realm.getDefaultInstance();
        CityID = setInitialValueForID(realmDB, City.class);

        realmDB.close();
    }

    private void setupConfigurations(){
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(2)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private AtomicInteger setInitialValueForID(Realm realmDB, Class anyClass){
        RealmResults results = realmDB.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
