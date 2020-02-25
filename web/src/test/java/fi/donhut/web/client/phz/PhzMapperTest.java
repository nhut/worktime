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

import fi.donhut.client.phz.generated.myown.AuthenticatePostResponse;
import fi.donhut.web.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link PhzMapper}.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class PhzMapperTest {

    @Test
    public void toUser() {
        final AuthenticatePostResponse phzResponse = new AuthenticatePostResponse();
        fi.donhut.client.phz.generated.myown.User phzUser = new fi.donhut.client.phz.generated.myown.User();
        phzUser.setLoginname("username");
        phzUser.setVorname("firstname");
        phzUser.setNachname("lastname");
        phzResponse.setUser(phzUser);

        final User user = PhzMapper.toUser(phzResponse);

        assertNotNull(user);
        assertEquals(phzResponse.getUser().getLoginname(), user.getUsername());
        assertEquals(phzResponse.getUser().getVorname(), user.getFirstName());
        assertEquals(phzResponse.getUser().getNachname(), user.getLastName());
        assertEquals(phzResponse.getAccessToken(), user.getAccessToken());
        assertEquals(phzResponse.getRefreshToken(), user.getRefreshToken());
        assertEquals(phzResponse.getRefreshUrl(), user.getRefreshUrl());
    }
}