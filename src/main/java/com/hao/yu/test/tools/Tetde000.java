package com.hao.yu.test.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 说明：
 *
 * @author： Vicolen.Hao
 * @date： 2024-03-17 20:55:30
 */
public class Tetde000 {
    public static void main(String[] args) {
        String filePath = "1213/2324324/nihoad的哥*回11111    1个吧.tx1t";
        String fileName = filePath.substring(filePath.lastIndexOf("/"));
        System.out.println(fileName);

        String newFileName = null;
        //txt pdf epub doc docx mobi azw3
        List<String> pattern = Arrays.asList("txt", "pdf", "epub", "docx", "doc", "azw3", "mobi");
        if (Objects.nonNull(fileName)) {
            boolean can = false;
            for (String p : pattern) {
                if (fileName.endsWith(p)) {
                    can = true;
                    break;
                }
            }
            if (!can) {
                return;
            }
            newFileName = fileName.substring(0, fileName.lastIndexOf("."));
            //去处空格
            newFileName = newFileName.replaceAll(" ", "");
        }
        System.out.println(newFileName);
    }
}
