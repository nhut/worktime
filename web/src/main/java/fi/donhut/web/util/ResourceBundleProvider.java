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
package fi.donhut.web.util;

import javax.faces.context.FacesContext;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class ResourceBundleProvider {

    public ResourceBundleProvider() {
    }

    private static ResourceBundle getBundle(final String resourceBundleVar) {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getResourceBundle(context, resourceBundleVar);
    }

    public static String msg(final String key) {
        String resourceBundleVariable = "msg";
        return getResourceBundleKey(key, resourceBundleVariable);
    }

    public static String serverMsg(final String key) {
        final String resourceBundleVariable = "serverMsg";
        return getResourceBundleKey(key, resourceBundleVariable);
    }

    private static String getResourceBundleKey(final String key, final String resourceBundleVariable) {
        String result;
        try {
            final ResourceBundle resourceBundle = getBundle(resourceBundleVariable);
            result = resourceBundle.getString(key);

        } catch (MissingResourceException e) {
            result = "???" + key + "???";
        }
        return result;
    }
}
