package com.hao.yu.test.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2024-03-21 00:49:07
 */
public class DeleteFile {

    public static void main(String[] args) {
        List<String> files = files();

        Set<String> sets = new HashSet<>();
        for (String file : files) {
            String substring = file.substring(file.lastIndexOf("/") + 1);
            if (substring.contains("《") && substring.contains("》")) {
                String substring1 = substring.substring(substring.indexOf("《") + 1, substring.indexOf("》"));
                if (sets.contains(substring1)) {
                    if (new File(file).delete()) {
                        System.out.println("删除了：" + file);
                    }
                }
                sets.add(substring1);
            }
        }
    }

    private static List<String> files() {
        File file = new File("/Users/待定/Desktop/目标书库/");

        List<String> filePaths = new ArrayList<>();
        if (file.isDirectory()) {
            getAllFiles(file, filePaths);
        }
        return filePaths;
    }

    private static List<String> getAllFiles(File folder, List<String> filePaths) {

        if (Objects.isNull(folder)) {
            return filePaths;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllFiles(file, filePaths);
                } else {
                    filePaths.add(file.getAbsolutePath());
                    // System.out.println(file.getAbsolutePath()); // 输出文件路径
                }
            }
        }
        return filePaths;
    }

}
