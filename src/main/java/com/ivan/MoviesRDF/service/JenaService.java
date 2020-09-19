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

    public String currentDataset = "movies";

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

        setupModel(currentDataset);

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

        QueryExecution qexec = executeQuery(queryString, "movies");
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

        QueryExecution qexec = executeQuery(queryString, "movies");
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

        QueryExecution qexec = executeQuery(queryString, "movies");
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
        QueryExecution qexec = executeQuery(queryString, "movies");
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

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString, "cast");

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

    public List<CrewMember> getCrewMembers(Long movieId) {
        List<CrewMember> resultList = new ArrayList<>();

        String queryString = "";
        queryString += "SELECT ?cast ?person ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + " . ";
        queryString += "?movie " + stringXML(WBS, "hasCrew") + " ?cast . ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString, "cast");

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

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString, "cast");

        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();

            Resource cast = soln.getResource("cast");

            Company c = new Company(cast.getProperty(labelProp).getString(), 0, 0);

            resultList.add(c);

        }

        qexec.close();

        return resultList;
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

        // System.out.println(queryString);
        QueryExecution qexec = executeQuery(queryString, "cast");

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
        QueryExecution qexec = executeQuery(queryString, "cast");
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
        String queryString = "";
        queryString += "SELECT ?movie ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + ". ";
        queryString += "?movie " + stringXML(WBS, "id") + " " + movieId + " . ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString, "movies");
        ResultSet results = qexec.execSelect();

        QuerySolution soln = results.nextSolution();
        Resource movie = soln.getResource("movie");

        System.out.println(movie.getProperty(hasCastProp));

        Movie m = new Movie(movieId, movie.getProperty(labelProp).getString());

        if (movie.getProperty(releaseProp) != null) {
            m.setReleaseDate(movie.getProperty(releaseProp).getString());
            System.out.println("werein");
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

    // TODO overloaded
    public List<Movie> getMovieList() {
        List<Movie> resultList = new ArrayList<>();
        String queryString = "";
        queryString += "SELECT ?movie ";
        queryString += "WHERE { ";
        queryString += "?movie " + stringXML(RDF, "type") + " " + stringXML(DBPEDIA, "Film") + ". ";
        queryString += "} ";

        QueryExecution qexec = executeQuery(queryString, "movies");
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
                System.out.println("werein");
            }

            m.setPopularity(movie.getProperty(popularityProp).getFloat());
            m.setCastMembers(getCastMembers(movie.getProperty(idProp).getLong()));
            m.setGenres(getGenreList(movie.getProperty(idProp).getLong()));

            resultList.add(m);
        }

        qexec.close();

        return resultList;
    }

    private QueryExecution executeQuery(String queryString, String dataset) {
        // if (!dataset.equals(currentDataset)) {
        // model.close();
        // setupModel(dataset);
        // currentDataset = dataset;
        // }

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec;
    }

    private void setupModel(String dataset) {

        model = ModelFactory.createDefaultModel();
        if (dataset.equals("movies")) {
            InputStream fileStream = JenaService.class.getResourceAsStream("/movies1.ttl");
            BufferedInputStream bfi = new BufferedInputStream(fileStream);
            model.read(bfi, null, "TURTLE");
            try {
                bfi.close();
                fileStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        InputStream fileStream = JenaService.class.getResourceAsStream("/" + dataset + ".ttl");
        BufferedInputStream bfi = new BufferedInputStream(fileStream);
        model.read(bfi, null, "TURTLE");
        try {
            bfi.close();
            fileStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private String stringXML(String prefix, String object) {
        return "<" + prefix + object + ">";
    }
}