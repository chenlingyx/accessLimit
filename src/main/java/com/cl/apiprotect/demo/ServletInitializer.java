package com.cl.apiprotect.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/***
 * @ClassName: ServletInitializer
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:59
 * @version : V1.0.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }
}
