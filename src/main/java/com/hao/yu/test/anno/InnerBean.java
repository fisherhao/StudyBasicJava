package com.hao.yu.test.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-06-27 22:28:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InnerBean {

	Class<?> classssssssssssss();

}
