package com.ivan.MoviesRDF.service;

import java.util.List;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.enitity.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    MoviesRepository movieRepository;

    public List<Genre> getGenreList() {
        return movieRepository.getGenreList();
    }

    public List<Genre> getGenreList(Long movieId) {
        return movieRepository.getGenreList(movieId);
    }

    public List<Company> getCompanyList() {
        return movieRepository.getCompanyList();
    }

    public List<Company> getCompanyList(Long movieId) {
        return movieRepository.getCompanyList(movieId);
    }

    public Member getMember(Long id) {
        return movieRepository.getMember(id);
    }

    public List<CastMember> getCastMembers(Long movieId) {
        return movieRepository.getCastMembers(movieId);
    }

    public List<CrewMember> getCrewMembers(Long movieId) {
        return movieRepository.getCrewMembers(movieId);
    }

    public List<String> getCountryList(Long movieId) {
        return movieRepository.getCountryList(movieId);
    }

    public List<String> getKeywords(Long movieId) {
        return movieRepository.getKeywords(movieId);
    }

    public Movie getMovie(Long movieId) {
        return movieRepository.getMovie(movieId);
    }

    public List<Movie> getMovieList() {
        return movieRepository.getMovieList();
    }
}