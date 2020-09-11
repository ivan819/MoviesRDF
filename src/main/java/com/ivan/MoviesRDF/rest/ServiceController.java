package com.ivan.MoviesRDF.rest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.CastMember;
import com.ivan.MoviesRDF.enitity.Company;
import com.ivan.MoviesRDF.enitity.CrewMember;
import com.ivan.MoviesRDF.enitity.Genre;
import com.ivan.MoviesRDF.enitity.Member;
import com.ivan.MoviesRDF.service.JenaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServiceController {

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

}