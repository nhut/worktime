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
package fi.donhut.web.model.dataimport;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JiraImportRow implements Serializable {

    private static final long serialVersionUID = -3534083203504142784L;

    private static final String DATE_FORMAT = "yyyy-M-d";

    @CsvBindByName(column = "Work date", required = true)
    private String workDate;

    @CsvBindByName(column = "Hours", required = true)
    private BigDecimal hours;

    @CsvBindByName(column = "Work description")
    private String workDescription;

    public JiraImportRow() {
    }

    public JiraImportRow(final String workDate, final BigDecimal hours, final String workDescription) {
        this.workDate = workDate;
        this.hours = hours;
        this.workDescription = workDescription;
    }

    public LocalDate getWorkDateInDate() {
        if (workDate == null) {
            return null;
        }
        final int dateFormatLength = 10;
        if (workDate.length() > dateFormatLength) {
            workDate = workDate.substring(0, dateFormatLength);
        }
        return LocalDate.parse(workDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(final String workDate) {
        this.workDate = workDate;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(final BigDecimal hours) {
        this.hours = hours;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(final String workDescription) {
        this.workDescription = workDescription;
    }
}
