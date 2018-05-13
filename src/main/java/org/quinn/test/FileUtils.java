package org.quinn.test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {
    public static String getResPath(String name) {
        String absolutePath = "";
        try {
            URL resource = FileUtils.class.getClassLoader().getResource(name);
            absolutePath = Paths.get(resource.toURI()).toAbsolutePath().toString();
        } catch (Exception e) {
            System.err.println(e);
        }
        return absolutePath;
    }
}

