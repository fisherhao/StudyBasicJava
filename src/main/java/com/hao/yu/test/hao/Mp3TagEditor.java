package com.hao.yu.test.hao;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 说明：MP3文件标签编辑工具类
 *
 * @author Witty·Kid Fisher
 * @date 2026年01月11日 Sunday 15:24
 */
public class Mp3TagEditor {

    private static final AtomicInteger processedFileCount = new AtomicInteger();

    /**
     * 批量修改文件夹下所有MP3文件的标签信息
     *
     * @param folderPath
     *     文件夹路径
     * @param albumName
     *     专辑名称
     *
     * @throws IOException
     *     处理过程中可能出现IO异常
     */
    public static void batchEditMp3Tags(String folderPath, String albumName)
        throws IOException {
        Path folder = Paths.get(folderPath);

        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException(
                "指定的路径不存在或不是文件夹: " + folderPath);
        }

        // 递归遍历文件夹中的所有MP3文件
        editMp3TagsRecursive(folder, albumName);

        System.out.println(
            "批量修改MP3标签完成！共处理 " + processedFileCount.get() + " 个文件");
    }

    /**
     * 递归查找并修改MP3文件标签
     *
     * @param dir
     *     目录路径
     * @param albumName
     *     专辑名称
     *
     * @throws IOException
     *     处理过程中可能出现IO异常
     */
    private static void editMp3TagsRecursive(Path dir, String albumName)
        throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path item : stream) {
                if (Files.isDirectory(item)) {
                    // 如果是目录，递归处理子目录
                    editMp3TagsRecursive(item, albumName);
                } else if (Files.isRegularFile(item)) {
                    // 如果是文件，检查是否为MP3文件
                    String fileName = item.getFileName().toString().toLowerCase();
                    if (fileName.endsWith(".mp3")) {
                        editSingleMp3Tag(item.toFile(), albumName);
                    }
                }
            }
        }
    }

    /**
     * 修改单个MP3文件的标签信息
     *
     * @param file
     *     MP3文件
     * @param albumName
     *     专辑名称
     */
    private static void editSingleMp3Tag(File file, String albumName) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTagOrCreateAndSetDefault();

            // 获取文件名（不含扩展名）作为标题
            String fileName = file.getName();
            String title = fileName.substring(0, fileName.lastIndexOf('.'));

            // 设置标题为文件名
            tag.setField(FieldKey.TITLE, title);

            // 设置专辑名称
            tag.setField(FieldKey.ALBUM, albumName);

            // 保存修改
            audioFile.commit();

            System.out.println("已修改: " + file.getAbsolutePath() + " -> 标题: " + title
                + ", 专辑: " + albumName);
            processedFileCount.incrementAndGet();
        } catch (Exception e) {
            System.err.println("修改文件失败: " + file.getAbsolutePath() + ", 错误: "
                + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void test001(String folderPath, String albumName) {
        try {
            batchEditMp3Tags(folderPath, albumName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test001("/Users/yuhao/Downloads/06网盘/电台节目1", "凡人修仙传原声带");
    }
}
