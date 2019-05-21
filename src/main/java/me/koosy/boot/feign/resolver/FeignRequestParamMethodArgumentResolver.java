package me.koosy.boot.feign.resolver;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

public class FeignRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    public FeignRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        super(beanFactory, useDefaultResolution);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
