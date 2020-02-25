/*
 * Copyright since 2018 Nhut Do <mr.nhut@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.donhut.web.app.config;

import fi.donhut.web.app.filter.LoginFilter;
import fi.donhut.web.app.filter.NoCacheFilter;
import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Filter creation configurations.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        final FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/secured/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
        final FilterRegistrationBean<NoCacheFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new NoCacheFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
    
    @Bean
    public FilterRegistrationBean<RewriteFilter> rewriteFilter() {
        final FilterRegistrationBean<RewriteFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RewriteFilter());
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
                DispatcherType.ASYNC, DispatcherType.ERROR));
        registration.addUrlPatterns("/*");
        return registration;
    }
}
