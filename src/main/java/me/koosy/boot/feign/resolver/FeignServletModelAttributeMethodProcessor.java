package me.koosy.boot.feign.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

public class FeignServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor implements DefaultMethodArgumentResolverSupportable {

    public FeignServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DefaultMethodArgumentResolverSupportable.super.supportsParameter(parameter, super::supportsParameter);
    }
}
