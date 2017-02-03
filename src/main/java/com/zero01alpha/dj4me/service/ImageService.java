package com.zero01alpha.dj4me.service;

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.zero01alpha.dj4me.domain.AtmosphereRepository;
import com.zero01alpha.dj4me.domain.MoodRepository;
import net.coobird.thumbnailator.Thumbnails;
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
                System.out.println(mostRecentFile);
                lastUsedFile = mostRecentFile;
                Thumbnails.of(mostRecentFile)
                        .size(1600, 1200)
                        .allowOverwrite(true)
                        .toFile(mostRecentFile);
                getVision(mostRecentFile);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getVision(String imagePath) {
        System.out.println("New Image, checking vision...");
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // Reads the file
            Path path = Paths.get(imagePath);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image image = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder()
                    .setType(Type.LABEL_DETECTION)
                    .setType(Type.FACE_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(image)
                    .build();
            requests.add(request);

            // Performs detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.println("Error: " + res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation entityAnnotation : res.getLabelAnnotationsList() ) {
                    entityAnnotation.getAllFields()
                            .forEach((k, v) -> System.out.println(k + ": " + v));
                }
                System.out.println(res.getFaceAnnotationsList().size() + " faces found");
                for (FaceAnnotation faceAnnotation : res.getFaceAnnotationsList() ) {
                    faceAnnotation.getAllFields()
                            .forEach((k, v) -> System.out.println(k + ": " + v));
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
