package com.ivan.MoviesRDF.enitity;

public class Company {
    private String name;
    private int numberMovies;
    private long revenue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberMovies() {
        return numberMovies;
    }

    public void setNumberMovies(int numberMovies) {
        this.numberMovies = numberMovies;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public Company(String name, int numberMovies, long revenue) {
        this.name = name;
        this.numberMovies = numberMovies;
        this.revenue = revenue;
    }

}