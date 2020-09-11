package com.ivan.MoviesRDF.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.service.JenaService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    JenaService jenaService;

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies(Model model) {

        return "movie";
    }

    @GetMapping(value = "/production")
    public String productions(Model model) {
        model.addAttribute("companylist", jenaService.getCompanyList().stream()
                .sorted(Comparator.comparing(Company::getNumberMovies).reversed()).collect(Collectors.toList()));

        return "productions";
    }

    @GetMapping(value = "/about")
    public String about() {

        return "about";
    }

    @GetMapping(value = "/genre")
    public String genres(Model model) {
        model.addAttribute("genrelist", jenaService.getGenreList().stream()
                .sorted(Comparator.comparing(Genre::getNumberMovies).reversed()).collect(Collectors.toList()));
        return "genres";
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

    @ResponseBody
    @GetMapping(value = "/icon", produces = { "image/png" })
    public ResponseEntity<?> icon(@RequestParam String icon) throws IOException {
        File file = ResourceUtils.getFile("classpath:static/images/" + icon.replace(" ", "") + ".png");
        InputStream fileStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fileStream);
        System.out.println("loaded and returned");

        fileStream.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}