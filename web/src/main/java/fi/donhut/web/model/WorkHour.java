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
package fi.donhut.web.model;

import java.io.Serializable;

/**
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class WorkHour implements Serializable {

    private static final long serialVersionUID = -2020018562308830342L;

    private Integer id;
    private Integer projectId;
    private Integer hour;
    private Integer minute;
    private String dateS;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(final Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(final Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(final Integer minute) {
        this.minute = minute;
    }

    public String getDateS() {
        return dateS;
    }

    public void setDateS(final String dateS) {
        this.dateS = dateS;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
