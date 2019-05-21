package me.koosy.boot.feign.resolver;

import me.koosy.boot.feign.ClassUtil;
import org.springframework.core.MethodParameter;

public interface DefaultMethodArgumentResolverSupportable {

    default boolean supportsParameter(MethodParameter parameter, Supportable able) {
        MethodParameter originalMethod = ClassUtil.getFeignInterfaceMethodParameter(parameter);
        if (originalMethod == null)
            return false;
        return able.support(originalMethod);
    }

    @FunctionalInterface
    interface Supportable {
        boolean support(MethodParameter parameter) ;
    }

}
