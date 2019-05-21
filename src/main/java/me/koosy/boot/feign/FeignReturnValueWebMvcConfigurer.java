package me.koosy.boot.feign;

import me.koosy.boot.feign.resolver.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

public class FeignReturnValueWebMvcConfigurer implements BeanFactoryAware, InitializingBean {

    private WebMvcConfigurationSupport webMvcConfigurationSupport;
    private ConfigurableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.beanFactory = (ConfigurableBeanFactory) beanFactory;
            this.webMvcConfigurationSupport = beanFactory.getBean(WebMvcConfigurationSupport.class);
        }
    }

    public void afterPropertiesSet() throws Exception {

        if (this.beanFactory == null) {
            throw new IllegalStateException("beanFactory cannot use");
        }
        if (this.webMvcConfigurationSupport == null) {
            throw new IllegalStateException("webMvcConfigurationSupport cannot use");
        }

        try {
            Class<WebMvcConfigurationSupport> configurationSupportClass = WebMvcConfigurationSupport.class;
            List<HttpMessageConverter<?>> messageConverters = ClassUtil.invokeNoParameterMethod(configurationSupportClass, webMvcConfigurationSupport, "getMessageConverters");
            List<HandlerMethodReturnValueHandler> returnValueHandlers = ClassUtil.invokeNoParameterMethod(configurationSupportClass, webMvcConfigurationSupport, "getReturnValueHandlers");
            List<HandlerMethodArgumentResolver> argumentResolverHandlers = ClassUtil.invokeNoParameterMethod(configurationSupportClass, webMvcConfigurationSupport, "getArgumentResolvers");

            //将所有返回值都当作 @ResponseBody 注解进行处理
            returnValueHandlers.add(new FeignRequestResponseBodyMethodProcessor(messageConverters));

            //因为接口中的注解无法识别，所以需要设置对应的注解 ArgumentResolver
            argumentResolverHandlers.add(new FeignExpressionValueMethodArgumentResolver(this.beanFactory));
            argumentResolverHandlers.add(new FeignMatrixVariableMapMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignMatrixVariableMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignPathVariableMapMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignPathVariableMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignRequestAttributeMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignRequestHeaderMapMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignRequestHeaderMethodArgumentResolver(this.beanFactory));
            argumentResolverHandlers.add(new FeignRequestParamMapMethodArgumentResolver());
            argumentResolverHandlers.add(new FeignRequestParamMethodArgumentResolver(this.beanFactory, false));
            argumentResolverHandlers.add(new FeignRequestPartMethodArgumentResolver(messageConverters));
            argumentResolverHandlers.add(new FeignRequestResponseBodyMethodProcessor(messageConverters));
            argumentResolverHandlers.add(new FeignServletCookieValueMethodArgumentResolver(this.beanFactory));
            argumentResolverHandlers.add(new FeignServletModelAttributeMethodProcessor(false));
            argumentResolverHandlers.add(new FeignSessionAttributeMethodArgumentResolver());

        } catch (Exception e) {
            throw new IllegalStateException("Annotation ArgumentResolver add fail");
        }

        //通过反射调用WebMvcConfigurationSupport方法的操作不用担心会消失，因为这几个接口都是 protected 的，所以他们是作为扩展接口来提供的
    }


}
