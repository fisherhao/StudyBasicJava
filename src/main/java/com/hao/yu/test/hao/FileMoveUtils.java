package com.hao.yu.test.hao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年01月09日 星期五 22:05
 */
public class FileMoveUtils {
    private static final AtomicInteger movedFileCount = new AtomicInteger();

    private static final List<String> moveList = new ArrayList<>();

    // 缓存文件路径（放在java类文件同目录下）
    private static final String CACHE_FILE_NAME = "moved_books_cache.txt";

    // 已移动的书名缓存
    private static final Set<String> movedBookNames = new HashSet<>();

    // 定义常见的图书格式和文本格式扩展名
    private static final Set<String> BOOK_AND_TEXT_EXTENSIONS = new HashSet<>(
        Arrays.asList(
            ".txt", ".pdf", ".epub", ".mobi", ".azw", ".azw3", ".fb2", ".lit",
            ".lrf", ".pdb", ".pml", ".rb", ".tcr", ".html", ".htm", ".xml",
            ".doc", ".docx", ".odt", ".rtf", ".tex", ".wks", ".wps", ".wpd",
            ".md", ".markdown", ".djvu", ".cbz", ".cbr", ".cb7", ".cba", ".cbt"
        ));

    /**
     * 将源文件夹下的所有图书格式或文本文件移动到目标文件夹，并平铺（不保留子文件夹结构）
     *
     * @param sourceFolderPath
     *     源文件夹路径
     * @param targetFolderPath
     *     目标文件夹路径
     *
     * @throws IOException
     *     移动过程中可能出现IO异常
     */
    public static void moveBookAndTextFilesFlat(String sourceFolderPath, String targetFolderPath)
        throws IOException {
        // 启动移动时，读取缓存并在文件末尾添加换行
        loadMovedBooksCache();
        appendNewlineToCacheFile();

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
     *     源目录
     * @param targetDir
     *     目标目录（平铺所有文件）
     *
     * @throws IOException
     *     移动过程中可能出现IO异常
     */
    private static void moveBookAndTextFilesRecursive(Path sourceDir, Path targetDir)
        throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path sourceItem : stream) {
                if (Files.isDirectory(sourceItem)) {
                    // 如果是目录，递归处理子目录
                    moveBookAndTextFilesRecursive(sourceItem, targetDir);
                } else if (Files.isRegularFile(sourceItem)) {
                    // 如果是文件，检查是否为图书或文本格式
                    String fileName = sourceItem.getFileName().toString().toLowerCase();
                    String originalFileName = sourceItem.getFileName().toString();
                    for (String extension : BOOK_AND_TEXT_EXTENSIONS) {
                        if (fileName.endsWith(extension)) {
                            // 获取书名（不含扩展名）
                            String bookName = getBookNameWithoutExtension(
                                originalFileName);

                            // 检查书名是否已移动过
                            if (movedBookNames.contains(bookName)) {
                                System.out.println("跳过已移动的书: " + originalFileName);
                                break; // 跳过，不移动
                            }

                            // 是图书或文本格式，移动到目标目录（平铺）
                            Path targetFile = targetDir.resolve(sourceItem.getFileName());

                            // 处理文件名冲突
                            targetFile = getUniqueFilePath(targetFile);

                            Files.move(sourceItem, targetFile,
                                StandardCopyOption.REPLACE_EXISTING);

                            // 记录书名到缓存
                            recordBookName(bookName);

                            String x = getX(sourceItem, targetFile);
                            System.out.println(x);

                            moveList.add(x);
                            movedFileCount.incrementAndGet();
                            break; // 找到匹配的扩展名后跳出循环
                        }
                    }
                }
            }
        }
    }

    private static String getX(Path sourceItem, Path targetFile) {
        return "Moved: " + sourceItem + " -> " + targetFile;
    }

    /**
     * 获取书名（不含扩展名）
     *
     * @param fileName
     *     文件名
     *
     * @return 书名（不含扩展名）
     */
    private static String getBookNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    /**
     * 获取缓存文件路径
     *
     * @return 缓存文件路径
     */
    private static Path getCacheFilePath() {
        // 缓存文件放在java类文件同目录下
        Path classDir = Paths.get("src/main/java/com/hao/yu/test/hao");
        return classDir.resolve(CACHE_FILE_NAME);
    }

    /**
     * 加载已移动的书名缓存
     */
    private static void loadMovedBooksCache() {
        try {
            Path cacheFile = getCacheFilePath();
            if (Files.exists(cacheFile)) {
                List<String> lines = Files.readAllLines(cacheFile,
                    StandardCharsets.UTF_8);
                movedBookNames.clear();
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        movedBookNames.add(trimmed);
                    }
                }
                System.out.println(
                    "已加载 " + movedBookNames.size() + " 个已移动的书名记录");
            }
        } catch (IOException e) {
            System.err.println("加载缓存文件失败: " + e.getMessage());
        }
    }

    /**
     * 启动移动时，在缓存文件末尾添加换行
     */
    private static void appendNewlineToCacheFile() {
        try {
            Path cacheFile = getCacheFilePath();
            // 确保目录存在
            Files.createDirectories(cacheFile.getParent());
            // 在文件末尾添加换行（如果文件存在）
            if (Files.exists(cacheFile)) {
                Files.write(cacheFile,
                    System.lineSeparator().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println("添加换行失败: " + e.getMessage());
        }
    }

    /**
     * 记录书名到缓存文件
     *
     * @param bookName
     *     书名
     */
    private static void recordBookName(String bookName) {
        try {
            // 添加到内存缓存
            movedBookNames.add(bookName);

            // 写入文件（不换行）
            Path cacheFile = getCacheFilePath();
            Files.createDirectories(cacheFile.getParent());
            Files.write(cacheFile, bookName.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("记录书名失败: " + bookName + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 获取唯一的目标文件路径（如果文件已存在则添加数字后缀）
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

    public static void test01(String sourceFolder, String targetFolder) {
        // 示例用法

//        // 示例：将A文件夹中的所有图书或文本文件移动到B文件夹（平铺）
//        String sourceFolder =
//                "/Users/www/Downloads/06网盘/全网热门网文书单合集";
// 源文件夹路径
//        String targetFolder =
//                "/Users/www/Downloads/06网盘/hahahah"; // 目标文件夹路径

        try {
            moveBookAndTextFilesFlat(sourceFolder, targetFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("图书和文本文件平铺移动完成！22");

    }

    public static void main(String[] args) {
        test01("/Users/yuhao/Downloads/06网盘/00原始文件",
            "/Users/yuhao/Downloads/06网盘/00目标文件");

    }

}

