package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;

public class FeignRequestHeaderMapMethodArgumentResolver extends RequestHeaderMapMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
