package net.stackoverflow.cms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置类
 *
 * @author 凉衫薄
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${application.upload.path}")
    private String path;
    @Value("${application.upload.prefix}")
    private String prefix;

    /**
     * 静态文件路径映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(prefix + "/**")) {
            registry.addResourceHandler(prefix + "/**").addResourceLocations("file:" + path);
        }
    }
}
