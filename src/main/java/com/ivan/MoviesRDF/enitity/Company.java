package com.ivan.MoviesRDF.enitity;

import java.text.DecimalFormat;

public class Company {
    private String name;
    private int numberMovies;
    private long revenue;
    private String revenueString;

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
        this.revenueString = formatMoney(revenue);
    }

    public Company(String name, int numberMovies, long revenue) {
        this.name = name;
        this.numberMovies = numberMovies;
        this.revenue = revenue;
        this.revenueString = formatMoney(revenue);
    }

    private String formatMoney(Long budget) {
        DecimalFormat df = new DecimalFormat("#.#");
        String f = "";
        if (budget > 1000000000)
            f = "$ " + df.format(budget / 1000000000f) + "B";
        else if (budget > 1000000)
            f = "$ " + df.format(budget / 1000000f) + "M";
        else if (budget > 1000)
            f = "$ " + df.format(budget / 1000f) + "K";
        else if (budget == 0)
            f = "not available";
        else
            f = df.format(budget);

        return f;
    }

    public String getRevenueString() {
        return revenueString;

    }

}