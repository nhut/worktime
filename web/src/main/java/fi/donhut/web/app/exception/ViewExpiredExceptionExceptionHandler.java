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
package fi.donhut.web.app.exception;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;
import java.util.Map;

/**
 * Handler for JSF view expired.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public ViewExpiredExceptionExceptionHandler(final ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() {
        for (Iterator<ExceptionQueuedEvent> queuedEventIterator =
             getUnhandledExceptionQueuedEvents().iterator(); queuedEventIterator.hasNext();) {
            ExceptionQueuedEvent event = queuedEventIterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
                NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
                try {
                    // Push stuff to the request scope which can be use use in the page.
                    requestMap.put("currentViewId", vee.getViewId());
                    navigationHandler.handleNavigation(facesContext, null, "/logout?sessionExpired=true");
                    facesContext.renderResponse();
                } finally {
                    queuedEventIterator.remove();
                }
            }
        }

        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }
}
