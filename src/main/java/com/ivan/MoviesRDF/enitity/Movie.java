package com.ivan.MoviesRDF.enitity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Movie {

    private Long id;
    private String title;
    private Long budget;
    private Long revenue;
    private String homepage;
    private String originalLanguage;
    private String overview;
    private String tagline;
    private Float popularity;
    private String releaseDate;
    private Integer runtime;

    private List<Genre> genres;
    private List<String> keywords;
    private List<Company> productionCompanies;
    private List<String> productionCountries;

    private List<CrewMember> crewMembers;
    private List<CastMember> castMembers;

    private String budgetString;
    private String revenueString;

    public Movie() {
    }

    public Movie(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie [genres=" + genres + ", id=" + id + ", title=" + title + " : " + releaseDate + "]";
    }

    public Long getBudget() {
        return budget;
    }

    public String getBudgetString() {
        return budgetString;
    }

    public void setBudget(Long budget) {

        this.budgetString = formatMoney(budget);
        this.budget = budget;
    }

    private String formatMoney(Long budget) {
        return String.format("%d%s $", budget > 1000000 ? budget / 1000000 : budget / 1000,
                budget > 1000000 ? "M" : "K");
    }

    public Long getRevenue() {
        return revenue;
    }

    public String getRevenueString() {
        return revenueString;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
        this.revenueString = formatMoney(revenue);
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Integer getPopularity() {
        return Math.round(popularity);
    }

    public void setPopularity(Float popularity) {
        if (popularity == null) {
            this.popularity = 0f;
        }
        this.popularity = popularity;
    }

    public Date getReleaseDate() {
        DateFormat d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            return d.parse(this.releaseDate);
        } catch (ParseException e) {

        }
        return new Date();
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<CrewMember> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public List<CastMember> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(List<CastMember> castMembers) {
        this.castMembers = castMembers;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Movie) obj).getId());
    }

    @Override
    public int hashCode() {
        if (this.id == null)
            id = -1L;
        return this.id.hashCode();
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<Company> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<Company> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<String> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<String> productionCountries) {
        this.productionCountries = productionCountries;
    }

}