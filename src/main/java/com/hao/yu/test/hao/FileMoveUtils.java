package com.hao.yu.test.hao;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年01月09日 星期五 22:05
 */
public class FileMoveUtils {

    // 定义常见的图书格式和文本格式扩展名
    private static final Set<String> BOOK_AND_TEXT_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".txt", ".pdf", ".epub", ".mobi", ".azw", ".azw3", ".fb2", ".lit",
            ".lrf", ".pdb", ".pml", ".rb", ".tcr", ".html", ".htm", ".xml",
            ".doc", ".docx", ".odt", ".rtf", ".tex", ".wks", ".wps", ".wpd",
            ".md", ".markdown", ".djvu", ".cbz", ".cbr", ".cb7", ".cba", ".cbt"
    ));

    /**
     * 将源文件夹下的所有图书格式或文本文件移动到目标文件夹，并平铺（不保留子文件夹结构）
     *
     * @param sourceFolderPath
     *         源文件夹路径
     * @param targetFolderPath
     *         目标文件夹路径
     *
     * @throws IOException
     *         移动过程中可能出现IO异常
     */
    public static void moveBookAndTextFilesFlat(String sourceFolderPath, String targetFolderPath) throws IOException {
        Path sourcePath = Paths.get(sourceFolderPath);
        Path targetPath = Paths.get(targetFolderPath);

        // 确保目标文件夹存在
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        // 递归遍历源文件夹中的所有内容
        moveBookAndTextFilesRecursive(sourcePath, targetPath);
    }

    /**
     * 递归查找并移动图书格式或文本文件到目标文件夹（平铺）
     *
     * @param sourceDir
     *         源目录
     * @param targetDir
     *         目标目录（平铺所有文件）
     *
     * @throws IOException
     *         移动过程中可能出现IO异常
     */
    private static void moveBookAndTextFilesRecursive(Path sourceDir, Path targetDir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path sourceItem : stream) {
                if (Files.isDirectory(sourceItem)) {
                    // 如果是目录，递归处理子目录
                    moveBookAndTextFilesRecursive(sourceItem, targetDir);
                } else if (Files.isRegularFile(sourceItem)) {
                    // 如果是文件，检查是否为图书或文本格式
                    String fileName = sourceItem.getFileName().toString().toLowerCase();
                    for (String extension : BOOK_AND_TEXT_EXTENSIONS) {
                        if (fileName.endsWith(extension)) {
                            // 是图书或文本格式，移动到目标目录（平铺）
                            Path targetFile = targetDir.resolve(sourceItem.getFileName());

                            // 处理文件名冲突
                            targetFile = getUniqueFilePath(targetFile);

                            Files.move(sourceItem, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Moved: " + sourceItem + " -> " + targetFile);
                            break; // 找到匹配的扩展名后跳出循环
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取唯一的目标文件路径（如果文件已存在则添加数字后缀）
     *
     * @param targetFile
     *         目标文件路径
     *
     * @return 唯一的文件路径
     *
     * @throws IOException
     *         可能出现IO异常
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

    /**
     * 将源文件夹下的所有文件（包含子文件夹）移动到目标文件夹（保持原有结构）
     *
     * @param sourceFolderPath
     *         源文件夹路径
     * @param targetFolderPath
     *         目标文件夹路径
     *
     * @throws IOException
     *         移动过程中可能出现IO异常
     */
    public static void moveFolderContents(String sourceFolderPath, String targetFolderPath) throws IOException {
        Path sourcePath = Paths.get(sourceFolderPath);
        Path targetPath = Paths.get(targetFolderPath);

        // 确保目标文件夹存在
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        // 遍历源文件夹中的所有内容
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) {
            for (Path sourceItem : stream) {
                Path targetItem = targetPath.resolve(sourceItem.getFileName());

                if (Files.isDirectory(sourceItem)) {
                    // 如果是目录，则递归移动整个目录
                    moveDirectoryRecursively(sourceItem, targetItem);
                    // 删除原目录
                    deleteDirectory(sourceItem);
                } else {
                    // 如果是文件，则直接移动
                    Files.move(sourceItem, targetItem, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * 递归移动目录及其内容
     *
     * @param sourceDir
     *         源目录
     * @param targetDir
     *         目标目录
     *
     * @throws IOException
     *         移动过程中可能出现IO异常
     */
    private static void moveDirectoryRecursively(Path sourceDir, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path sourceItem : stream) {
                Path targetItem = targetDir.resolve(sourceItem.getFileName());

                if (Files.isDirectory(sourceItem)) {
                    // 递归处理子目录
                    moveDirectoryRecursively(sourceItem, targetItem);
                } else {
                    // 移动文件
                    Files.move(sourceItem, targetItem, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * 删除目录及其所有内容
     *
     * @param dir
     *         要删除的目录
     *
     * @throws IOException
     *         删除过程中可能出现IO异常
     */
    private static void deleteDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path item : stream) {
                    if (Files.isDirectory(item)) {
                        deleteDirectory(item);
                    } else {
                        Files.delete(item);
                    }
                }
                Files.delete(dir);
            }
        }
    }

    public static void main(String[] args) {
        // 示例用法
        try {
            // 示例：将A文件夹中的所有图书或文本文件移动到B文件夹（平铺）
            String sourceFolder =
                    "/Users/yuhao/Downloads/06网盘/全网VIP小说排行榜Top100，必读热门网文书单合集"; // 源文件夹路径
            String targetFolder =
                    "/Users/yuhao/Downloads/06网盘/hahahah"; // 目标文件夹路径

            moveBookAndTextFilesFlat(sourceFolder, targetFolder);
            System.out.println("图书和文本文件平铺移动完成！");
        } catch (IOException e) {
            System.err.println("移动文件时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
}

