package me.nelonn.mcfontrenderer;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class Symbol {
    private final char character;
    private final int ascent;
    private final BufferedImage image;

    public Symbol(char character, int ascent, @NotNull BufferedImage image) {
        this.character = character;
        this.ascent = ascent;
        this.image = image;
    }

    public char getCharacter() {
        return character;
    }

    public int getAscent() {
        return ascent;
    }

    public @NotNull BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }
}
