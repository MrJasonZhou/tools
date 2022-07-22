/**  */
package com.jasonzhou.tool.sag.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;

/**
 * @author Jason Zhou
 *
 */
public class SpringUtil {

	private static ApplicationContext ac;

	/**
	 * アプリケーションコンテキスト を設定する
	 *
	 * @param ac アプリケーションコンテキスト
	 */
	public static void setApplicationContext(ApplicationContext ac) {
		SpringUtil.ac = ac;
	}
	
	/**
	 * Beanを取得する
	 * 
	 * @param name	Beanの名称
	 * @param requiredType	Beanのクラス
	 * @return	Beanのインスタンス
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return ac.getBean(name, requiredType);
	}

	/**
	 * Beanを取得する
	 * 
	 * @param requiredType	Beanのクラス
	 * @return	Beanのインスタンス
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return ac.getBean(requiredType);
	}

	/**
	 * 指定された親タイプのサブクラスのセットを取得する
	 * 
	 * @param <T>	サブクラス
	 * @param type	親タイプ
	 * @return　親タイプのサブクラスのセット
	 */
	public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
	    return new resourceScanner().findResource(type);
	}

	private static class resourceScanner extends ClassPathScanningCandidateComponentProvider {
	    public <T> Set<Class<? extends T>> findResource(Class<T> type) {
	        Set<Class<? extends T>> result = new HashSet<>();

	        super.setEnvironment(new StandardEnvironment());
	        super.setResourceLoader(new DefaultResourceLoader());
	        String basePackage = type.getPackage().getName();
	        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	        String resourcePattern = "**/*.class";
	        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX 
	        //+ resolveBasePackage(basePackage) + '/' 
       		+ resourcePattern;

	        String baseClassName = type.getName();
	        try {
	            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
	            for (Resource resource : resources) {
	                ClassMetadata classMetadata = getMetadataReaderFactory().getMetadataReader(resource).getClassMetadata();
	                if (baseClassName.equals(classMetadata.getSuperClassName())) {
	                    Class<?> aClass = Class.forName(classMetadata.getClassName());
	                    result.add((Class<? extends T>) aClass);
	                }
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	}
	
	
}
