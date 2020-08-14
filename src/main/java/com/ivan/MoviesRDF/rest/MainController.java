package com.ivan.MoviesRDF.rest;

import com.ivan.MoviesRDF.dbo.CategoryDBO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @Autowired
    CategoryDBO catDBO;

    @GetMapping(value = "/test")
    @ResponseBody
    public String getTestData() {

        return catDBO.test();
    }
}