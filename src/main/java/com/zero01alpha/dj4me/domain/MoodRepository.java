package com.zero01alpha.dj4me.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by hamerm on 1/27/17.
 */
public interface MoodRepository extends CrudRepository<Mood, Long> {

    List<Mood> findByName(String name);

}
