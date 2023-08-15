package me.nelonn.mcfontrenderer;

public class Util {
    public static char getCharacterAtPosition(String input, int position) {
        if (position >= 0 && position < input.length()) {
            return Character.highSurrogate(Character.codePointAt(input, position));
        } else {
            throw new IllegalArgumentException("Invalid position.");
        }
    }
}
