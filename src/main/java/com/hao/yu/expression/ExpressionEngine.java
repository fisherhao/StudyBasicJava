package com.hao.yu.expression;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExpressionEngine {
    public static <T> T parse(String expression, Map<String, String> params, Class<T> clazz) {
        // 替换表达式中的变量为参数值
        String parsedExpression = replaceVariables(expression, params);
        // 计算表达式结果
        Object result = evaluateExpression(parsedExpression);
        // 转换结果类型
        return convertResult(result, clazz);
    }

    private static String replaceVariables(String expression, Map<String, String> params) {
        String result = expression;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replaceAll(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static Object evaluateExpression(String expression) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            return engine.eval(expression);
        } catch (ScriptException e) {
            throw new IllegalArgumentException("表达式解析失败: " + expression, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertResult(Object result, Class<T> clazz) {
        if (clazz == Integer.class) {
            return (T) Integer.valueOf(result.toString());
        } else if (clazz == Double.class) {
            return (T) Double.valueOf(result.toString());
        } else if (clazz == Float.class) {
            return (T) Float.valueOf(result.toString());
        } else if (clazz == BigDecimal.class) {
            return (T) new BigDecimal(result.toString());
        } else {
            throw new IllegalArgumentException("不支持的目标类型: " + clazz.getName());
        }
    }


    public static void main(String[] args) {
        // 测试用例：a=2, b=3, c=4，表达式 a+b*c+(a+b)*c 应计算为 2+3*4+(2+3)*4 = 2+12+20=34
        String expression = "a+b*c+(a+b)*c";
        Map<String, String> params = new HashMap<>();
        params.put("a", "2");
        params.put("b", "3");
        params.put("c", "4");

        try {
            // 测试 Double 类型
            Double doubleResult = parse(expression, params, Double.class);
            System.out.println("Double 类型结果: " + doubleResult); // 应输出 34.0

            // 测试 BigDecimal 类型
            BigDecimal bigDecimalResult = parse(expression, params, BigDecimal.class);
            System.out.println("BigDecimal 类型结果: " + bigDecimalResult); // 应输出 34
        } catch (Exception e) {
            System.out.println("测试失败: " + e.getMessage());
        }
    }
}