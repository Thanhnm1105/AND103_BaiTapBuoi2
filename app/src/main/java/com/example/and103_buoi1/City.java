package com.example.and103_buoi1;

public class City {
    private String id;
    private String fullName;
    private String country;
    private int population;

    public City() {
    }

    public City(String fullName, String country, int population) {
        this.fullName = fullName;
        this.country = country;
        this.population = population;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
