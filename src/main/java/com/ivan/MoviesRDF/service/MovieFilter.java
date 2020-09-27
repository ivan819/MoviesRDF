package com.ivan.MoviesRDF.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.Movie;

public class MovieFilter {
    private List<Movie> movies;

    JenaService jenaService;

    private MovieFilter(List<Movie> movies) {
        this.movies = movies;
        this.jenaService = new JenaService();
    }

    public static MovieFilter getFilter(List<Movie> movies) {
        MovieFilter fs = new MovieFilter(movies);
        return fs;
    }

    public MovieFilter filter(String s) {
        if (s != null && !s.isEmpty()) {

            this.movies = this.movies.stream().filter(e -> {

                if (e.getTitle().toLowerCase().contains(s.toLowerCase())
                        || e.getOverview().toLowerCase().contains(s.toLowerCase())
                        || e.getTagline().toLowerCase().contains(s.toLowerCase())
                        || jenaService.getMovie(e.getId()).getKeywords().stream()
                                .anyMatch(ee -> ee.toLowerCase().contains(s.toLowerCase()))
                        || jenaService.getMovie(e.getId()).getCastMembers().stream()
                                .anyMatch(ee -> ee.getName().toLowerCase().contains(s.toLowerCase()))
                        || jenaService.getMovie(e.getId()).getCrewMembers().stream()
                                .anyMatch(ee -> ee.getName().toLowerCase().contains(s.toLowerCase()))
                        || jenaService.getMovie(e.getId()).getProductionCompanies().stream()
                                .anyMatch(ee -> ee.getName().toLowerCase().contains(s.toLowerCase()))
                        || jenaService.getMovie(e.getId()).getProductionCountries().stream()
                                .anyMatch(ee -> ee.toLowerCase().contains(s.toLowerCase()))
                        || jenaService.getMovie(e.getId()).getGenres().stream()
                                .anyMatch(ee -> ee.getName().equals(s))) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());

        }

        return this;
    }

    public MovieFilter filterGenre(String s) {
        if (s != null && !s.isEmpty()) {

            this.movies = this.movies.stream()
                    .filter(e -> e.getGenres().stream().anyMatch(ee -> ee.getName().equals(s)))
                    .collect(Collectors.toList());
        }

        return this;
    }

    public MovieFilter filterCompany(String s) {
        if (s != null && !s.isEmpty()) {

            this.movies = this.movies.stream().filter(e -> jenaService.getMovie(e.getId()).getProductionCompanies()
                    .stream().anyMatch(ee -> ee.getName().equals(s))).collect(Collectors.toList());
        }

        return this;
    }

    public MovieFilter filterCountry(String s) {
        if (s != null && !s.isEmpty()) {

            this.movies = this.movies.stream().filter(
                    e -> jenaService.getMovie(e.getId()).getProductionCountries().stream().anyMatch(ee -> ee.equals(s)))
                    .collect(Collectors.toList());
        }

        return this;
    }

    public MovieFilter order(int orderType, boolean asc) {
        Comparator<Movie> c;

        switch (orderType) {
            case 1:
                c = Comparator.comparing(Movie::getTitle, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 2:
                c = Comparator.comparing(Movie::getBudget, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 3:
                c = Comparator.comparing(Movie::getRevenue, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 4:
                c = Comparator.comparing(Movie::getPopularity, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 5:
                c = Comparator.comparing(Movie::getReleaseDate, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 6:
                c = Comparator.comparing(Movie::getRuntime, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            default:
                c = Comparator.comparing(Movie::getPopularity, Comparator.nullsFirst(Comparator.naturalOrder()));
        }

        if (!asc)
            c = c.reversed();

        try {
            this.movies = this.movies.stream().sorted(c).collect(Collectors.toList());
            // this.movies.stream().map(Movie::getRuntime).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e);
        }

        return this;

    }

    public MovieFilter limit(Integer limit) {
        if (limit != 0)
            this.movies = this.movies.stream().limit(limit).collect(Collectors.toList());
        return this;
    }

    public List<Movie> get() {
        return this.movies;
    }

}
