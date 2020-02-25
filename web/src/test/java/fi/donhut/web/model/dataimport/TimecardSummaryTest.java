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

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimecardSummaryTest {

    private TimecardSummary sut;

    private List<JiraImportRow> rows = new ArrayList<>();

    @Before
    public void setUp() {
        sut = new TimecardSummary();

        rows.add(new JiraImportRow("2018-10-3", BigDecimal.valueOf(2), "Working1"));
        rows.add(new JiraImportRow("2018-10-3", BigDecimal.valueOf(7.75), "Working1"));
        sut.setRows(rows);
    }

    @Test
    public void getTotalHourAndMinutes() {
        assertEquals(BigDecimal.valueOf(9.75), sut.getTotalHourAndMinutes());
    }

    @Test
    public void getTotalHour() {
        assertEquals(9, sut.getTotalHour());
    }

    @Test
    public void getTotalMinute() {
        assertEquals(45, sut.getTotalMinute());
    }

    @Test
    public void getStartTime() {
        assertEquals(LocalTime.of(10, 0), sut.getStartTime());
    }

    @Test
    public void getStartTime_overHourSpecialHandlingRequired() {
        rows.add(new JiraImportRow("2018-10-3", BigDecimal.valueOf(5), "Working2"));

        LocalTime startTime = sut.getStartTime();

        assertEquals(LocalTime.of(10-(14-13), 0), startTime);
    }

    @Test
    public void getEndTime() {
        assertEquals(LocalTime.of(19, 45), sut.getEndTime());
    }

    @Test
    public void getEndTime_overHourSpecialHandlingRequired() {
        rows.add(new JiraImportRow("2018-10-3", BigDecimal.valueOf(5), "Working2"));

        assertEquals(LocalTime.of(23, 45), sut.getEndTime());
    }

}