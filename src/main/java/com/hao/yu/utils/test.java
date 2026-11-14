package com.hao.yu.utils;

import java.io.IOException;
import java.util.Scanner;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月19日 星期六 22:02
 */
public class test {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        // 处理多组测试用例（两行一组）
        while (sc.hasNextLine()) {
            // 1. 读取第一行（操作序列）
            String line1 = sc.nextLine().trim();
            if (line1.isEmpty()) {
                break; // 遇到空行直接退出
            }

            // 2. 读取第二行（要执行的操作数目）
            if (!sc.hasNextLine()) break;
            String line2 = sc.nextLine().trim();
            if (line2.isEmpty()) break;

            int hang = Integer.parseInt(line2);

            // 3. 切分第一行的数据
            String[] arr = line1.split("\\s+");

            int like = 0;
            int comment = 0;

            // 防止给的数目大于实际数组长度，取最小值
            int loopCount = Math.min(hang, arr.length);

            for (int i = 0; i < loopCount; i++) {
                int num = Integer.parseInt(arr[i]);

                // 4. 核心业务逻辑
                switch (num) {
                    case 1:
                        comment++; // 看到评论就评论
                        break;
                    case 2:
                        like++;    // 看到点赞就点赞
                        break;
                    case 3:
                        // 既点赞又评论
                        like++;
                        comment++;
                        break;
                    case 4:
                        // 什么都不做
                        break;
                    default:
                        break;
                }
            }

            // 5. 输出这一组最终的结果
            System.out.println("最终状态 -> 评论数: " + comment + ", 点赞数: " + like);
        }
        sc.close();
    }
}

