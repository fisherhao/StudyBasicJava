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
	public void  testBean(){
		System.out.println("年代液晶的金额噶几飞洒就给了我");
		System.out.println(task1.getTaskDesc());
		System.out.println(task1);
	}

	@org.junit.Test
	public void  testBean1(){
		System.out.println("年代液晶的金额噶几飞洒就给了我");
		System.out.println(task2.getTaskDesc());
		System.out.println(task2);
	}
}
