package com.zero01alpha.dj4me;

import com.zero01alpha.dj4me.domain.Mood;
import com.zero01alpha.dj4me.domain.MoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Dj4meApplication {

	private static final Logger log = LoggerFactory.getLogger(Dj4meApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(Dj4meApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(MoodRepository moodRepo) {
		return (args) -> {
				moodRepo.save(new Mood("Happy"));
				moodRepo.save(new Mood("Sad"));
				moodRepo.save(new Mood("Angry"));
				moodRepo.save(new Mood("Neutral"));

				log.info("All moods");
				for (Mood mood : moodRepo.findAll()) {
					log.info("Found " + mood);
				}
				log.info("-------------");
				log.info("Find one mood by id");
				Mood mood = moodRepo.findOne(1L);
				log.info("found " + mood);
				log.info("-------------");

				log.info("Find log by name: Angry");
				mood = moodRepo.findByName("Angry").get(0);
				log.info("Found " + mood);
				log.info("-------------");

		};
	}



}
