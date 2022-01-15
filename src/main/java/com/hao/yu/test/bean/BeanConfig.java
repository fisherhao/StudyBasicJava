package com.hao.yu.test.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-01-15 18:26:03
 */
@Configuration
public class BeanConfig {

	/**
	 * 最终的bean是FactoryBean中的泛型
	 *
	 * @return
	 */
	@Bean
	public FactoryBeanTest task1() {
		FactoryBeanTest test = new FactoryBeanTest();
		return test;
	}

	@Bean
	public FactoryBeanTest task2() {
		FactoryBeanTest test = new FactoryBeanTest();
		return test;
	}
}
