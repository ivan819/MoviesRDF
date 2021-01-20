package com.ivan.MoviesRDF.service;

import java.util.List;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.enitity.Movie;

import org.springframework.stereotype.Repository;

public interface MoviesRepository {

    public List<Genre> getGenreList();

    public List<Genre> getGenreList(Long movieId);

    public List<Company> getCompanyList();

    public List<Company> getCompanyList(Long movieId);

    public Member getMember(Long id);

    public List<CastMember> getCastMembers(Long movieId);

    public List<CrewMember> getCrewMembers(Long movieId);

    public List<String> getCountryList(Long movieId);

    public List<String> getKeywords(Long movieId);

    public Movie getMovie(Long movieId);

    public List<Movie> getMovieList();

}