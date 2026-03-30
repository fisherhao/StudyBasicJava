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
    private static final List<String> FORCE_DELETE_KEYWORDS = Stream.of("女", "妾")
        .collect(Collectors.toList());

    public static void main(String[] args) {
        System.out.println("开始执行");
        start();
    }

    private static void start() {
        String folderPath = "/Users/yuhao/Downloads/06网盘";
        // 仅删除女频：命中女频关键词，且不命中男频保护关键词
        List<String> femaleKeywords = Stream.of("王妃", "皇后", "太子妃", "侧妃", "嫡女",
                "庶女",
                "贵女", "千金", "小姐", "娘娘", "夫人", "娇妻", "萌宝", "团宠", "甜宠",
                "替嫁",
                "和离", "休夫", "女配", "恶毒女配", "宫斗", "宅斗", "闺阁", "闺秀", "后宫",
                "本宫",
                "哀家", "外室", "小妾", "姨娘", "世子妃", "摄政王妃", "冲喜新娘", "替身新娘",
                "福妻",
                "医妃", "毒妃", "宠妃", "宠后", "贵妃", "淑妃", "贤妃", "皇贵妃", "王府嫡女",
                "侯府千金",
                "将军夫人", "女主", "女尊", "女强", "长姐", "幺女", "秀女", "侍寝", "承宠",
                "争宠",
                "凤华", "凰妃", "王爷独宠", "王妃驾到", "王妃又", "王妃要", "带球跑", "追妻",
                "复婚", "总裁夫人",
                "美男", "夫君", "郎君", "相公", "夫婿", "良人", "王爷", "王府", "郡主", "公主",
                "驸马", "太后", "太妃", "嫡妻", "继室", "正妻", "侧室", "姨太", "小娘子", "娘子",
                "宠妻", "独宠", "娇宠", "盛宠", "权宠", "偏宠", "娇软", "软糯", "奶团子", "小奶包",
                "双宝", "三宝", "四宝", "五宝", "锦鲤", "福宝", "福运", "旺夫", "冲喜", "守寡",
                "再嫁", "合离", "闺房", "闺蜜", "白月光", "黑莲花", "替身", "先婚后爱", "隐婚",
                "豪门", "总裁", "前妻", "契约婚姻", "病娇", "掉马", "马甲", "苏爽", "虐渣",
                "嫡姐重生", "嫡妹", "嫡母", "庶妹逆袭", "庶姐", "嫡长女", "嫡长媳", "世子夫人", "侯门主母", "将门嫡女",
                "将门虎女", "丞相嫡女", "国公千金", "侯府嫡女", "王府弃妃", "冷宫弃后", "废妃重生", "贵妃重生", "皇后重生", "太子妃重生",
                "王妃重生", "郡主重生", "公主重生", "嫡女归来", "千金归来", "真千金", "假千金", "真假千金", "首辅夫人", "权臣夫人",
                "摄政王独宠", "摄政王的", "皇叔的", "王爷的替嫁妃", "王爷宠妃", "暴君宠后", "帝后恩宠", "皇上独宠", "后宫争宠", "宫廷秘恋",
                "深宫计", "宫墙柳", "凤仪天下", "母仪天下", "凤冠霞帔", "锦绣良缘", "盛世嫡妃", "嫡女谋", "嫡女医妃", "神医王妃",
                "毒医王妃", "神医毒妃", "医女倾城", "妃常嚣张", "妃常难驯", "凰权", "凤谋", "凰女", "凤女", "女帝归来",
                "女帝养成", "长公主", "公主在上", "驸马在下", "和离后", "和离书", "休夫后", "休夫记", "再嫁高门", "守寡后",
                "冲喜王妃", "冲喜夫人", "替嫁王妃", "替嫁千金", "替身妻子", "豪门甜妻", "总裁娇妻", "总裁追妻", "先婚后宠", "隐婚甜妻",
                "带崽", "萌娃来袭", "萌宝来袭", "双宝来袭", "三宝来袭", "团宠小", "团宠千金", "锦鲤小", "福运小", "娇妻在上")
            .map(String::trim)
            .filter(StrUtil::isNotBlank)
            .distinct()
            .collect(Collectors.toList());

        // 出现这些关键词时默认保留，避免把男频或中性题材误删
        List<String> maleProtectKeywords = Stream.of("玄幻", "奇幻", "修仙", "修真",
                "仙尊", "仙帝",
                "武道", "武神", "武帝", "剑道", "剑神", "刀神", "战神", "兵王", "赘婿",
                "龙王", "天帝",
                "大帝", "至尊", "诸天", "万界", "高武", "末世", "洪荒", "封神", "斗破",
                "斗罗", "吞噬",
                "星空", "网游", "电竞", "军旅", "谍战", "特种兵", "官场", "鉴宝", "道士",
                "天师", "茅山",
                "风水", "捉鬼", "盗墓", "摸金", "悬疑", "推理", "破案", "刑侦", "黑道",
                "江湖", "少主",
                "少帅", "侯爷", "世子", "家主", "宗门", "门派", "世家", "修罗", "魔尊",
                "邪帝", "龙婿",
                "奶爸", "归来", "逆袭", "崛起", "都市最强", "长生", "不朽", "机甲", "御兽",
                "卡牌", "将门")
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
