package me.koosy.boot.feign.resolver;

import me.koosy.boot.feign.ClassUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * FeignClient 注解类参数返回值处理器，统一按 @ResponseBody 方式处理
 */
public class FeignRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor implements DefaultMethodArgumentResolverSupportable {

    public FeignRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Class<?> declaringClass = returnType.getMethod().getDeclaringClass();
        Class feignClientMarkClass = ClassUtil.getFeignClientMarkClass(declaringClass);
        return feignClientMarkClass != null;
    }
}
