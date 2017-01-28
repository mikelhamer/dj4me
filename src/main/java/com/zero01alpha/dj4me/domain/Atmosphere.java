package com.zero01alpha.dj4me.domain;

import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.util.List;

/**
 * Created by hamerm on 1/27/17.
 */
@Entity
public class Atmosphere {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Mood> moods;

    Atmosphere() {}

    public Atmosphere(String name, List<Mood> moods) {
        this.name = name;
        this.moods = moods;
    }

    @Override
    public String toString() {
        return name + ": " + moods;
    }
}
