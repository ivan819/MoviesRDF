package com.ivan.MoviesRDF.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.enitity.Movie;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Repository;

@Repository
public class JenaService extends JenaServiceData {

    public List<Genre> getGenreList() {
        List<Genre> resultList = new ArrayList<>();
        String queryString = "SELECT ?label (count(?movie) as ?count)";
        queryString += "WHERE { ";
        queryString += "?genre " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Genre") + " . ";
        queryString += "?genre " + stringXML(RDFS, "label") + " ?label . ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(DBPEDIA, "genre") + " ?genre . ";
        queryString += "} GROUP BY ?label";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode label = soln.get("label");
            Literal l = soln.getLiteral("count");
            resultList.add(new Genre(label.toString(), l.getInt()));
        }
        qexec.close();
        return resultList;
    }

    public List<Genre> getGenreList(Long movieId) {
        List<Genre> resultList = new ArrayList<>();
        String queryString = "SELECT ?label ";
        queryString += "WHERE { ";
        queryString += "?genre " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Genre") + " . ";
        queryString += "?genre " + stringXML(RDFS, "label") + " ?label . ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "?movie " + stringXML(DBPEDIA, "genre") + " ?genre . ";
        queryString += "}";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode label = soln.get("label");
            resultList.add(new Genre(label.toString(), -1));
        }
        qexec.close();
        return resultList;
    }

    public List<Company> getCompanyList() {
        List<Company> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?label (count(?movie) as ?count) (sum(?revenue) as ?sum) ";
        queryString += "WHERE { ";
        queryString += "?company " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Company") + " . ";
        queryString += "?company " + stringXML(RDFS, "label") + " ?label . ";
        queryString += "?movie " + stringXML(DBPEDIA, "producedBy") + " ?company . ";
        queryString += "?movie " + stringXML(DBPEDIA, "revenue") + " ?revenue ";
        queryString += "} GROUP BY ?label";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode label = soln.get("label");
            Literal l = soln.getLiteral("count");
            Literal sum = soln.getLiteral("sum");
            resultList.add(new Company(label.toString(), l.getInt(), sum.getLong()));
        }
        qexec.close();
        return resultList;
    }

    public List<Company> getCompanyList(Long movieId) {
        List<Company> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?cast ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(DBPEDIA, "producedBy") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource cast = soln.getResource("cast");
            try {
                Company c = new Company(cast.getProperty(labelProp).getString(), 0, 0);
                resultList.add(c);
            } catch (Exception exception) {

            }

        }
        qexec.close();
        return resultList;
    }

    public Member getMember(Long id) {
        String queryString = "";
        queryString += "SELECT ?id ?label ";
        queryString += "WHERE { ";
        queryString += "?member " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Person") + ". ";
        queryString += "?member " + stringXML(RDFS, "label") + " ?label . ";
        queryString += "?member " + stringXML(WBS, "id") + " " + id + " . ";
        queryString += "?member " + stringXML(WBS, "id") + " ?id . ";
        queryString += "} ";
        Member member = null;
        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode label = soln.get("label");
            Literal idd = soln.getLiteral("id");
            member = new Member(idd.getLong(), label.toString());
        }
        qexec.close();
        return member;
    }

    public List<CastMember> getCastMembers(Long movieId) {
        List<CastMember> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCast") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();

            Resource cast = soln.getResource("cast");
            Resource person = cast.getProperty(hasPersonProp).getResource();

            Long id = person.getProperty(idProp).getLong();
            String name = person.getProperty(labelProp).getString();
            CastMember m = new CastMember(id, name);
            m.setOrder(cast.getProperty(hasOrderProp).getInt());
            m.setcCharacter(cast.getProperty(hasCharacterProp).getString());
            resultList.add(m);
        }
        qexec.close();
        return resultList.stream().sorted(Comparator.comparing(CastMember::getOrder)).collect(Collectors.toList());

    }

    public List<CrewMember> getCrewMembers(Long movieId) {
        List<CrewMember> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCrew") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource cast = soln.getResource("cast");
            Statement s = cast.getProperty(hasPersonProp);
            Resource person = s.getResource();
            Long id = person.getProperty(idProp).getLong();
            String name = person.getProperty(labelProp).getString();
            CrewMember m = new CrewMember(id, name);
            m.setDepartment(cast.getProperty(inDepartmentProp).getResource().getProperty(labelProp).getString());
            m.setJob(cast.getProperty(inJobProp).getString());
            resultList.add(m);
        }
        qexec.close();
        return resultList.stream().sorted(Comparator.comparing(CrewMember::getDepartment)).collect(Collectors.toList());
    }

    public List<String> getCountryList(Long movieId) {
        List<String> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?cast ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "producedIn") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";
        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource cast = soln.getResource("cast");
            resultList.add(cast.getProperty(labelProp).getString());
        }
        qexec.close();
        return resultList;
    }

    public List<String> getKeywords(Long movieId) {
        List<String> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "keyword") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";
        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Literal cast = soln.getLiteral("cast");
            resultList.add(cast.toString());
        }
        qexec.close();
        return resultList;
    }

    public Movie getMovie(Long movieId) {
        if (movieId == 0) {
            Random r = new Random();
            List<Movie> list = getMovieList();
            movieId = list.get(r.nextInt(list.size())).getId();

        }
        String queryString = "";
        queryString += "SELECT ?movie ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + ". ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";
        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        QuerySolution soln = results.nextSolution();
        Resource movie = soln.getResource("movie");

        Movie m = new Movie(movieId, movie.getProperty(labelProp).getString());

        if (movie.getProperty(releaseProp) != null) {
            m.setReleaseDate(movie.getProperty(releaseProp).getString());
        }

        m.setTagline(movie.getProperty(taglineProp).getString());
        m.setPopularity(movie.getProperty(popularityProp).getFloat());
        m.setBudget(movie.getProperty(budgetProp).getLong());
        m.setRevenue(movie.getProperty(revenueProp).getLong());
        m.setHomepage(movie.getProperty(homepageProp).getString());
        m.setOverview(movie.getProperty(overviewProp).getString());
        m.setRuntime(movie.getProperty(runtimeProp).getInt());
        m.setOriginalLanguage(movie.getProperty(languageProp).getString());

        m.setGenres(getGenreList(movieId));
        m.setCastMembers(getCastMembers(movieId));
        m.setCrewMembers(getCrewMembers(movieId));
        m.setKeywords(getKeywords(movieId));
        m.setProductionCompanies(getCompanyList(movieId));
        m.setProductionCountries(getCountryList(movieId));

        qexec.close();
        return m;
    }

    public List<Movie> getMovieList() {
        List<Movie> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?movie ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + ". ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString);
        ResultSet results = qexec.execSelect();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource movie = soln.getResource("movie");
            Movie m = new Movie(movie.getProperty(idProp).getLong(), movie.getProperty(labelProp).getString());
            m.setTagline(movie.getProperty(taglineProp).getString());
            if (movie.getProperty(releaseProp) != null) {
                m.setReleaseDate(movie.getProperty(releaseProp).getString());
            } else {
                m.setReleaseDate("");
            }
            m.setPopularity(movie.getProperty(popularityProp).getFloat());
            m.setCastMembers(getCastMembers(movie.getProperty(idProp).getLong()));
            m.setGenres(getGenreList(movie.getProperty(idProp).getLong()));
            m.setOverview(movie.getProperty(overviewProp).getString());
            m.setRuntime(movie.getProperty(runtimeProp).getInt());
            m.setBudget(movie.getProperty(budgetProp).getLong());
            m.setRevenue(movie.getProperty(revenueProp).getLong());
            resultList.add(m);
        }
        qexec.close();
        return resultList;
    }

    private QueryExecution executeQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec;
    }

    private String stringXML(String prefix, String object) {
        return "<" + prefix + object + ">";
    }
}