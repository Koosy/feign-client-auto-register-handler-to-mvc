package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;

public class FeignPathVariableMapMethodArgumentResolver extends PathVariableMapMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
