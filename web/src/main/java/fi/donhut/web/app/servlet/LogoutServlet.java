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
package fi.donhut.web.app.servlet;

import fi.donhut.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Logout servlet.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class LogoutServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        final HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        final String sessionExpired = request.getParameter("sessionExpired");
        if (sessionExpired != null) {
            LOG.info("User session out, redirect to login with parameter '{}'.", sessionExpired);
            final String loginUrlWithExpireInfo = Constants.VIEW_LOGIN + "?sessionExpired=" + sessionExpired;
            response.sendRedirect(loginUrlWithExpireInfo);
            return;
        }
        response.sendRedirect(Constants.VIEW_LOGIN);
    }
}
