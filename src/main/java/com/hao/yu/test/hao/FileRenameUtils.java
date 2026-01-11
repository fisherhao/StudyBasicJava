package com.hao.yu.test.hao;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 说明：文件批量重命名工具类
 *
 * @author Witty·Kid Fisher
 * @date 2026年01月11日 Sunday 14:58
 */
public class FileRenameUtils {

    private static final AtomicInteger renamedFileCount = new AtomicInteger();

    /**
     * 批量将文件夹下面的所有文件名称中包含A，替换成为B
     *
     * @param folderPath
     *     文件夹路径
     * @param oldString
     *     需要替换的字符串A
     * @param newString
     *     替换后的字符串B
     *
     * @throws IOException
     *     重命名过程中可能出现IO异常
     */
    public static void batchRenameFiles(String folderPath, String oldString, String newString)
        throws IOException {
        Path folder = Paths.get(folderPath);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException(
                "指定的路径不存在或不是文件夹: " + folderPath);
        }

        // 递归遍历文件夹中的所有文件
        renameFilesRecursive(folder, oldString, newString);

        System.out.println(
            "批量重命名完成！共重命名 " + renamedFileCount.get() + " 个文件");
    }

    /**
     * 递归查找并重命名文件
     *
     * @param dir
     *     目录路径
     * @param oldString
     *     需要替换的字符串A
     * @param newString
     *     替换后的字符串B
     *
     * @throws IOException
     *     重命名过程中可能出现IO异常
     */
    private static void renameFilesRecursive(Path dir, String oldString, String newString)
        throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path item : stream) {
                if (Files.isDirectory(item)) {
                    // 如果是目录，递归处理子目录
                    renameFilesRecursive(item, oldString, newString);
                } else if (Files.isRegularFile(item)) {
                    // 如果是文件，检查文件名是否包含需要替换的字符串
                    String fileName = item.getFileName().toString();
                    if (fileName.contains(oldString)) {
                        // 替换文件名中的字符串
                        String newFileName = fileName.replace(oldString, newString);
                        Path newFilePath = item.getParent().resolve(newFileName);

                        // 处理文件名冲突
                        newFilePath = getUniqueFilePath(newFilePath);

                        // 重命名文件
                        Files.move(item, newFilePath);

                        System.out.println("重命名: " + item + " -> " + newFilePath);
                        renamedFileCount.incrementAndGet();
                    }
                }
            }
        }
    }

    /**
     * 获取唯一的文件路径（如果文件已存在则添加数字后缀）
     *
     * @param targetFile
     *     目标文件路径
     *
     * @return 唯一的文件路径
     *
     * @throws IOException
     *     可能出现IO异常
     */
    private static Path getUniqueFilePath(Path targetFile) throws IOException {
        if (!Files.exists(targetFile)) {
            return targetFile;
        }

        String fileName = targetFile.getFileName().toString();
        String nameWithoutExt = fileName;
        String extension = "";

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            nameWithoutExt = fileName.substring(0, lastDotIndex);
            extension = fileName.substring(lastDotIndex);
        }

        int counter = 1;
        Path newTargetFile;
        do {
            String newName = nameWithoutExt + "_" + counter + extension;
            newTargetFile = targetFile.getParent().resolve(newName);
            counter++;
        }
        while (Files.exists(newTargetFile));

        return newTargetFile;
    }

    public static void test001(String folderPath, String oldString, String newString) {
        try {
            batchRenameFiles(folderPath, oldString, newString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        test001("/Users/yuhao/Downloads/06网盘/电台节目1", "（ai提取版）", "");
        test001("/Users/yuhao/Downloads/06网盘/电台节目2", "楓葉落慕 - ", "");
        test001("/Users/yuhao/Downloads/06网盘/电台节目1", "星环联邦统帅 - ", "");
        test001("/Users/yuhao/Downloads/06网盘/电台节目2", "（ai提取版）", "");

        test001("/Users/yuhao/Downloads/06网盘/电台节目1", "《", "");
        test001("/Users/yuhao/Downloads/06网盘/电台节目1", "》", "");

    }
}
