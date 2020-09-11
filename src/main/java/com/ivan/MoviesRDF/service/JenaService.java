package com.ivan.MoviesRDF.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class JenaService {
    private static final String WBS = "http://prefix.com/#";
    private static final String DBPEDIA = "http://dbpedia.org/ontology/";
    private static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";

    static Model model = ModelFactory.createDefaultModel();

    public JenaService() {
        try {
            File file = ResourceUtils.getFile("classpath:movies.ttl");
            InputStream fileStream = new FileInputStream(file);
            model.read(fileStream, null, "TURTLE");
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    // TODO
    public List<CastMember> getCastMembers(Long movieId) {
        List<CastMember> resultList = new ArrayList<>();

        String queryString = "";
        queryString += "SELECT ?cast ?id ?name ?character ?order ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCast") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "?cast " + stringXML(RDF, "type") + " " + stringXML(WBS, "CastMember") + " . ";
        queryString += "?cast " + stringXML(WBS, "hasPerson") + " ?person . ";

        queryString += "?person " + stringXML(RDFS, "label") + " ?name . ";
        queryString += "?person " + stringXML(WBS, "id") + " ?id . ";

        queryString += "?cast " + stringXML(WBS, "hasOrder") + " ?order . ";
        queryString += "?cast " + stringXML(WBS, "hasCharacter") + " ?character . ";
        queryString += "} ";

        System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString);

        ResultSet results = qexec.execSelect();
        System.out.println();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();

            Literal id = soln.getLiteral("id");
            Literal name = soln.getLiteral("name");
            Literal character = soln.getLiteral("character");
            Literal order = soln.getLiteral("order");

            resultList.add(new CastMember(id.getLong(), name.getString(), order.getInt(), character.getString()));
        }

        qexec.close();
        return resultList;
    }

    public List<CrewMember> getCrewMembers(Long movieId) {
        List<CrewMember> resultList = new ArrayList<>();

        String queryString = "";
        queryString += "SELECT ?id ?deptname ?job ?name ";
        queryString += " WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCrew") + " ?crew . ";
        queryString += "?crew " + stringXML(RDF, "type") + " " + stringXML(WBS, "CrewMember") + " . ";
        queryString += "?crew " + stringXML(WBS, "hasPerson") + " ?person . ";

        // queryString += "?person " + stringXML(RDFS, "label") + " ?name . ";
        // queryString += "?person " + stringXML(WBS, "id") + " ?id . ";

        queryString += "?crew " + stringXML(WBS, "inJob") + " ?job . ";
        queryString += "?crew " + stringXML(WBS, "inDepartment") + " ?dept . ";
        queryString += "?dept " + stringXML(RDFS, "label") + " ?deptname . ";
        queryString += "} ";
        System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString);

        ResultSet results = qexec.execSelect();
        System.out.println();
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();

            System.out.println(soln.getResource("person"));
            // System.out.println(soln.getLiteral("name"));

            System.out.println(soln.getLiteral("job"));
            System.out.println(soln.getLiteral("deptname"));
            // Literal id = soln.getLiteral("id");
            // Literal name = soln.getLiteral("name");
            Literal dept = soln.getLiteral("deptname");
            Literal job = soln.getLiteral("job");

            resultList.add(new CrewMember(1L, "name.getString()", dept.getString(), job.getString()));
            // resultList.add(new CrewMember(id.getLong(), name.getString(),
            // dept.getString(), job.getString()));
        }

        qexec.close();
        return resultList;
    }

    public List<String> getKeywords(Long movieId) {
        return null;
    }

    public Movie getMovie(Long movieId) {
        return null;
    }

    // TODO overloaded
    public List<Movie> getMovieList() {
        return null;
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