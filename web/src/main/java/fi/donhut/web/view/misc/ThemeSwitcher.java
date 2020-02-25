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

import fi.donhut.web.model.Theme;
import fi.donhut.web.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Manages user theme.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@SessionScoped
public class ThemeSwitcher implements Serializable {

    private static final long serialVersionUID = -5726820689587082293L;

    private static final Logger LOG = LoggerFactory.getLogger(ThemeSwitcher.class);

    private List<Theme> themes;

    @Inject
    private ThemeService service;

    private String selectedTheme;

    @PostConstruct
    public void init() {
        themes = service.getThemes();
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void handleChange(final ValueChangeEvent event){
        final String newValue = (String) event.getNewValue();
        LOG.info("Switching theme to: {}", newValue);
        if (newValue == null) {
            selectedTheme = null;
            return;
        }
        for (Theme theme : themes) {
            final String themeName = theme.getName();
            if (newValue.equals(themeName)) {
                selectedTheme = themeName;
                return;
            }
        }
        selectedTheme = null;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(final String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }
}
