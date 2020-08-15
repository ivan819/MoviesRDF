package com.ivan.MoviesRDF.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.service.JenaService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    JenaService jenaService;

    @GetMapping(value = "/genres")
    @ResponseBody
    public List<Genre> getTest1Data() {

        return jenaService.getGenreList();
    }

    @GetMapping(value = "/companies")
    @ResponseBody
    public List<Company> getTest11Data() {
        return jenaService.getCompanyList().stream().sorted(Comparator.comparing(Company::getRevenue).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/member/{id}")
    @ResponseBody
    public Member getTest112Data(@PathVariable Long id) {
        return jenaService.getMember(id);
    }

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies() {

        return "index2";
    }

    @ResponseBody
    @GetMapping(value = "/data", produces = { "text/turtle" })
    public ResponseEntity<?> data() throws IOException {
        File file = ResourceUtils.getFile("classpath:movies.ttl");
        InputStream fileStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fileStream);
        System.out.println("loaded and returned");

        fileStream.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}