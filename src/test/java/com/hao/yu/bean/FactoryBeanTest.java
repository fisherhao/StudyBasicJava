package com.hao.yu.bean;

import com.hao.yu.test.bean.TaskTets;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-15 18:22:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FactoryBeanTest {

    @Autowired
    private TaskTets taskTets1;

    @Autowired
    private TaskTets taskTets2;

    @org.junit.Test
    public void testBean1() {
        System.out.println("FactoryBean 任务1");
        System.out.println(taskTets1.getTaskDesc());
        System.out.println(taskTets1);
        System.out.println("FactoryBean 任务12");
    }

    @org.junit.Test
    public void testBean2() {

        System.out.println("FactoryBean 任务 2");
        System.out.println(taskTets2.getTaskDesc());
        System.out.println(taskTets2.getTaskName());
        System.out.println(taskTets2);
    }
}
