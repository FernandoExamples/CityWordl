package com.tecno.udemy.fernando.cityworld.model;

import com.tecno.udemy.fernando.cityworld.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class City extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String name;

    private String description;

    @Required
    private String linkBackground;

    private float numStarts;

    public City() {
    }

    public City(String name, String description, String linkBackground, float numStarts) {
        this.id = MyApplication.CityID.incrementAndGet();
        this.name = name;
        this.description = description;
        this.linkBackground = linkBackground;
        this.numStarts = numStarts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkBackground() {
        return linkBackground;
    }

    public void setLinkBackground(String linkBackground) {
        this.linkBackground = linkBackground;
    }

    public float getNumStarts() {
        return numStarts;
    }

    public void setNumStarts(float numStarts) {
        this.numStarts = numStarts;
    }
}
