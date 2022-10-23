package gbc.comp3095.assignment1.Utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageParser {
    private byte[] defaultImage;
    private List<byte[]> imageBytes = new ArrayList<>();

    public ImageParser() {
        try {
            // Setting default image
            File file = ResourceUtils.getFile("classpath:default_images/default.png");
            FileInputStream fileInputStream = new FileInputStream(file);
            defaultImage = fileInputStream.readAllBytes();

            // Setting default images for testing
            for (int i = 1; i <= 4; i++) {
                file = ResourceUtils.getFile("classpath:default_images/image" + i + ".jpg");
                fileInputStream = new FileInputStream(file);
                imageBytes.add(fileInputStream.readAllBytes());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<byte[]> getImageBytes() {
        return imageBytes;
    }

    public byte[] getDefaultImage() {
        return defaultImage;
    }
}
