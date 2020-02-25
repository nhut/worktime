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
/*import com.fasterxml.jackson.databind.ObjectMapper;
import fi.donhut.client.phz.generated.api.AuthenticationApi;
import fi.donhut.client.phz.generated.myown.AuthenticateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@RestClientTest
public class PhzApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    private final AuthenticationApi api = new AuthenticationApi();

    public PhzApiTest() {
        api.getApiClient();
                //.setBasePath("https://intra-api.phz.fi");
    }

    @Test
    public void authLoginPostTest() {
        String username = "nhudo";
        String password = "";
        //String response = api.authLoginPost(username, password);

        String json = "{\"user\":{\"ID\":185,\"vorname\":\"Nhut\",\"nachname\":\"Do\",\"kurz\":\"ND\",\"firma\":\"\",\"gruppe\":36,\"email\":\"nhut.do@phz.fi\",\"acc\":\"cy\",\"tel1\":\"\",\"tel2\":\"\",\"fax\":\"\",\"strasse\":\"\",\"stadt\":\"\",\"plz\":\"\",\"land\":\"\",\"sprache\":\"fi\",\"mobil\":\"0405567461\",\"loginname\":\"nhut\",\"ldap_name\":\"\",\"anrede\":\"\",\"sms\":\"\",\"role\":1,\"proxy\":null,\"settings\":\"a:25:{s:6:\\\"langua\\\";s:2:\\\"fi\\\";s:4:\\\"skin\\\";s:7:\\\"default\\\";s:6:\\\"screen\\\";s:0:\\\"\\\";s:11:\\\"startmodule\\\";s:8:\\\"timecard\\\";s:8:\\\"timezone\\\";s:1:\\\"0\\\";s:15:\\\"start_tree_mode\\\";s:0:\\\"\\\";s:13:\\\"start_perpage\\\";s:0:\\\"\\\";s:11:\\\"tagesanfang\\\";s:1:\\\"6\\\";s:9:\\\"tagesende\\\";s:2:\\\"22\\\";s:13:\\\"cal_leftframe\\\";s:3:\\\"210\\\";s:14:\\\"timestep_daily\\\";s:2:\\\"15\\\";s:15:\\\"timestep_weekly\\\";s:2:\\\"15\\\";s:3:\\\"ppc\\\";s:1:\\\"6\\\";s:3:\\\"cut\\\";s:1:\\\"1\\\";s:8:\\\"cal_mode\\\";s:1:\\\"1\\\";s:8:\\\"cal_view\\\";s:1:\\\"0\\\";s:8:\\\"reminder\\\";s:1:\\\"2\\\";s:11:\\\"remind_freq\\\";s:2:\\\"15\\\";s:13:\\\"reminder_mail\\\";s:1:\\\"0\\\";s:11:\\\"cont_action\\\";s:7:\\\"members\\\";s:15:\\\"chat_entry_type\\\";s:0:\\\"\\\";s:14:\\\"chat_direction\\\";s:0:\\\"\\\";s:15:\\\"forum_view_both\\\";s:1:\\\"0\\\";s:18:\\\"file_download_type\\\";s:0:\\\"\\\";s:15:\\\"notes_view_both\\\";s:1:\\\"0\\\";}\",\"hrate\":\"\",\"usertype\":null,\"status\":null},\"access_token\":\"xQBJbaoPwHdEgmLS2WgIKiMH8C6AUxOSvEDFbgsz3WLqUQFePJ5qj9AzOb5Bu7oD\",\"access_token_expires_at\":{\"date\":\"2018-10-15 03:12:20.112665\",\"timezone_type\":3,\"timezone\":\"UTC\"},\"refresh_token\":\"4aQWCjL0D4ls9m8VoNYZSY7CH7lzy85mHJ9ZNfLhwJdwM26onkKMDE56vxBkmkPU\",\"refresh_url\":\"https://intra-api.phz.fi/auth/refresh\"}";
        AuthenticateResponse authenticateResponse = objectMapper.convertValue(json, AuthenticateResponse.class);
System.out.println(authenticateResponse);
        // TODO: test validations
    }
}
*/