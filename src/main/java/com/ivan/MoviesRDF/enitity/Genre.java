package com.ivan.MoviesRDF.enitity;

public class Genre {
    private String name;
    private int numberMovies;

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

    public Genre(String name, int numberMovies) {
        this.name = name;
        this.numberMovies = numberMovies;
    }
}