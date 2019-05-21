# feign-client-auto-register-handler-to-mvc

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

应用Spring Cloud微服务之后针对每个feign api模块除了实现其Service外，还需要提供对应的代理Controller以提供HTTP请求响应，所以此库提供了自动的Feign Service注入URL请求映射功能，无需编写代理Controller。

#### 如何使用

在 SpringApplication 启动类上配置`@EnableFeignAutoHandlerRegister`注解即可，代码如下

```
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.liying","com.jq"}, defaultConfiguration = FeignConfig.class)
@SpringBootApplication(scanBasePackages = {"com.liying","com.jq"})
@EnableFeignAutoHandlerRegister({"com.liying.game"})
public class SpringApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SpringApplication.class).run(args);
	}
}
```

注意：
1. `@EnableFeignAutoHandlerRegister`注解需提供完整的扫描包路径，否则可能会将其他导入的api模块也注入URL映射。
2. `FeignAutoHandlerRegisterHandlerMapping`优先级低于`RequestMappingHandlerMapping`，如果因为业务问题需自行编写Controller他的执行逻辑是优先于自动注入的映射的。
3. 基于`@FeignClient`自动注入的映射目前无法处理配置的`RequestBodyAdvice`和`ResponseBodyAdvice`。 
4. 如果编写了针对Controller的AOP拦截，这里因为是直接注入的映射所以也不在生效，如果需要拦截请自行编写对应扩展。

#### 实现原理

基于Spring `ImportBeanDefinitionRegistrar` 和 Spring MVC `RequestMappingHandlerMapping` 进行实现。

大致流程如下

1. SpringApplication 启动扫描到 `@EnableFeignAutoHandlerRegister` 根据此注解上的 `@Import` 注解执行 `FeignAutoHandlerRegisterConfiguration` 类逻辑。
    1. `FeignAutoHandlerRegisterConfiguration` 继承于 `ImportBeanDefinitionRegistrar` 会执行注册逻辑
2. 注册 `FeignAutoHandlerRegisterHandlerMapping` 和 `FeignReturnValueWebMvcConfigurer`
    1. `FeignReturnValueWebMvcConfigurer` 会注册参数解析器和返回值处理器到`RequestMappingHandlerAdapter`以提供针对`@FeignClient`注解的类所有方法都当作 `@ResponseBody`注解过。因为实现类无法继承接口的注解，所以此处又实现了一遍注解参数解析器。
    2. Spring IOC在初始化时会自动将 `FeignAutoHandlerRegisterHandlerMapping` 注册到 `DispatcherServlet` 的 `handlerMappings` 上。
        1. 实际是 `DispatcherServlet` 主动从 Spring IOC 容器中获取实现了 `HandlerMapping` 接口的类进行注册。

执行逻辑：全部执行逻辑都依托于Spring IOC Bean的初始化过程和 Spring MVC的执行逻辑。    

