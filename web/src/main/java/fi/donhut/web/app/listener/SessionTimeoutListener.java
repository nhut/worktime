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

package fi.donhut.web.app.listener;

import fi.donhut.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Listener to forward user back to logout page when session have been timeout.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class SessionTimeoutListener  implements PhaseListener {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTimeoutListener.class);

    private String getLogoutPath() {
        return Constants.SERVLET_LOGOUT;
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.getPartialViewContext().isAjaxRequest()
                || facesContext.getRenderResponse()) { // not ajax or too late
            return;
        }

        final HttpServletRequest request =
                (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (request.getDispatcherType() == DispatcherType.FORWARD
                && getLogoutPath().equals(request.getServletPath())) { // isLoginRedirection()
            final String redirect =
                    facesContext.getExternalContext().getRequestContextPath() + request.getServletPath();
            try {
                facesContext.getExternalContext().redirect(redirect);
            } catch (final IOException e) {
                LOG.error("Failed to make redirect to {}", redirect);
            }
        }
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        LOG.trace("{} afterPhase, event: {}.", this.getClass().getSimpleName(), event);
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
