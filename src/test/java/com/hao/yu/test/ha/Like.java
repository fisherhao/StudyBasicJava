package com.hao.yu.test.ha;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年09月30日 星期三 22:34
 */
public class Like {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int a = 0, b = 0;

        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt() && !in.hasNextLine()) { // 注意 while 处理多个 case
            a = in.nextInt();
            b = in.nextInt();
            System.out.println(a + b);
        }
        String s = "";
        //读取scanner屏幕数据

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String temp = br.readLine();
            if (temp != null) {
                s = s + temp;
            }

        } catch (Exception e) {

        }
        System.out.println("结果数");

        System.out.println(s);
        System.out.println("结果数");

        int like = 0;
        int comment = 0;
        String[] split = String.valueOf(b).split(" ");
        for (int i = 0; i < a; i++) {
            Integer num = Integer.valueOf(split[i]);
            switch (num) {
                case 1:
                    comment++;
                    break;
                case 2:
                    like++;
                    break;
                case 3:
                    if (comment == like) {
                        like++;
                        comment++;
                    } else if (comment > like) {
                        comment++;
                    } else {
                        like++;
                    }
                    break;
                case 4:
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + num);

            }

        }
        System.out.println(comment + " " + like);

    }
}
