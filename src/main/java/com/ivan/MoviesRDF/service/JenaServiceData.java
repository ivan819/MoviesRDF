package com.ivan.MoviesRDF.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class JenaServiceData {
    static final String WBS = "http://prefix.com/#";
    static final String DBPEDIA = "http://dbpedia.org/ontology/";
    static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    static final String XSD = "http://www.w3.org/2001/XMLSchema#";

    static final Resource genreResource;
    static final Resource productionCompanyResource;
    static final Resource productionCountryResource;
    static final Resource filmResource;
    static final Resource personResource;
    static final Resource departmentResource;
    static final Resource crewResource;
    static final Resource castResource;

    static final Property isA;
    static final Property genreProp;
    static final Property keywordProp;
    static final Property labelProp;
    static final Property producedByProp;
    static final Property producedInProp;
    static final Property budgetProp;
    static final Property homepageProp;
    static final Property languageProp;
    static final Property overviewProp;
    static final Property taglineProp;
    static final Property releaseProp;
    static final Property popularityProp;
    static final Property revenueProp;
    static final Property runtimeProp;
    static final Property idProp;
    static final Property hasCrewProp;
    static final Property hasCastProp;
    static final Property hasOrderProp;
    static final Property hasCharacterProp;
    static final Property inDepartmentProp;
    static final Property inJobProp;
    static final Property hasPersonProp;

    public static Model model = ModelFactory.createDefaultModel();

    static {

        genreResource = model.createResource(DBPEDIA + "Genre");
        filmResource = model.createResource(DBPEDIA + "Film");
        personResource = model.createResource(DBPEDIA + "Person");
        departmentResource = model.createResource(DBPEDIA + "Department");
        productionCompanyResource = model.createResource(DBPEDIA + "Company");
        productionCountryResource = model.createResource(DBPEDIA + "Country");
        crewResource = model.createResource(WBS + "CrewMember");
        castResource = model.createResource(WBS + "CastMember");

        isA = model.createProperty(RDFS + "type");
        genreProp = model.createProperty(DBPEDIA + "genre");
        keywordProp = model.createProperty(WBS + "keyword");
        labelProp = model.createProperty(RDFS + "label");
        producedByProp = model.createProperty(DBPEDIA + "producedBy");
        producedInProp = model.createProperty(WBS + "producedIn");
        budgetProp = model.createProperty(DBPEDIA + "budget");
        homepageProp = model.createProperty(DBPEDIA + "homepage");
        languageProp = model.createProperty(DBPEDIA, "originalLanguage");
        overviewProp = model.createProperty(RDFS, "comment");
        taglineProp = model.createProperty(DBPEDIA, "tagline");
        releaseProp = model.createProperty(DBPEDIA, "releaseDate");
        popularityProp = model.createProperty(DBPEDIA, "popularity");
        runtimeProp = model.createProperty(DBPEDIA, "runtime");
        revenueProp = model.createProperty(DBPEDIA, "revenue");
        idProp = model.createProperty(WBS, "id");
        hasCrewProp = model.createProperty(WBS, "hasCrew");
        hasCastProp = model.createProperty(WBS, "hasCast");
        inDepartmentProp = model.createProperty(WBS, "inDepartment");
        inJobProp = model.createProperty(WBS, "inJob");
        hasOrderProp = model.createProperty(WBS, "hasOrder");
        hasCharacterProp = model.createProperty(WBS, "hasCharacter");
        hasPersonProp = model.createProperty(WBS, "hasPerson");

        setupModel();
    }

    private static void setupModel() {
        model = ModelFactory.createDefaultModel();
        InputStream fileStream = JenaServiceData.class.getResourceAsStream("/movies.ttl");
        BufferedInputStream bfi = new BufferedInputStream(fileStream);
        model.read(bfi, null, "TURTLE");
        try {
            bfi.close();
            fileStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
