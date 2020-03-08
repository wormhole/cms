package net.stackoverflow.cms.config;

import net.stackoverflow.cms.constant.UploadConst;
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

    /**
     * 静态文件路径映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern(UploadConst.PREFIX + "/**")) {
            String path = null;
            if (SysUtils.isWin()) {
                path = UploadConst.UPLOAD_PATH_WINDOWS;
            } else {
                path = UploadConst.UPLOAD_PATH_LINUX;
            }
            registry.addResourceHandler(UploadConst.PREFIX + "/**").addResourceLocations("file:" + path);
        }
    }
}
