package net.stackoverflow.cms.config;

import net.stackoverflow.cms.constant.UploadPathConst;
import net.stackoverflow.cms.util.SysUtils;
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

    private static final String PATH = UploadPathConst.PREFIX + "/**";

    /**
     * 静态文件路径映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(PATH)) {
            String path = SysUtils.pwd() + UploadPathConst.UPLOAD_PATH;
            registry.addResourceHandler(PATH).addResourceLocations("file:" + path);
        }
    }
}
