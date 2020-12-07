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
package fi.donhut.web.view.misc;

import fi.donhut.web.Constants;
import fi.donhut.web.client.phz.PhzAuthenticationApi;
import fi.donhut.web.model.User;
import fi.donhut.web.util.ResourceBundleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Manages user.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@SessionScoped
public class UserManager implements Serializable {

    private static final long serialVersionUID = 6790797927980253090L;
    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);

    @Inject
    private PhzAuthenticationApi phzAuthenticationService;

    private String username;
    private String password;

    private User currentUser;

    public String login() {
        try {
            currentUser = find(username, password);
            if (currentUser != null) {
                LOG.info("User '{} ({} {})' login successful.'", username, currentUser.getFirstName(), currentUser.getLastName());

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "You have been logged in. Remember to logout if you don't use anymore.", ""));
                return Constants.AFTER_LOGIN_REDIRECT;
            } else {
                LOG.info("login failed for '{}'", username);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed. Invalid or unknown credentials.", ""));

                return null;
            }

        } catch (RestClientException e) {
            LOG.error("Server side error. Failed to log in user {}.", username, e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, ResourceBundleProvider.serverMsg("login_failed_server_error"),
                            ""));
            return null;
        }
    }

    public String logout() {
        String identifier = username;

        // invalidate the session
        LOG.debug("invalidating session for '{}'", identifier);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        LOG.info("logout successful for '{}'", identifier);
        return Constants.LOGOUT_REDIRECT;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String isLoggedInForwardHome() {
        if (isLoggedIn()) {
            return Constants.AFTER_LOGIN_REDIRECT;
        }
        return null;
    }

    private User find(String username, String password) {
        if (phzAuthenticationService.isEnabled()) {
            try {
                currentUser = phzAuthenticationService.login(username, password);
                phzAuthenticationService.getApiClient()
                        .addDefaultHeader("Authorization", "Bearer " + currentUser.getAccessToken());
                return currentUser;

            } catch (Exception e) {
                LOG.error("User {} failed to log into PHZ intra.", username, e);
                return null;
            }
        }

        User result = null;
        if ("admin".equalsIgnoreCase(username) && "admin".equals(password)) {
            result = new User(username, "John", "Doe");
        }
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // do not provide a setter for currentUser!
}