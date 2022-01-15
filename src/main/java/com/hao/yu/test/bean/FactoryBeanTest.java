package com.hao.yu.test.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-01-15 18:18:59
 */
public class FactoryBeanTest implements FactoryBean<Task> {

	@Override
	public Task getObject() throws Exception {
		return new Task("task", "任务描述");
	}

	@Override
	public Class<?> getObjectType() {
		return Task.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
