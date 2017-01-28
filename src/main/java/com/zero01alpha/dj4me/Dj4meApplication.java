package com.zero01alpha.dj4me;

import com.zero01alpha.dj4me.domain.Atmosphere;
import com.zero01alpha.dj4me.domain.AtmosphereRepository;
import com.zero01alpha.dj4me.domain.Mood;
import com.zero01alpha.dj4me.domain.MoodRepository;
import com.zero01alpha.dj4me.tasks.ImageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


@SpringBootApplication
public class Dj4meApplication {

    private static final Logger log = LoggerFactory.getLogger(Dj4meApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Dj4meApplication.class, args);
    }

    @Bean
    public CommandLineRunner createTestData(MoodRepository moodRepo, AtmosphereRepository atmosphereRepo) {
        return (args) -> {

            Mood happy = moodRepo.save(new Mood("Happy"));
            Mood sad = moodRepo.save(new Mood("Sad"));
            Mood angry = moodRepo.save(new Mood("Angry"));
            Mood neutral = moodRepo.save(new Mood("Neutral"));

            atmosphereRepo.save(new Atmosphere("All Smiles",
                    Arrays.asList(happy)));
            atmosphereRepo.save(new Atmosphere("Way Sad",
                    Arrays.asList(sad)));
            atmosphereRepo.save(new Atmosphere("GRRRR!!!!",
                    Arrays.asList(angry)));
            atmosphereRepo.save(new Atmosphere("Meh",
                    Arrays.asList(neutral)));
            atmosphereRepo.save(new Atmosphere("Smile Frown",
                    Arrays.asList(happy, sad)));
            atmosphereRepo.save(new Atmosphere("Evil Laugh",
                    Arrays.asList(happy, angry)));
            atmosphereRepo.save(new Atmosphere("Slight Grin",
                    Arrays.asList(happy, neutral)));
            atmosphereRepo.save(new Atmosphere("Manic Depressed",
                    Arrays.asList(sad, angry)));
            atmosphereRepo.save(new Atmosphere("Slight Frown",
                    Arrays.asList(sad, neutral)));


            log.info("All Atmospheres");
            for (Atmosphere atmosphere : atmosphereRepo.findAll()) {
                log.info(atmosphere.toString());
            }

        };
    }

    @Bean
    public CommandLineRunner imageTask() {
        return args -> {
            Timer timer = new Timer();
            timer.schedule(new ImageTask(), 0, 1000);
        };
    }


}
