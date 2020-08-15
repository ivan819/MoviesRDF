package com.ivan.MoviesRDF.enitity;

public class CastMember extends Member {

    private Integer order;
    private String character;

    public CastMember(Long id, String name) {
        super(id, name);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getCharacter() {
        return character;
    }

    public void setcCharacter(String character) {
        this.character = character;
    }

    public CastMember(Long id, String name, Integer order, String character) {
        super(id, name);
        this.order = order;
        this.character = character;
    }

}