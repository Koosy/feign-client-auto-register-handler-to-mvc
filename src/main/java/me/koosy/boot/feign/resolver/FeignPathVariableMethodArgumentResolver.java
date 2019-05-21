package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

public class FeignPathVariableMethodArgumentResolver extends PathVariableMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
