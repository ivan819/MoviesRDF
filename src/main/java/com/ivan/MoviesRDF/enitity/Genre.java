package com.ivan.MoviesRDF.enitity;

import java.io.Serializable;

public class Genre implements Serializable {
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

    public Genre(String name, int l) {
        this.name = name;
        this.numberMovies = l;
    }

    @Override
    public String toString() {
        return "Genre [name=" + name + "]";
    }

    public Genre(String name) {
        this.name = name;
    }

}