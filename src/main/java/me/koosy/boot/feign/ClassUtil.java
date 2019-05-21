package me.koosy.boot.feign;

import feign.Target;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

public class ClassUtil {
    
    /**
     * 查找指定类注解FeignClient的接口
     * @param beanType 指定类
     * @return 返回该类标注了 FeignClient 的接口，如果未找到返回 null
     */
    public static Class getFeignClientMarkClass(Class beanType) {
        //获取该Bean的所有接口
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(beanType);
        Class<?> feignClientMarkClass = null;
        //遍历接口找到注解了FeignClient的接口
        for (Class<?> anInterface : interfaces) {
            boolean isFeignClientMarkClass = AnnotatedElementUtils.hasAnnotation(anInterface, FeignClient.class);
            if (isFeignClientMarkClass) {
                feignClientMarkClass = anInterface;
                break;
            }
        }
        return feignClientMarkClass;
    }

    /**
     * 判断指定类是否是Feign的代理类
     * @param beanType 要判断的指定类
     * @return 如果是 FeignTarget 返回 true，否则返回 false
     */
    public static boolean isFeignTargetClass(Class beanType) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(beanType);
        for (Class<?> anInterface : interfaces) {
            if (anInterface.equals(Target.class))
                return true;
        }
        return false;
    }

    /**
     * 执行无参数方法
     * @param beanType 查找方法的指定类
     * @param target 执行方法的目标类
     * @param methodName 方法名
     * @param <T>
     * @return 方法执行结果
     */
    public static <T> T invokeNoParameterMethod(Class beanType, Object target ,String methodName) {
        try {
            Method returnValueHandlersMethod = beanType.getDeclaredMethod(methodName);
            returnValueHandlersMethod.setAccessible(true);
            return (T) returnValueHandlersMethod.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取该MethodParameter对应FeignClient注解接口对应方法MethodParameter
     * @param parameter
     * @return
     */
    public static MethodParameter getFeignInterfaceMethodParameter(MethodParameter parameter) {
        Method method = parameter.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        Class feignClientMarkClass = ClassUtil.getFeignClientMarkClass(declaringClass);
        if (feignClientMarkClass == null)
            return null;

        try {
            Method originalMethod = feignClientMarkClass.getMethod(method.getName(), method.getParameterTypes());
            //根据此 Method 生成对应 MethodParameter 方法
            MethodParameter originalParameter = new MethodParameter(originalMethod, parameter.getParameterIndex());
            return originalParameter;
        } catch (Exception ex) {
            return null;
        }
    }


}
