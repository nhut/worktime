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
import fi.donhut.client.phz.generated.myown.BonusGetResponse;
import fi.donhut.client.phz.generated.myown.ProjectsGetResponse;
import fi.donhut.client.phz.generated.myown.TimeProjectsGetPerMonthResponse;
import fi.donhut.web.model.Bonus;
import fi.donhut.web.model.Project;
import fi.donhut.web.model.User;
import fi.donhut.web.model.WorkHour;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps PHZ objects to domain class.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
final class PhzMapper {

    private PhzMapper() {
    }

    static User toUser(final AuthenticatePostResponse authenticateResponse) {
        final fi.donhut.client.phz.generated.myown.User phzUser = authenticateResponse.getUser();
        final User user = new User(phzUser.getLoginname(), phzUser.getVorname(), phzUser.getNachname());
        user.setAccessToken(authenticateResponse.getAccessToken());
        user.setRefreshToken(authenticateResponse.getRefreshToken());
        user.setRefreshUrl(authenticateResponse.getRefreshUrl());
        return user;
    }

    static List<Project> toProject(final ProjectsGetResponse[] projectsGetResponses) {
        final List<Project> projects = new ArrayList<>();
        for (final ProjectsGetResponse response : projectsGetResponses) {
            final Project project = new Project();
            project.setId(response.getID());
            project.setName(response.getName());
            project.setDescription(response.getNote());
            projects.add(project);
        }
        return projects;
    }

    static List<WorkHour> toWorkHour(final TimeProjectsGetPerMonthResponse[]
                                             timeProjectsGetPerMonthResponses) {
        final List<WorkHour> workHours = new ArrayList<>();
        for (final TimeProjectsGetPerMonthResponse response : timeProjectsGetPerMonthResponses) {
            final WorkHour workHour = new WorkHour();
            workHour.setId(response.getID());
            workHour.setProjectId(response.getProjekt());
            workHour.setHour(response.getH());
            workHour.setMinute(response.getM());
            workHour.setDateS(response.getDatum());
            workHour.setDescription(response.getNote());
            workHours.add(workHour);
        }
        return workHours;
    }

    static List<Bonus> toBonus(final BonusGetResponse[] bonusGetResponses) {
        final List<Bonus> bonuses = new ArrayList<>();
        for (BonusGetResponse response : bonusGetResponses) {
            final Bonus bonus = new Bonus(response.getMonth(), response.getCumulativeInvoiceabilityRate());
            bonuses.add(bonus);
        }
        return bonuses;
    }
}
