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
import fi.donhut.client.phz.generated.handler.ApiClient;
import fi.donhut.client.phz.generated.myown.TimecardsGetResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Extended PHZ intra {@link fi.donhut.client.phz.generated.api.TimecardsApi}.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Service
@Primary
public class PhzTimecardsApi extends fi.donhut.client.phz.generated.api.TimecardsApi {

    private final ObjectMapper objectMapper;

    @Inject
    public PhzTimecardsApi(final PhzIntraApiClient client, final ObjectMapper objectMapper) {
        super(client);
        this.objectMapper = objectMapper;
    }

    public List<TimecardsGetResponse> timecardsGet() throws RestClientException, IOException {
        Object postBody = null;

        String path = UriComponentsBuilder.fromPath("/timecards").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final ApiClient apiClient = super.getApiClient();
        final String[] accepts = {};
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {};
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {"intra_auth"};

        final ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>(){};
        final String response = apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody,
                headerParams, formParams, accept, contentType, authNames, returnType);
        return Arrays.asList(this.objectMapper.readValue(response, TimecardsGetResponse[].class));
    }

    public boolean isEnabled() {
        final boolean enabled = PhzIntraUtil.isEnabled(getApiClient());
        return enabled;
    }
}
