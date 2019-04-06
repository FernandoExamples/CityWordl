package com.tecno.udemy.fernando.cityworld.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.squareup.picasso.Picasso;
import com.tecno.udemy.fernando.cityworld.R;
import com.tecno.udemy.fernando.cityworld.model.City;

import io.realm.Realm;

public class AddCityActivity extends AppCompatActivity {

    private EditText txtName, txtLink, txtDescription;
    private Button btnPreview;
    private RatingBar ratingBar;
    private ImageView imagePreview;

    private boolean isCreation;

    private City city;

    private Realm realmDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        realmDB = Realm.getDefaultInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initComponents();

        reciveCity();

        setActivityTitle();

        setUIData();

    }

    private void initComponents(){
        txtName = findViewById(R.id.editTextName);
        txtLink = findViewById(R.id.editTextLink);
        txtDescription = findViewById(R.id.editTextDescription);
        btnPreview = findViewById(R.id.btnPreview);
        ratingBar = findViewById(R.id.ratingBar);
        imagePreview = findViewById(R.id.imageViewPreview);

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage();
            }
        });
    }

    private void reciveCity(){
        Bundle bundle = getIntent().getExtras();

        if(bundle == null)
            isCreation = true;
        else{
            isCreation = false;
            int cityID = bundle.getInt("id");
            city = realmDB.where(City.class).equalTo("id", cityID).findFirst();
        }
    }

    private void setActivityTitle(){
        if(isCreation)
            setTitle("Add City");
        else
            setTitle("Edit City");
    }

    private void setUIData(){
        if(!isCreation){
            String name = city.getName();
            String description = city.getDescription();
            String link = city.getLinkBackground();
            float stars = city.getNumStarts();
            txtName.setText(name);
            txtDescription.setText(description);
            txtLink.setText(link);
            ratingBar.setRating(city.getNumStarts());
            Picasso.get().load(link).fit().into(imagePreview);
        }
    }

    /*------------------------------------------------------CRUD ACTIONS ------------------------------------*/

    private void addCity(){
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        String link = txtLink.getText().toString();
        float stars = ratingBar.getRating();

        realmDB.beginTransaction();
        City city = new City(name, description, link, stars);
        realmDB.copyToRealm(city);
        realmDB.commitTransaction();
    }

    private void editCity(){
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        String link = txtLink.getText().toString();
        float stars = ratingBar.getRating();

        realmDB.beginTransaction();
        city.setName(name);
        city.setDescription(description);
        city.setLinkBackground(link);
        city.setNumStarts(stars);
        realmDB.copyToRealmOrUpdate(city);
        realmDB.commitTransaction();
    }


    /*------------------------------------------------------MENUS ------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_activity, menu);

        if(!isCreation)
            menu.getItem(0).setIcon(android.R.drawable.ic_menu_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnuSave:
                if(isCreation) {
                    addCity();
                    finish();
                }
                else {
                    editCity();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /*-------------------------------------------------------------------------------------------------*/

    /*------------------------------------------------------Listeners ------------------------------------*/

    private void previewImage(){
        if(txtLink.getText().length() > 0) {
            String link = txtLink.getText().toString();
            Picasso.get().load(link).fit().into(imagePreview);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmDB.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
