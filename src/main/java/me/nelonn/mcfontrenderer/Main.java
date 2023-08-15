package me.nelonn.mcfontrenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage:");
            System.out.println("java -jar util.jar (Input string)|(Input file.txt) [Font json file]");
            return;
        }

        String input = args[0];
        if (input.endsWith(".txt")) {
            try {
                input = IOUtil.readString(new File(input));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        Renderer renderer = new Renderer(args.length >= 2 ? args[1] : "default.json");
        BufferedImage bufferedImage = renderer.render(input);

        File outputFile = new File("output.png");

        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Saved to " + outputFile.getName());
        } catch (IOException e) {
            System.out.println("Error while saving: " + e.getMessage());
        }
    }
}
