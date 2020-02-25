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

import fi.donhut.web.Constants;
import fi.donhut.web.view.misc.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Login filter, every request that requires authentication goes though this filter.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class LoginFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);

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
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        final String logoutUrl = httpServletRequest.getContextPath() + Constants.SERVLET_LOGOUT;
        final UserManager userManager = (UserManager) httpServletRequest
                .getSession().getAttribute(Constants.CONTEXT_ATTRIBUTE_USER_MANAGER);
        if (userManager != null) {
            if (userManager.isLoggedIn()) {
                LOG.trace("User '{}' is still logged in.", userManager.getUsername());
                // user is logged in, continue request
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            LOG.trace("User is not logged in, redirect to logout.");
            httpServletResponse.sendRedirect(logoutUrl);
            return;
        }
        LOG.trace("User is not logged in, redirect to logout.");
        httpServletResponse.sendRedirect(logoutUrl);
    }
}
