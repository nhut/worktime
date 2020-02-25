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
import fi.donhut.client.phz.generated.myown.ProjectsGetResponse;
import fi.donhut.web.model.Project;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Extended PHZ intra {@link fi.donhut.client.phz.generated.api.ProjectsApi}.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Service
@Primary
public class PhzProjectApi extends fi.donhut.client.phz.generated.api.ProjectsApi {

    private final ObjectMapper objectMapper;

    @Inject
    public PhzProjectApi(final PhzIntraApiClient client, final ObjectMapper objectMapper) {
        super(client);
        this.objectMapper = objectMapper;
    }

    public boolean isEnabled() {
        return PhzIntraUtil.isEnabled(getApiClient());
    }

    public List<Project> projects() throws RestClientException, IOException {
        final String response = super.projectsGet();
        final ProjectsGetResponse[] projectsGetResponses =
                objectMapper.readValue(response, ProjectsGetResponse[].class);

        return PhzMapper.toProject(projectsGetResponses);
    }
}
