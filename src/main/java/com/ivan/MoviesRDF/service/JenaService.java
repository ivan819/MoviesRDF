package com.ivan.MoviesRDF.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class JenaService {
    private static final String WBS = "http://prefix.com/#";
    private static final String DBPEDIA = "http://dbpedia.org/ontology/";
    private static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";

    private final String rdfPath = "C:\\Users\\Duck\\Desktop\\movies.ttl";

    private final String wbs = "http://prefix.com/#";
    private final String dbpedia = "http://dbpedia.org/ontology/";
    private final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    private final String xsd = "http://www.w3.org/2001/XMLSchema#";

    private final Resource genreResource;
    private final Resource productionCompanyResource;
    private final Resource productionCountryResource;
    private final Resource filmResource;
    private final Resource personResource;
    private final Resource departmentResource;
    private final Resource crewResource;
    private final Resource castResource;

    private final Property isA;
    private final Property genreProp;
    private final Property keywordProp;
    private final Property labelProp;
    private final Property producedByProp;
    private final Property producedInProp;
    private final Property budgetProp;
    private final Property homepageProp;
    private final Property languageProp;
    private final Property overviewProp;
    private final Property taglineProp;
    private final Property releaseProp;
    private final Property popularityProp;
    private final Property revenueProp;
    private final Property runtimeProp;
    private final Property idProp;

    private final Property hasCrewProp;
    private final Property hasCastProp;

    private final Property hasOrderProp;
    private final Property hasCharacterProp;

    private final Property inDepartmentProp;
    private final Property inJobProp;

    private final Property hasPersonProp;

    public static Model model = ModelFactory.createDefaultModel();

    public JenaService() {

        genreResource = model.createResource(dbpedia + "Genre");
        filmResource = model.createResource(dbpedia + "Film");
        personResource = model.createResource(dbpedia + "Person");
        departmentResource = model.createResource(dbpedia + "Department");
        productionCompanyResource = model.createResource(dbpedia + "Company");
        productionCountryResource = model.createResource(dbpedia + "Country");
        crewResource = model.createResource(wbs + "CrewMember");
        castResource = model.createResource(wbs + "CastMember");

        isA = model.createProperty(rdf + "type");
        genreProp = model.createProperty(dbpedia + "genre");
        keywordProp = model.createProperty(wbs + "keyword");
        labelProp = model.createProperty(rdfs + "label");
        producedByProp = model.createProperty(dbpedia + "producedBy");
        producedInProp = model.createProperty(wbs + "producedIn");
        budgetProp = model.createProperty(dbpedia + "budget");
        homepageProp = model.createProperty(dbpedia + "homepage");
        languageProp = model.createProperty(dbpedia, "originalLanguage");
        overviewProp = model.createProperty(rdfs, "comment");
        taglineProp = model.createProperty(dbpedia, "tagline");
        releaseProp = model.createProperty(dbpedia, "releaseDate");
        popularityProp = model.createProperty(dbpedia, "popularity");
        runtimeProp = model.createProperty(dbpedia, "runtime");
        revenueProp = model.createProperty(dbpedia, "revenue");
        idProp = model.createProperty(wbs, "id");

        hasCrewProp = model.createProperty(wbs, "hasCrew");
        hasCastProp = model.createProperty(wbs, "hasCast");

        inDepartmentProp = model.createProperty(wbs, "inDepartment");
        inJobProp = model.createProperty(wbs, "inJob");

        hasOrderProp = model.createProperty(wbs, "hasOrder");
        hasCharacterProp = model.createProperty(wbs, "hasCharacter");
        hasPersonProp = model.createProperty(wbs, "hasPerson");

        Arrays.asList("movies").stream().forEach(e -> {
            // try {
            // File file = ResourceUtils.getFile("classpath:" + e + ".ttl");
            // InputStream fileStream = new FileInputStream(file);
            InputStream fileStream = JenaService.class.getResourceAsStream("/" + e + ".ttl");
            BufferedInputStream bfi = new BufferedInputStream(fileStream);
            model.read(bfi, null, "TURTLE");

            try {
                bfi.close();
                fileStream.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // } catch (IOException e1) {
            // // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
        });

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

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString);

        ResultSet results = qexec.execSelect();

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

    public List<CastMember> getCastMembers2(Long movieId) {
        List<CastMember> resultList = new ArrayList<>();

        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCast") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        // queryString += "?cast " + stringXML(RDF, "type") + " " + stringXML(WBS,
        // "CastMember") + " . ";
        // queryString += "?cast " + stringXML(WBS, "hasPerson") + " ?person . ";
        queryString += "} ";

        // System.out.println(queryString);
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
        return resultList;
    }

    public List<CastMember> getCastMembers3(Long movieId) {
        List<CastMember> resultList = new ArrayList<>();

        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCast") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "?cast " + stringXML(RDF, "type") + " " + stringXML(WBS, "CastMember") + " . ";
        queryString += "?cast " + stringXML(WBS, "hasPerson") + " ?person . ";
        queryString += "} ";

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString);

        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Resource cast = soln.getResource("cast");
            Resource person = soln.getResource("person");

            Long id = person.getProperty(idProp).getLong();
            String name = person.getProperty(labelProp).getString();
            CastMember m = new CastMember(id, name);
            m.setOrder(cast.getProperty(hasOrderProp).getInt());
            m.setcCharacter(cast.getProperty(hasCharacterProp).getString());

            resultList.add(m);

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
            }

            m.setPopularity(movie.getProperty(popularityProp).getFloat());

            // m.setCastMembers(getCastMembers2(movie.getProperty(idProp).getLong()));
            // m.setGenres(getGenreList(movie.getProperty(idProp).getLong()));

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