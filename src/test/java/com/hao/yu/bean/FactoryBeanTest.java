package com.hao.yu.bean;

import com.hao.yu.test.bean.Task;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-01-15 18:22:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FactoryBeanTest {

	@Autowired
	private Task task1;

	@Autowired
	private Task task2;

	@org.junit.Test
	public void  testBean1(){
		System.out.println("FactoryBean 任务1");
		System.out.println(task1.getTaskDesc());
		System.out.println(task1);
		System.out.println("FactoryBean 任务12");
	}

	@org.junit.Test
	public void testBean2() {

		System.out.println("FactoryBean 任务2");
		System.out.println(task2.getTaskDesc());
		System.out.println(task2.getTaskName());
		System.out.println(task2);
	}
}
