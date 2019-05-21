package me.koosy.boot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class FeignAutoHandlerRegisterHandlerMapping extends RequestMappingHandlerMapping {

    Set<String> basePackages;

    private RequestMappingInfo.BuilderConfiguration mappingInfoBuilderConfig;
    private boolean isGetSupperClassConfig = false;

    public FeignAutoHandlerRegisterHandlerMapping(Set<String> basePackages) {
        super();
        this.basePackages = basePackages;
    }

    @Override
    public int getOrder() {
        return super.getOrder() - 1;
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        //注解了 @FeignClient 的接口, 并且是这个接口的实现类

        //传进来的可能是接口，比如 FactoryBean 的逻辑
        //FeignClient会通过 FactoryBean 生成代理对象，传进来的Class就是接口的Class
        if (beanType.isInterface())
            return false;

        //是否是Feign的代理类，如果是则不支持
        if (ClassUtil.isFeignTargetClass(beanType))
            return false;

        //是否在包范围内，如果不在则不支持
        if (!isPackageInScope(beanType))
            return false;

        //是否有标注了 @FeignClient 的接口
        Class feignClientMarkClass = ClassUtil.getFeignClientMarkClass(beanType);
        return feignClientMarkClass != null;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        Class feignClientMarkClass = ClassUtil.getFeignClientMarkClass(handlerType);
        try {
            //查找到原始接口的方法，获取其注解解析为 requestMappingInfo
            Method originalMethod = feignClientMarkClass.getMethod(method.getName(), method.getParameterTypes());
            RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(originalMethod, RequestMapping.class);
            RequestMappingInfo info = createRequestMappingInfo(requestMapping, null);

            if (info != null) {
                FeignClient feignClient = AnnotatedElementUtils.findMergedAnnotation(feignClientMarkClass, FeignClient.class);
                RequestMappingInfo typeInfo = createRequestMappingInfo(feignClient);

                if (typeInfo != null)
                    info = typeInfo.combine(info);
            }
            return info;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    protected RequestMappingInfo createRequestMappingInfo(
            FeignClient feignClient) {

        String path = feignClient.path();
        String[] paths = new String[]{path};

        RequestMappingInfo.Builder builder = RequestMappingInfo
                .paths(resolveEmbeddedValuesInPatterns(paths));

        //通过反射获得 config
        if (!isGetSupperClassConfig) {
            try {
                Field field = RequestMappingHandlerMapping.class.getDeclaredField("config");
                field.setAccessible(true);
                RequestMappingInfo.BuilderConfiguration config = (RequestMappingInfo.BuilderConfiguration) field.get(this);
                this.mappingInfoBuilderConfig = config;
            } catch (NoSuchFieldException ex) {
            } catch (IllegalAccessException ex) {
            } finally {
                isGetSupperClassConfig = true;
            }
        }

        if (this.mappingInfoBuilderConfig != null)
            return builder.options(this.mappingInfoBuilderConfig).build();
        else
            return builder.build();
    }

    /**
     * 判断指定类是否在包范围内
     * @param beanType 指定类
     * @return 如果在范围内返回 true，否则返回 false
     */
    private boolean isPackageInScope(Class beanType) {
        //是否在包路径内
        String packageName = ClassUtils.getPackageName(beanType);
        boolean isPackageScope = false;
        for (String basePackage : basePackages) {
            if (packageName.startsWith(basePackage)) {
                isPackageScope = true;
                break;
            }
        }
        return isPackageScope;
    }

}
