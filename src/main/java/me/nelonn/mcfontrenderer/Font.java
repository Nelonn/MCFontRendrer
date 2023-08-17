package me.nelonn.mcfontrenderer;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Font {
    public static final Gson GSON = new GsonBuilder().create();

    private final int spaceSize;
    private final Map<Character, Symbol> symbols;

    public Font(int spaceSize, @NotNull Map<Character, Symbol> symbols) {
        this.spaceSize = spaceSize;
        this.symbols = new HashMap<>(symbols);
    }

    public int getSpaceSize() {
        return spaceSize;
    }

    public Map<Character, Symbol> getSymbols() {
        return symbols;
    }

    public static @NotNull Font deserialize(@NotNull String string) {
        JsonObject jsonObject = GSON.fromJson(string, JsonObject.class);
        JsonArray providers = jsonObject.getAsJsonArray("providers");

        int spaceSize = 4;
        Map<Character, Symbol> symbols = new HashMap<>();

        for (JsonElement providerElement : providers) {
            JsonObject providerJson = providerElement.getAsJsonObject();
            String type = providerJson.get("type").getAsString();
            if (type.equals("space")) {
                JsonObject advances = providerJson.getAsJsonObject("advances");
                if (!advances.has(" ")) continue;
                spaceSize = advances.get(" ").getAsInt();
            } else if (type.equals("bitmap")) {
                String path = providerJson.get("file").getAsString();
                if (path.startsWith("minecraft:font/")) {
                    path = path.substring("minecraft:font/".length());
                }
                int ascent = providerJson.get("ascent").getAsInt();
                JsonArray charsArray = providerJson.getAsJsonArray("chars");
                int columns = charsArray.get(0).getAsString().length();
                int rows = charsArray.size();
                File file = new File(path);
                BufferedImage bitmapImage;
                try {
                    bitmapImage = ImageIO.read(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int subImageWidth = bitmapImage.getWidth() / columns;
                int subImageHeight = bitmapImage.getHeight() / rows;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < columns; col++) {
                        String rowString = charsArray.get(row).getAsString();

                        char character = rowString.charAt(col);

                        int x = col * subImageWidth;
                        int y = row * subImageHeight;
                        BufferedImage image = bitmapImage.getSubimage(x, y, subImageWidth, subImageHeight);
                        image = clipRightEmptyPart(image);

                        Symbol symbol = new Symbol(character, ascent, image);

                        symbols.put(symbol.getCharacter(), symbol);
                    }
                }
            }
        }

        return new Font(spaceSize, symbols);
    }

    public static BufferedImage clipRightEmptyPart(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int rightMostNonEmptyColumn = width - 1;
        for (int x = width - 1; x >= 0; x--) {
            boolean columnIsEmpty = true;
            for (int y = 0; y < height; y++) {
                if ((originalImage.getRGB(x, y) >> 24) != 0) {
                    columnIsEmpty = false;
                    break;
                }
            }
            if (!columnIsEmpty) {
                rightMostNonEmptyColumn = x;
                break;
            }
        }

        int clippedWidth = rightMostNonEmptyColumn + 1;

        return originalImage.getSubimage(0, 0, clippedWidth, height);
    }
}
