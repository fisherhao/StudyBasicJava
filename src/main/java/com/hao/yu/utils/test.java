package com.hao.yu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月19日 星期六 22:02
 */
public class test {

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = reader.readLine();
        int hang = Integer.parseInt(reader.readLine());

        String[] arr = s.split(" ");

        int like = 0;
        int comment = 0;
        for (int i = 0; i < hang; i++) {
            Integer num = Integer.valueOf(arr[i]);

            switch (num) {
                case 1:
                    comment++;
                    break;
                case 2:
                    like++;
                    break;
                case 3:
                    if (like > comment) {
                        like++;
                    } else if (comment > like) {
                        like++;
                    } else {
                        like++;
                        comment++;
                    }
                    break;
                case 4:
                    break;
                default:
                    break;
            }
            System.out.println(comment + " " + like);
        }
    }
}

