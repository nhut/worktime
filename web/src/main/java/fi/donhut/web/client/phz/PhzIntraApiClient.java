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
package fi.donhut.web.client.phz;

import fi.donhut.client.phz.generated.handler.ApiClient;
import fi.donhut.web.app.config.PhzIntraConfig;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Wrapper class for default {@link ApiClient} with injected application specific configs.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Component
@Primary
public final class PhzIntraApiClient extends ApiClient {

    private PhzIntraConfig config;

    @Inject
    public PhzIntraApiClient(final PhzIntraConfig config) {
        super();
        this.config = config;
        this.setBasePath(config.getUrl());
    }

    public PhzIntraConfig getConfig() {
        return config;
    }
}
