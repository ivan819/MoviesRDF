package com.ivan.MoviesRDF.enitity;

public class CrewMember implements Member {

    private Long id;
    private String name;
    private String department;
    private String job;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Member) obj).getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
