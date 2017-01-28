package com.zero01alpha.dj4me.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by hamerm on 1/27/17.
 */
@Entity
public class Mood {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    Mood() {}

    public Mood(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
