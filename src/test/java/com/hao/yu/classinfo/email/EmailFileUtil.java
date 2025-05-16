package com.hao.yu.classinfo.email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public final class EmailFileUtil {

    public static List<String> getFiles() throws IOException {

        String folder = "/Users/yuhao/Downloads/06网盘/";

        List<String> collect = Files
                .list(Paths.get(folder))
                .map(t -> t.getFileName().toString())
                .filter(t -> !t.startsWith("."))
                .map(t -> folder + t)
                .collect(Collectors.toList());

        return collect;
    }

    public static List<String> getFileName() throws IOException {

        String folder = "/Users/yuhao/Downloads/06网盘/";

        return Files
                .list(Paths.get(folder))
                .map(t -> t.getFileName().toString())
                .filter(t -> !t.startsWith("."))
                .collect(Collectors.toList());
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private EmailFileUtil() {
        throw new UnsupportedOperationException("This class is not meant to be instantiated.");
    }
}
