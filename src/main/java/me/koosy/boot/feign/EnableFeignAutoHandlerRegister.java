package me.koosy.boot.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@ConditionalOnClass(DispatcherServlet.class)
@Import(FeignAutoHandlerRegisterConfiguration.class)
public @interface EnableFeignAutoHandlerRegister {

    /**
     * basePackages 的别名属性
     * @return
     */
    String[] value() default {};

    /**
     * FeignClient 注解的基础包扫描路径
     * @return
     */
    String[] basePackages() default {};

}
