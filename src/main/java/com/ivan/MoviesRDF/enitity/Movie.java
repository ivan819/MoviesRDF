package com.ivan.MoviesRDF.enitity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private List<Category> genres;
    private List<Category> keywords;
    private List<Category> productionCompanies;
    private List<Category> productionCountries;

    private List<CrewMember> crewMembers;
    private List<CastMember> castMembers;

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

    public List<Category> getGenres() {
        return genres;
    }

    public void setGenres(List<Category> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Movie [genres=" + genres + ", id=" + id + ", title=" + title + "]";
    }

    public List<Category> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Category> keywords) {
        this.keywords = keywords;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
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

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public Date getReleaseDate() {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return d.parse(this.releaseDate);
        } catch (ParseException e) {

        }
        return null;
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

    public List<Category> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<Category> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<Category> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<Category> productionCountries) {
        this.productionCountries = productionCountries;
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

}