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

import fi.donhut.web.util.ResourceBundleProvider;
import fi.donhut.web.util.TimeUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimecardSummary implements Serializable {

    private static final long serialVersionUID = -2300087474242450538L;

    private String id = "";
    private LocalDate date;
    private List<JiraImportRow> rows = new ArrayList<>();
    private boolean includeLunchtime;

    /**
     * For testing purpose.
     */
    public TimecardSummary() {
    }

    public TimecardSummary(
            final LocalDate date, final List<JiraImportRow> rows, final boolean includeLunchtime) {
        this.date = date;
        this.rows = rows;
        this.includeLunchtime = includeLunchtime;
        this.id = createId();
    }

    public String createId() {
        return date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth();
    }

    public String getId() {
        return id;
    }

    public String getRowsToolTip() {
        final StringBuilder sb = new StringBuilder();
        for (JiraImportRow row : rows) {
            final BigDecimal totalHourAndMinutes = row.getHours();
            final int totalHour = TimeUtil.getTotalHour(totalHourAndMinutes);
            final int totalMinute = TimeUtil.getTotalMinute(totalHourAndMinutes, totalHour);
            final String workDescription = row.getWorkDescription();

            sb.append(createToolTipRow(totalHour, totalMinute, workDescription));
        }
        if (includeLunchtime) {
            final int minutesForLunch = 30;
            sb.append(createToolTipRow(0, minutesForLunch, ResourceBundleProvider.msg("note_lunch")));
        }
        return sb.toString();
    }

    private StringBuilder createToolTipRow(
            final int totalHour, final int totalMinute, final String workDescription) {
        return new StringBuilder().append("* ").append(totalHour)
                .append(ResourceBundleProvider.msg("unit_hour")).append(" ")
                .append(totalMinute)
                .append(ResourceBundleProvider.msg("unit_minute")).append("  ")
                .append(workDescription).append("<br/>");
    }

    BigDecimal getTotalHourAndMinutes() {
        BigDecimal totalTime = BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR);
        for (JiraImportRow row : rows) {
            totalTime = totalTime.add(row.getHours());
        }
        if (includeLunchtime) {
            final BigDecimal lunchTimeInHour = BigDecimal.valueOf(0.5);
            totalTime = totalTime.add(lunchTimeInHour);
        }
        return totalTime;
    }

    public int getTotalHour() {
        return TimeUtil.getTotalHour(getTotalHourAndMinutes());
    }

    public int getTotalMinute() {
        final BigDecimal totalHourAndMinutes = getTotalHourAndMinutes();
        final int totalHour = getTotalHour();

        return TimeUtil.getTotalMinute(totalHourAndMinutes, totalHour);
    }

    public LocalTime getStartTime() {
        final int totalHour = getTotalHour();

        return TimeUtil.getStartTime(totalHour);
    }

    public LocalTime getEndTime() {
        final LocalTime endTime = getStartTime();
        final int totalHour = getTotalHour();
        final int totalMinute = getTotalMinute();

        return TimeUtil.getEndTime(endTime, totalHour, totalMinute);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public List<JiraImportRow> getRows() {
        return rows;
    }

    public void setRows(final List<JiraImportRow> rows) {
        this.rows = rows;
    }

    public boolean isIncludeLunchtime() {
        return includeLunchtime;
    }

    public void setIncludeLunchtime(final boolean includeLunchtime) {
        this.includeLunchtime = includeLunchtime;
    }
}
