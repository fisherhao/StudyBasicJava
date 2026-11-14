package com.hao.yu.utils;

import java.util.Scanner;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月24日 星期六 22:00
 */
public class Test2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {

            int comment = 0;
            int like = 0;
            String a = sc.nextLine();
            String b = sc.nextLine();

            String[] s = b.split(" ");

            for (String string : s) {

                switch (string) {
                    case "1":
                        comment++;
                        break;
                    case "2":
                        like++;
                        break;
                    case "3":
                        if (comment > like) {
                            comment++;
                        } else if (comment == like) {
                            comment++;
                            like++;
                        } else {
                            like++;
                        }
                        break;
                    case "4":
                        break;
                    default:
                }

            }
            System.out.println(comment + " " + like);
        }
    }
}
