package com.zero01alpha.dj4me.tasks;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hamerm on 1/28/17.
 */
public class ImageTask extends TimerTask {

    private final Path imagePath = Paths.get("/Users/hamerm/Google Drive/Google Photos/");

    private String lastUsedFile = "";

    @Override
    public void run() {
        try  {
            String mostRecentFile = Arrays
                    .stream(imagePath.toFile().listFiles())
                    .filter(file -> file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .get().getAbsolutePath();

            if (!mostRecentFile.equals(lastUsedFile)) {
                lastUsedFile = mostRecentFile;
                System.out.println(mostRecentFile);
            }


        } catch (Exception e) {
            System.out.print("error");
        }
    }
}
