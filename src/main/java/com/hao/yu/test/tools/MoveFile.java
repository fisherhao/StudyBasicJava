package com.hao.yu.test.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2024-02-13 17:05:43
 */
public class MoveFile {
    private static final ScheduledExecutorService scheduledExecutorService;
    static {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }
    public static void main(String[] args) {
        scheduledExecutorService.scheduleAtFixedRate(() -> exe(), 1, 400000, TimeUnit.SECONDS);
    }

    public static void exe() {
        System.out.println(Thread.currentThread()
                .getName());
        System.out.println(new Date());

        List<String> stringList = new ArrayList<>(8000);

        List<String> files = files();

        Set<String> read = read();

        for (String file : files) {
            move(file, stringList, read);
        }

        write(stringList);
    }

    private static List<String> files() {
        File file = new File("/Users/待定/Desktop/各类电子书籍精品大合集");

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

    private static void move(String filePath, List<String> stringList, Set<String> read) {
        String target = "/Users/待定/Desktop/目标书库";
        String fileName = filePath.substring(filePath.lastIndexOf("/"));

        String newFileName = null;
        //txt pdf epub doc docx mobi azw3
        List<String> pattern = Arrays.asList("txt", "pdf", "epub", "docx", "doc", "azw3", "mobi");
        if (Objects.nonNull(fileName)) {
            if (fileName.endsWith(".part")) {
                System.out.println("移动中：" + fileName);
                return;
            }
            boolean can = false;
            for (String p : pattern) {
                if (fileName.endsWith(p)) {
                    can = true;
                    break;
                }
            }
            if (!can) {
                System.out.println("格式不对：" + fileName);
                return;
            }
            newFileName = fileName.substring(0, fileName.lastIndexOf("."));
            //去处空格
            newFileName = newFileName.replaceAll(" ", "");
            //去处空格
            newFileName = newFileName.replaceAll("/", "");
        }
        if (stringList.contains(newFileName)) {
            System.out.println("文件重复1：" + fileName);
            return;
        }
        if (read.contains(newFileName)) {
            System.out.println("文件重复2：" + fileName);
            return;
        }

        stringList.add(newFileName);

        File sourceFile = new File(filePath);
        File targetFile = new File(target + fileName);
        if (sourceFile.exists()) {
            boolean isMoved = sourceFile.renameTo(targetFile);

            if (isMoved) {
                System.out.println("成功移动：" + fileName);
            } else {
                System.out.println("无法移动文件。");
            }
        } else {
            System.out.println("源文件不存在。");
        }

    }

    private static final String file_Path = "/Users/待定/Desktop/记录/书名记录.txt";

    private static void write(List<String> stringList) {

        Set<String> readHash = read();

        readHash.addAll(stringList);

        // 要写入的文件路径

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file_Path))) {
            // 将字符串写入文件
            writer.write(String.join("\n", readHash));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Set<String> read() {
        try (FileInputStream fis = new FileInputStream(file_Path)) {

            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len;
            while ((len = fis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
                buffer = new byte[1024];
            }
            fis.close();

            String content = sb.toString();
            if (Objects.isNull(content)) {
                return new HashSet<>();
            }

            String[] split = content.split("\n");
            return Stream.of(split)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }
}
