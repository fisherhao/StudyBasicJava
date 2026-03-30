package com.hao.yu.test.hao;

import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 说明：按文件名关键字批量删除文件工具类
 *
 * @author yuHao
 * @date 2026年03月26日 Thursday 22:15
 */
public class FileNameBatchDeleteUtils {

    private static final AtomicInteger deletedItemCount = new AtomicInteger();
    private static final List<String> failedItemNames = new ArrayList<>();
    private static final List<String> FORCE_DELETE_KEYWORDS = Stream.of("")
        .collect(Collectors.toList());

    public static void main(String[] args) {
        System.out.println("开始执行");
        start();
    }

    private static void start() {
        String folderPath = "/Users/yuhao/Downloads/06网盘";
        // 仅删除女频：命中关键词，且不命中保护关键词
        List<String> femaleKeywords = Stream.of("")
            .map(String::trim)
            .filter(StrUtil::isNotBlank)
            .distinct()
            .collect(Collectors.toList());

        // 出现这些关键词时默认保留
        List<String> maleProtectKeywords = Stream.of("玄幻")
            .map(String::trim)
            .filter(StrUtil::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        runBatchDelete(folderPath, femaleKeywords, maleProtectKeywords);
    }

    private static void runBatchDelete(String folderPath, List<String> femaleKeywords,
                                       List<String> maleProtectKeywords) {
        try {
            failedItemNames.clear();
            deletedItemCount.set(0);
            validateParams(folderPath, femaleKeywords, maleProtectKeywords);
            deleteMatchedFiles(folderPath, femaleKeywords, maleProtectKeywords);
            System.out.println(
                "批量删除完成！共删除 " + deletedItemCount.get() + " 个文件/文件夹");
            printFailedItemNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validateParams(String folderPath, List<String> femaleKeywords,
                                       List<String> maleProtectKeywords) {
        Path folder = Paths.get(folderPath);
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException(
                "指定路径不存在或不是文件夹: " + folderPath);
        }
        if (femaleKeywords == null || femaleKeywords.isEmpty()) {
            throw new IllegalArgumentException("女频关键字列表不能为空");
        }
        if (maleProtectKeywords == null || maleProtectKeywords.isEmpty()) {
            throw new IllegalArgumentException("男频保护关键字列表不能为空");
        }
    }

    private static void deleteMatchedFiles(String folderPath, List<String> femaleKeywords,
                                           List<String> maleProtectKeywords) throws IOException {
        Path folder = Paths.get(folderPath);
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (attrs.isRegularFile() && shouldDeleteByBookName(
                    file.getFileName().toString(), femaleKeywords, maleProtectKeywords)) {
                    deleteFileSafely(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                if (isRootFolder(folder, dir)) {
                    return FileVisitResult.CONTINUE;
                }
                if (shouldDeleteByBookName(dir.getFileName().toString(), femaleKeywords,
                    maleProtectKeywords)) {
                    deleteDirectorySafely(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static boolean isRootFolder(Path rootFolder, Path currentFolder) {
        return rootFolder.equals(currentFolder);
    }

    private static boolean shouldDeleteByBookName(String fileName, List<String> femaleKeywords,
                                                  List<String> maleProtectKeywords) {
        if (containsAnyKeyword(fileName, FORCE_DELETE_KEYWORDS)) {
            return true;
        }
        if (!containsAnyKeyword(fileName, femaleKeywords)) {
            return false;
        }
        return !containsAnyKeyword(fileName, maleProtectKeywords);
    }

    private static boolean containsAnyKeyword(String fileName, List<String> keywords) {
        String lowerCaseFileName = fileName.toLowerCase();
        for (String keyword : keywords) {
            if (lowerCaseFileName.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static void deleteFileSafely(Path filePath) {
        try {
            if (Files.deleteIfExists(filePath)) {
                deletedItemCount.incrementAndGet();
                System.out.println("已删除: " + filePath);
            }
        } catch (IOException e) {
            failedItemNames.add(filePath.getFileName().toString());
            System.err.println("删除失败: " + filePath + ", 原因: " + e.getMessage());
        }
    }

    private static void deleteDirectorySafely(Path dirPath) {
        try {
            deleteDirectoryRecursively(dirPath);
            deletedItemCount.incrementAndGet();
            System.out.println("已删除文件夹: " + dirPath);
        } catch (IOException e) {
            failedItemNames.add(dirPath.getFileName().toString());
            System.err.println("删除失败: " + dirPath + ", 原因: " + e.getMessage());
        }
    }

    private static void deleteDirectoryRecursively(Path dirPath) throws IOException {
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void printFailedItemNames() {
        if (failedItemNames.isEmpty()) {
            return;
        }
        System.err.println("删除失败文件/文件夹名称列表如下：");
        for (String failedItemName : failedItemNames) {
            System.err.println(failedItemName);
        }
    }
}
