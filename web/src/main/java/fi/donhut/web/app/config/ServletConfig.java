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

import fi.donhut.web.Constants;
import fi.donhut.web.app.servlet.LogoutServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Servlet creation configurations.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean logoutServlet() {
        final ServletRegistrationBean<LogoutServlet> bean =
                new ServletRegistrationBean<>(new LogoutServlet(), Constants.SERVLET_LOGOUT);
        bean.setLoadOnStartup(1);
        return bean;
    }
}
