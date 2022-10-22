package gbc.comp3095.assignment1.Utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageParser {
    private List<byte[]> imageBytes = new ArrayList<>();

    public ImageParser() {
        try {
            for (int i = 1; i <= 10; i++) {
                File path = ResourceUtils.getFile("classpath:images/image" + i + ".jpg");
                FileInputStream fileInputStream = new FileInputStream(path);
                imageBytes.add(fileInputStream.readAllBytes());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<byte[]> getImageBytes() {
        return imageBytes;
    }
}
