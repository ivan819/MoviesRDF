package com.ivan.MoviesRDF.service;

import java.util.List;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.enitity.Movie;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SparkRepository implements MoviesRepository {

    @Autowired
    SparkSession sparkSession;

    @Override
    public List<Genre> getGenreList() {
        Dataset<Row> dataset = sparkSession.read().parquet("C:\\Users\\Duck\\Desktop\\testpni\\genres.parquet");

        dataset.explain();
        return null;
    }

    @Override
    public List<Genre> getGenreList(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Company> getCompanyList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Company> getCompanyList(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Member getMember(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CastMember> getCastMembers(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CrewMember> getCrewMembers(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getCountryList(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getKeywords(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Movie getMovie(Long movieId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Movie> getMovieList() {
        // TODO Auto-generated method stub
        return null;
    }

}
