package dima.config.common.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceFactory {
	
	private static Map<Class<?>, Object> services=new ConcurrentHashMap<>();
	
	public static void registerService(Class<?> serviceClassName, Object instance){
		services.put(serviceClassName, instance);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getService(Class<T> serviceClassName){
		Object obj = services.get(serviceClassName);
		return (T)obj;
	}
	
	

}
