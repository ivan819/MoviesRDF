package com.ivan.MoviesRDF.enitity;

public class CrewMember extends Member {

    private String department;
    private String job;

    public CrewMember(Long id, String name) {
        super(id, name);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public CrewMember(Long id, String name, String department, String job) {
        super(id, name);
        this.department = department;
        this.job = job;
    }

}
