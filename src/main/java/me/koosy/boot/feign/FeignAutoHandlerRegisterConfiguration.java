package me.koosy.boot.feign;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeignAutoHandlerRegisterConfiguration implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        Map<String, Object> defaultAttrs = metadata
                .getAnnotationAttributes(EnableFeignAutoHandlerRegister.class.getName(), true);

        if (defaultAttrs == null || (!defaultAttrs.containsKey("value") && !defaultAttrs.containsKey("basePackages")))
            throw new IllegalArgumentException("basePackages not found");

        //获取扫描包路径
        Set<String> basePackages = getBasePackages(metadata);
        //生成BeanDefinition并注册到容器中
        BeanDefinitionBuilder mappingBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(FeignAutoHandlerRegisterHandlerMapping.class);
        mappingBuilder.addConstructorArgValue(basePackages);
        registry.registerBeanDefinition("feignAutoHandlerRegisterHandlerMapping", mappingBuilder.getBeanDefinition());

        BeanDefinitionBuilder processBuilder = BeanDefinitionBuilder.genericBeanDefinition(FeignReturnValueWebMvcConfigurer.class);
        registry.registerBeanDefinition("feignReturnValueWebMvcConfigurer", processBuilder.getBeanDefinition());

    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableFeignAutoHandlerRegister.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


}
