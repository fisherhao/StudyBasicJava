package com.hao.yu.test.ha;

/**
 * 说明：打印数字，降12345转换为1万2千3百4十五
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月13日 星期二 22:44
 */
public class PrintNum {

    public static void main(String[] args) {
        printNum(12345);
        printNum(10005);
        printNum(10025);
        printNum(10000);
    }

    private static void printNum(int num) {

        char[] charArray = String.valueOf(num).toCharArray();

        String[] units = {"", "十", "百", "千", "万", "十万"};

        StringBuilder builder = new StringBuilder();
        boolean zeroFlag = false;
        for (int i = 0; i < charArray.length; i++) {
            //中间如果有多个0的case

            if (charArray[i] == '0') {
                zeroFlag = true;
            } else {
                // 如果前面有零，并且不是最高位，就补一个“零”
                if (zeroFlag && builder.length() > 0) {
                    builder.append("零");
                }
                zeroFlag = false;
                builder.append(charArray[i]).append(units[charArray.length - i - 1]);
            }
        }

        System.out.println(builder);
    }

}
