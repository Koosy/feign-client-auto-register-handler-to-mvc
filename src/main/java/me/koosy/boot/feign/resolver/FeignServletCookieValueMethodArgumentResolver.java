package me.koosy.boot.feign.resolver;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;

public class FeignServletCookieValueMethodArgumentResolver extends ServletCookieValueMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    public FeignServletCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
