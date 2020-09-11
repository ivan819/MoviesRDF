package com.ivan.MoviesRDF.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.service.JenaService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    JenaService jenaService;

    @GetMapping(value = "/genres1")
    @ResponseBody
    public List<Genre> getGenres(@RequestParam(required = false) Long movieId) {
        if (movieId == null)
            return jenaService.getGenreList();
        else
            return jenaService.getGenreList(movieId);
    }

    @GetMapping(value = "/companies")
    @ResponseBody
    public List<Company> getCompanies() {
        return jenaService.getCompanyList().stream().sorted(Comparator.comparing(Company::getRevenue).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/member")
    @ResponseBody
    public Member getMember(@RequestParam Long memberId) {
        return jenaService.getMember(memberId);
    }

    @GetMapping(value = "/cast")
    @ResponseBody
    public List<CastMember> getCast(@RequestParam Long movieId) {
        return jenaService.getCastMembers(movieId).stream().sorted(Comparator.comparing(CastMember::getOrder))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/crew")
    @ResponseBody
    public List<CrewMember> getCrew(@RequestParam Long movieId) {
        return jenaService.getCrewMembers(movieId).stream().collect(Collectors.toList());
    }

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/app")
    public String movies() {

        return "app";
    }

    @GetMapping(value = "/genres")
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