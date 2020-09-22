package com.ivan.MoviesRDF.rest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Movie;
import com.ivan.MoviesRDF.service.FilterService;
import com.ivan.MoviesRDF.service.JenaService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    // String resultsDefault = "100";
    // String orderBy = "numberMovies";
    // Boolean asc;

    @Autowired
    JenaService jenaService;

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies(Model model, @RequestParam(required = false) Long movieId) {
        // jenaService.getMovieList().stream().limit(500).forEach(System.out::println);
        model.addAttribute("movielist", jenaService.getMovieList().stream().limit(500)
                .sorted(Comparator.comparing(Movie::getPopularity).reversed()).collect(Collectors.toList()));

        if (movieId != null)
            model.addAttribute("selectedMovie", jenaService.getMovie(movieId));
        return "movie";
    }

    @GetMapping(value = "/production")
    public String productions(Model model, HttpServletRequest request, @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer sortType, @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer clear) {

        HttpSession session = request.getSession();

        if (clear != null && clear == 1) {
            session.removeAttribute("asc");
            session.removeAttribute("limit");
            session.removeAttribute("sortType");
            session.removeAttribute("search");
        }

        // session
        Boolean ascSession = (Boolean) session.getAttribute("asc");
        Integer limitSession = (Integer) session.getAttribute("limit");
        Integer sortTypeSession = (Integer) session.getAttribute("sortType");
        String searchSession = (String) session.getAttribute("search");

        // defaults
        if (ascSession == null)
            ascSession = false;
        if (limitSession == null)
            limitSession = 100;
        if (sortTypeSession == null)
            sortTypeSession = 3;

        // query
        if (search != null)
            searchSession = search;
        if (limit != null)
            limitSession = limit;
        if (sortType != null) {
            sortTypeSession = sortType;
            session.setAttribute("asc", !ascSession);
        }

        session.setAttribute("limit", limitSession);
        session.setAttribute("sortType", sortTypeSession);
        session.setAttribute("search", searchSession);

        model.addAttribute("companylist", FilterService.getCompanyFilter(jenaService.getCompanyList())
                .filter(searchSession).order(sortTypeSession, ascSession).limit(limitSession).getList());

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