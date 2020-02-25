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

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.donhut.client.phz.generated.myown.AuthenticatePostResponse;
import fi.donhut.web.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Extended PHZ intra {@link fi.donhut.client.phz.generated.api.AuthenticationApi}.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Service
@Primary
public class PhzAuthenticationApi extends fi.donhut.client.phz.generated.api.AuthenticationApi {

    private final ObjectMapper objectMapper;

    @Inject
    public PhzAuthenticationApi(final PhzIntraApiClient client, final ObjectMapper objectMapper) {
        super(client);
        this.objectMapper = objectMapper;
    }

    public User login(final String username, final String password) throws IOException {
        final String response = super.authLoginPost(username, password);
        final AuthenticatePostResponse authenticatePostResponse =
                objectMapper.readValue(response, AuthenticatePostResponse.class);
        return PhzMapper.toUser(authenticatePostResponse);
    }

    public boolean isEnabled() {
        return PhzIntraUtil.isEnabled(getApiClient());
    }
}
