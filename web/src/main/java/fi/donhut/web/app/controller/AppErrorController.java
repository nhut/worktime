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

package fi.donhut.web.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Controller to handle application errors.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Controller
public class AppErrorController implements ErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(AppErrorController.class);

    @RequestMapping(value = "/error")
    public String handleError(final HttpServletRequest request, final Exception exception) {
        final FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (handleViewExpiredException(exception, currentInstance)) {
            final String expireTime = new SimpleDateFormat("dd.MM.yyyy").format(LocalDateTime.now());
            return "forward:/logout?sessionExpired=" + expireTime;
        }
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            final String forwardPage = handleStatusCodes(currentInstance, exception, status);
            if (forwardPage != null)
                return forwardPage;
        }
        LOG.trace("To error page...", exception);
        return "forward:/error.xhtml";
    }

    private static String handleStatusCodes(FacesContext currentInstance, Exception exception, Object status) {
        int statusCode = -1;
        try {
            statusCode = Integer.parseInt(status.toString());
        } catch (Exception e) {
            LOG.trace("Failed to parse status code.", e);
        }
        if (LOG.isWarnEnabled()) {
            final String errorMsg = exception == null ? "" : exception.getMessage();
            LOG.warn("Gone error, http code={}, error msg={}.", statusCode, errorMsg);
        }

        if(statusCode == HttpStatus.NOT_FOUND.value()) {
            return "forward:/error.xhtml";
        }
        if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
            if (currentInstance == null) {
                return "forward:/logout";
            }
            PartialViewContext pvc = currentInstance.getPartialViewContext();
            if(pvc.isAjaxRequest()) {
                return "logout";
            }
            return "forward:/logout";
        }
        LOG.error("Error occurred. Status code: {}", status, exception);
        return null;
    }

    private static boolean handleViewExpiredException(Exception exception, FacesContext currentInstance) {
        if (exception instanceof ViewExpiredException) {
            final ViewExpiredException viewExpiredException = (ViewExpiredException) exception;
            LOG.warn("User session expired. View id: {}. Message: {}",
                    viewExpiredException.getViewId(), viewExpiredException.getMessage());
            currentInstance.addMessage(null, new FacesMessage("Session expired!"));
            return true;
        }
        return false;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
