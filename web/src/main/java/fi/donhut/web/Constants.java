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
package fi.donhut.web;

/**
 * Constants.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public interface Constants {

    String SERVLET_LOGOUT = "/logout";

    String VIEW_LOGIN = "/login.xhtml";
    String VIEW_LOGOUT = "/logout.xhtml";

    String VIEW_WORKHOUR_INPUT = "/secured/workHourInputTab.xhtml";

    String AFTER_LOGIN_REDIRECT = VIEW_WORKHOUR_INPUT + "?faces-redirect=true";
    String LOGOUT_REDIRECT = VIEW_LOGOUT + "?faces-redirect=true";

    int PROJECT_ID_CONSULTING = 236;
    int PROJECT_ID_LUNCH = 228;

    String CONTEXT_ATTRIBUTE_USER_MANAGER = "userManager";
}
