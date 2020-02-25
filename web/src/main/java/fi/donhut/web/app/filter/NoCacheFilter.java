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
package fi.donhut.web.app.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter to prevent browser from caching resources (images, css, js, ...).
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class NoCacheFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(NoCacheFilter.class);

    @Override
    public void init(final FilterConfig config) {
        LOG.debug("{} initialized.", this.getClass().getSimpleName());
    }

    @Override
    public void destroy() {
        LOG.debug("{} destroyed.", this.getClass().getSimpleName());
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String jsfResourcesUrl = request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER;
        if (!request.getRequestURI().startsWith(jsfResourcesUrl)) { // Skip JSF resources (CSS/JS/Images/etc)
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
