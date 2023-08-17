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
        List<Symbol> symbols = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == ' ') {
                symbols.add(null);
                width += font.getSpaceSize();
                continue;
            }
            Symbol symbol = font.getSymbols().get(character);
            if (symbol == null) continue;
            width += symbol.getWidth();
            symbols.add(symbol);
            ++width;
        }
        --width;
        System.out.println("Width: " + width);
        BufferedImage bufferedImage = new BufferedImage(width, 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        int x = 0;
        for (Symbol symbol : symbols) {
            if (symbol == null) {
                x += font.getSpaceSize();
                continue;
            }
            int y = 12 - symbol.getImage().getHeight() + (symbol.getImage().getHeight() - symbol.getAscent() - 1);
            g2d.drawImage(symbol.getImage(), x, y, null);
            x += symbol.getWidth() + 1;
        }
        g2d.dispose();
        return bufferedImage;
    }

}
