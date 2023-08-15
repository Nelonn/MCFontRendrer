package me.nelonn.mcfontrenderer;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOUtil {
    public static String readString(@NotNull File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        }
    }
}
