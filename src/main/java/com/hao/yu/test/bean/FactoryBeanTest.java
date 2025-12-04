package com.hao.yu.test.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-15 18:18:59
 */
public class FactoryBeanTest implements FactoryBean<TaskTets> {

    @Override
    public TaskTets getObject() throws Exception {
        return new TaskTets("task", "任务描述");
    }

    @Override
    public Class<?> getObjectType() {
        return TaskTets.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
