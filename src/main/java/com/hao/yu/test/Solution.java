package com.hao.yu.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月24日 星期六 22:10
 */
class Solution {

    private static final List list = new ArrayList();

    public static void main(String[] args) {
        Solution solution = new Solution();

        solution.addData(new Data("test123"));
        solution.addData(new Data(123));
        solution.addData(new Data("test456"));

        List test = solution.searchData("test");
        System.out.println(test);
        List test1 = solution.searchData(123);
        System.out.println(test1);
    }

    public void addData(Data data) {
        list.add(data);
    }

    public List searchData(Object keyword) {

        if (keyword == null) {
            return null;
        }

        //如果keyword是一个String类型

        if (keyword.getClass().getSimpleName().equals(String.class.getSimpleName())) {
            List<String> real = new ArrayList<>();
            for (Object o : list) {
                if (((Data) o).getValue().getClass()
                    .getSimpleName()
                    .equals(String.class.getSimpleName())) {
                    if (((String) ((Data) o).getValue()).contains(keyword.toString())) {
                        real.add(((String) ((Data) o).getValue()));
                    }
                }
            }
            return real;

        }
        if (keyword.getClass().getSimpleName().equals(Integer.class.getSimpleName())) {
            List<Integer> real = new ArrayList<>();
            for (Object o : list) {
                if (((Data) o).getValue().getClass()
                    .getSimpleName()
                    .equals(Integer.class.getSimpleName())) {
                    if ((((Integer) ((Data) o).getValue())).equals((Integer) keyword)) {
                        real.add((Integer) ((Data) o).getValue());
                    }
                }
            }
            return real;

        }

        return null;
    }
}

class Data<T> {

    private T value;

    public Data(T data) {
        this.value = data;
    }

    /**
     * Getter method for property <tt>data</tt>.
     *
     * @return property value of data
     */
    public T getValue() {
        return value;
    }

    /**
     * Setter method for property <tt>data</tt>.
     *
     * @param value
     *     value to be assigned to property
     */
    public void setValue(T value) {
        this.value = value;
    }
}
