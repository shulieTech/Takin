/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.app.conf;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 项目配置类
 *
 * @author shulie
 * @description
 * @create 2018/9/15 9:56
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer, EnvironmentAware {

    private Environment environment;

    /**
     * 配置拦截器
     *
     * @param registry 拦截器注册器
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:55
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeSource = "dev".equals(environment.getProperty("env")) ? new String[] {"/**/*"} : new String[] {
            "/**/order/*",
            "/**/api/confcenter/wbmnt/query/bwlist",
            "/**/api/confcenter/wbmnt/query/bwlistmetric",
            "/**/api/loaddata/test",
            "/**/api/confcenter/interface/query/needUpload",
            "/**/api/confcenter/interface/add/interfaceData",
            "/**/api/confcenter/shadowTableConfig/queryAppShadowTableConfig",
            "/**/api/pressureready/builddata/update/scriptstatus",
            "/**/api/confcenter/applicationmnt/update/applicationAgent",
            "/**/api/preventcheat/applicationInfo/uploadInfo",
            "/**/api/preventcheat/applicationConfig/queryConfig",
            "/**/api/confcenter/pressureTime/**",
            "/**/api/confcenter/linkmnt/query/linkHead",
            "/**/api/confcenter/linkmnt/query/secondLinkByModule",
            "/**/api/isolation/query/rockemtMqIsoQuery",
            "/**/api/shadow/**",
            "/**/fonts/*",
            "/**/*.css",
            "/**/*.js",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.jpg",
            "/**/*.jpeg",
            "/**/*.html"
        };
    }

    /**
     * 配置路径匹配
     *
     * @description
     * @author shulie
     * @create 2018/9/15 9:56
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {

    }

    /**
     * 配置mediaType
     *
     * @param configurer
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:57
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
        configurer.mediaType("html", MediaType.TEXT_HTML);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {

    }

    /**
     * 配置静态资源拦截器
     *
     * @param registry
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:57
     */
    //此方法用来专门注册一个Handler，来处理静态资源的，例如：图片，js，css等
    //当请求http://{domain}/resource/1.png时，会把/WEB-INF/resources/1.png返回
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

    }

    /**
     * 配置视图转发
     *
     * @param registry
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:58
     */
    //这是访问${domain}/login时，会直接返回index页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("forward:/index.html");
    }

    /**
     * 配置视图解析器
     *
     * @param viewResolverRegistry
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:59
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {

    }

    /**
     * 配置参数解析器
     *
     * @param list
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:59
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    /**
     * 配置返回值处理器
     *
     * @param list
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 9:59
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    /**
     * 配置httpMessage转发器
     *
     * @param list
     * @return void
     * @description
     * @author shulie
     * @create 2018/9/15 10:00
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
        list.add(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。
        // Include.NON_NULL 属性为NULL 不序列化,就是为null的字段不参加序列化
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //忽略未知属性 防止解析报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(objectMapper);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        return converter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
