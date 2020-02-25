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

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;

/**
 * Manages user locale.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@SessionScoped
public class LocaleManager implements Serializable {

    private static final long serialVersionUID = -9220680122099750856L;

    private Locale locale;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init() {
        locale = facesContext.getExternalContext().getRequestLocale();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(final String language) {
        locale = language.equalsIgnoreCase("en") ? Locale.ENGLISH : new Locale(language);
        facesContext.getViewRoot().setLocale(locale);
    }

    public void languageChanged(final ValueChangeEvent e) {
        final String newLocaleValue = e.getNewValue().toString();
        setLanguage(newLocaleValue);
    }
}
