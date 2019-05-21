package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;

import java.util.List;

public class FeignRequestPartMethodArgumentResolver extends RequestPartMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    public FeignRequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
