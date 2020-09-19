package com.ivan.MoviesRDF.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Movie;
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

    String resultsNumber = "100";
    String orderBy = "numberMovies";
    Boolean asc;

    @Autowired
    JenaService jenaService;

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies(Model model) {
        // jenaService.getMovieList().stream().limit(500).forEach(System.out::println);
        model.addAttribute("movielist", jenaService.getMovieList().stream().limit(500).collect(Collectors.toList()));
        model.addAttribute("selectedMovie", jenaService.getMovie(364L));
        return "movie";
    }

    @GetMapping(value = "/production")
    public String productions(Model model, HttpServletRequest request, @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) String search) {

        Integer afterLimit = 0;
        Integer afterSortType = 0;
        HttpSession session = request.getSession();
        Boolean asc = (Boolean) session.getAttribute("asc");
        if (asc == null)
            asc = false;
        if (limit == null && session.getAttribute("limit") == null) {
            afterLimit = 100;
        }
        if (sortType == null && session.getAttribute("sortType") == null) {
            afterSortType = 3;
        }

        if (session.getAttribute("limit") != null) {
            afterLimit = (Integer) session.getAttribute("limit");
        }
        if (session.getAttribute("sortType") != null) {
            afterSortType = (Integer) session.getAttribute("sortType");

        }

        if (limit != null) {
            afterLimit = limit;
        }
        if (sortType != null) {
            afterSortType = sortType;
            session.setAttribute("asc", !asc);
        }
        session.setAttribute("limit", afterLimit);
        session.setAttribute("sortType", afterSortType);

        List<Company> companies = jenaService.getCompanyList();

        if (search != null && !search.isEmpty()) {
            companies = companies.stream().filter(e -> e.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (afterSortType == 1) {
            companies = companies.stream().sorted(Comparator.comparing(Company::getName)).collect(Collectors.toList());

        } else if (afterSortType == 2) {
            companies = companies.stream().sorted(Comparator.comparing(Company::getNumberMovies).reversed())
                    .collect(Collectors.toList());

        } else if (afterSortType == 3) {
            companies = companies.stream().sorted(Comparator.comparing(Company::getRevenue).reversed())
                    .collect(Collectors.toList());

        }
        if (asc) {
            Collections.reverse(companies);
        }
        if (afterLimit != null && afterLimit != 0) {
            companies = companies.stream().limit(afterLimit).collect(Collectors.toList());
        }

        model.addAttribute("companylist", companies);

        return "productions";
    }

    @GetMapping(value = "/about")
    public String about() {

        return "about";
    }

    @GetMapping(value = "/test")
    public String tset() {
        Long start = System.currentTimeMillis();

        List<Movie> list2 = jenaService.getMovieList();
        Long list2finish = System.currentTimeMillis();

        list2.stream().forEach(System.out::println);
        System.out.println(list2.size() + "first finish in :" + (list2finish - start));
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

        // File file = ResourceUtils.getFile("classpath:movies.ttl");
        // InputStream fileStream = new FileInputStream(file);
        // byte[] bytes = IOUtils.toByteArray(fileStream);

        // fileStream.close();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/icon", produces = { "image/png" })
    public ResponseEntity<?> icon(@RequestParam String icon) throws IOException {
        // File file = ResourceUtils.getFile("classpath:static/images/" + icon.replace("
        // ", "") + ".png");
        // InputStream fileStream = new FileInputStream(file);
        InputStream fileStream = MainController.class
                .getResourceAsStream("/static/images/" + icon.replace(" ", "") + ".png");
        BufferedInputStream bfi = new BufferedInputStream(fileStream);
        byte[] bytes = IOUtils.toByteArray(bfi);

        fileStream.close();
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}