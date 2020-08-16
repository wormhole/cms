package net.stackoverflow.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置类
 *
 * @author 凉衫薄
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean("threadPool")
    public Executor getAsyncExecutor() {
        return new ThreadPoolExecutor(10, 30, 60, TimeUnit.MINUTES, new LinkedBlockingDeque<>(10));
    }
}
