package com.ivan.MoviesRDF.enitity;

public class Category<T> {
    private T category;

    public Category() {
    }

    public Category(T category) {

        this.category = category;
    }

    public T getCategory() {
        return category;
    }

    public void setCategory(T category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.category.equals(((Category<T>) obj).getCategory());
    }

    @Override
    public int hashCode() {
        return this.category.hashCode();
    }
}