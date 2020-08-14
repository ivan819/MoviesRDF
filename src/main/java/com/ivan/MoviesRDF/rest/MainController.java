package com.ivan.MoviesRDF.rest;

import com.ivan.MoviesRDF.dbo.CategoryDBO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
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

    @GetMapping(value = "/home")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/movie")
    public String movies() {

        return "index2";
    }

    @ResponseBody
    @GetMapping(value = "/data")
    public Response<?> data() {
        // File file = ResourceUtils.getFile("classpath:application.properties");

        return ResourceUtils.getFile("classpath:application.properties");
    }
}