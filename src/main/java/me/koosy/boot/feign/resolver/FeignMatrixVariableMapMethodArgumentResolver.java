package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver;

public class FeignMatrixVariableMapMethodArgumentResolver extends MatrixVariableMapMethodArgumentResolver implements DefaultMethodArgumentResolverSupportable {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
