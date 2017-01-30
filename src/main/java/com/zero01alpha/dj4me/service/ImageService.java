package com.zero01alpha.dj4me.service;

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.zero01alpha.dj4me.domain.AtmosphereRepository;
import com.zero01alpha.dj4me.domain.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageService {

    private final Path imagePath = Paths.get("/Users/hamerm/Google Drive/Google Photos/");
    private String lastUsedFile = "";

    @Autowired
    AtmosphereRepository atmosphereRepository;

    @Autowired
    MoodRepository moodRepository;

    @Scheduled(fixedDelay = 2000)
    private void checkForNewImage() {
        try {
            String mostRecentFile = Arrays
                    .stream(imagePath.toFile().listFiles())
                    .filter(file -> file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))
                    .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()))
                    .get().getAbsolutePath();

            if (!mostRecentFile.equals(lastUsedFile)) {
                lastUsedFile = mostRecentFile;
                System.out.println(mostRecentFile);
                getVision(mostRecentFile);
            }


        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void getVision(String imagePath) {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // Reads the file
            Path path = Paths.get(imagePath);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder()
                    .setType(Type.FACE_DETECTION)
                    .setType(Type.TEXT_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (FaceAnnotation faceAnnotation : res.getFaceAnnotationsList() ) {
                    faceAnnotation.getAllFields().forEach((k, v)->System.out.printf("%s : %s\n", k, v.toString()));
                }
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

}
