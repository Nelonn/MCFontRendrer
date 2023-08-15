package me.nelonn.mcfontrenderer;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {
    private final Font font;

    public Renderer(@NotNull String fontPath) {
        try {
            String string = IOUtil.readString(new File(fontPath));
            font = Font.deserialize(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public BufferedImage render(@NotNull String text) {
        int width = 0;
        List<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            if (symbol == ' ') {
                images.add(null);
                width += font.getSpaceSize();
                continue;
            }
            BufferedImage bufferedImage = font.getSymbols().get(symbol);
            if (bufferedImage == null) continue;
            width += bufferedImage.getWidth();
            images.add(bufferedImage);
            ++width;
        }
        --width;
        System.out.println("Width: " + width);
        BufferedImage bufferedImage = new BufferedImage(width, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        int x = 0;
        for (BufferedImage image : images) {
            if (image == null) {
                x += font.getSpaceSize();
                continue;
            }
            g2d.drawImage(image, x, 0, null);
            x += image.getWidth() + 1;
        }
        g2d.dispose();
        return bufferedImage;
    }

}
