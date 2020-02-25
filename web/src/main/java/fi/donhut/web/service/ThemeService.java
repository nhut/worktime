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
package fi.donhut.web.service;

import fi.donhut.web.model.Theme;
import fi.donhut.web.model.Themes;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class ThemeService {

    private List<Theme> themes = new ArrayList<>();

    public ThemeService() {
        for (Themes theme : Themes.values()) {
            themes.add(new Theme(theme));
        }
    }

    public List<Theme> getThemes() {
        return themes;
    }
}
