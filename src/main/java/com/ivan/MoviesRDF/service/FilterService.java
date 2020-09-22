package com.ivan.MoviesRDF.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ivan.MoviesRDF.enitity.Company;

public class FilterService {
    private List<Company> companies;

    private FilterService(List<Company> companies) {
        this.companies = companies;
    }

    public static FilterService getCompanyFilter(List<Company> companies) {
        FilterService fs = new FilterService(companies);
        return fs;
    }

    public FilterService filter(String s) {
        if (s != null && !s.isEmpty())
            this.companies = this.companies.stream().filter(e -> e.getName().toLowerCase().contains(s.toLowerCase()))
                    .collect(Collectors.toList());
        return this;
    }

    public FilterService order(int orderType, boolean asc) {
        Comparator<Company> c;

        switch (orderType) {
            case 1:
                c = Comparator.comparing(Company::getName);
                break;
            case 2:
                c = Comparator.comparing(Company::getNumberMovies);
                break;
            case 3:
                c = Comparator.comparing(Company::getRevenue);
                break;
            default:
                c = Comparator.comparing(Company::getName);
        }
        if (!asc)
            c = c.reversed();

        this.companies = this.companies.stream().sorted(c).collect(Collectors.toList());
        return this;
    }

    public FilterService limit(Integer limit) {
        if (limit != 0)
            this.companies = this.companies.stream().limit(limit).collect(Collectors.toList());
        return this;
    }

    public List<Company> getList() {
        return this.companies;
    }
}
