package com.hao.yu.test.anno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-06-27 22:32:40
 */
public class TestAnno {

	public static void main(String[] args) {

		final String BASE_PACKAGE = "com.hao.yu.**.*";
		final String RESOURCE_PATTERN = ".class";
		Map<String, Class> handlerMap = new HashMap<>();

		Map<String, List<Object>> map = new HashMap<>();

		//spring工具类，可以获取指定路径下的全部类
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(
					BASE_PACKAGE) + RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(pattern);
			//MetadataReader 的工厂类
			MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {

				MetadataReader reader = readerfactory.getMetadataReader(resource);

				String classname = reader.getClassMetadata().getClassName();
				Class<?> clazz = Class.forName(classname);
				System.out.println();
				System.out.println(clazz.getSimpleName());
				System.out.println(clazz.isInterface());
				System.out.println(clazz.isMemberClass());
				System.out.println(clazz.isLocalClass());
				System.out.println();
				InnerBean anno = clazz.getAnnotation(InnerBean.class);

				List<Object> objects;
				if (anno != null) {
					//将注解中的类型值作为key，对应的类作为 value
					handlerMap.put(classname, clazz);

					objects = map.get(anno.classssssssssssss().getName());

					if (Objects.isNull(objects)) {
						map.put(anno.classssssssssssss().getName(), new ArrayList<>());
					}
					map.get(anno.classssssssssssss().getName()).add(clazz);
				}

			}
			System.out.println(JSON.toJSONString(handlerMap));
			System.out.println(JSON.toJSONString(map));

		} catch (IOException | ClassNotFoundException e) {
		}
	}

}

