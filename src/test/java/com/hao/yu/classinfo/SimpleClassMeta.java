package com.hao.yu.classinfo;

import java.io.IOException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

/**
 * 说明：Spring获取类的元数据
 *
 * @author： Fisher.Hao
 * @date： 2022-04-30 16:50:31
 */
public class SimpleClassMeta {
	public static void main(String[] args) throws IOException {

		SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory();

		MetadataReader metadataReader = simpleMetadataReaderFactory.getMetadataReader("com.hao.yu.classinfo.SimpleClassImpl");

		//类信息
		ClassMetadata classMetadata = metadataReader.getClassMetadata();

		//获取类名称
		System.out.println(classMetadata.getClassName());
		System.out.println(classMetadata.getSuperClassName());
		System.out.println(classMetadata.getEnclosingClassName());

		//获取所有的接口
		String[] interfaceNames = classMetadata.getInterfaceNames();
		for (String interfaceName : interfaceNames) {
			System.out.println(interfaceName);
		}

		//获取注解等信息
		AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

		Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
		for (String annotationType : annotationTypes) {
			System.out.println(annotationType);
		}
	}
}
