package com.zjut.lsw;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/logout").setViewName("login");
        registry.addViewController("/saler/index").setViewName("saler/index");
        registry.addViewController("/saler/porking").setViewName("saler/parking");
        registry.addViewController("/saler/invoice").setViewName("saler/invoice");
        registry.addViewController("/saler/ownerinfo").setViewName("saler/ownerinfo");
        registry.addViewController("/saler/ordermanage").setViewName("saler/ordermanage");
        registry.addViewController("/saler/information").setViewName("saler/information");
        registry.addViewController("/developer/index").setViewName("developer/index");
        registry.addViewController("/developer/information").setViewName("developer/information");
        registry.addViewController("/developer/porking").setViewName("developer/parking");
        registry.addViewController("/developer/invoice").setViewName("developer/invoice");
        registry.addViewController("/developer/contract").setViewName("developer/contract");
        registry.addViewController("/developer/ownerinfo").setViewName("developer/ownerinfo");
        registry.addViewController("/developer/community").setViewName("developer/community");
        registry.addViewController("/developer/quotation").setViewName("developer/quotation");
        registry.addViewController("/admin/index").setViewName("admin/index");
    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/","/login","/user/login","/user/toRegister","/user/register",
//                        "/css/**","/js/**", "/img/**","/font-awesome/**","/fonts/**");
//    }
}
