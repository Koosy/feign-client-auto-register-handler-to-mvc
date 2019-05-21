package me.koosy.boot.feign.resolver;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;

public class FeignExpressionValueMethodArgumentResolver extends ExpressionValueMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    public FeignExpressionValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
