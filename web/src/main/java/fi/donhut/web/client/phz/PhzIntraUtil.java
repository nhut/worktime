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

import fi.donhut.client.phz.generated.handler.ApiClient;
import fi.donhut.client.phz.generated.myown.TimecardsGetResponse;
import fi.donhut.web.model.Project;
import fi.donhut.web.util.ResourceBundleProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * PHZ intra specific utility.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public final class PhzIntraUtil {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter PHZ_YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter PHZ_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter PHZ_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    private static final Logger LOG = LoggerFactory.getLogger(PhzIntraUtil.class);

    public static boolean isEnabled(final ApiClient apiClient) {
        if (apiClient instanceof PhzIntraApiClient) {
            final PhzIntraApiClient phzApiClient = (PhzIntraApiClient) apiClient;
            return phzApiClient.getConfig().isEnabled();
        }
        return true;
    }

    public static String formatDate(final String dateInYYYYMMDD) throws ParseException {
        if (StringUtils.isNotBlank(dateInYYYYMMDD)) {
            final Date date = PhzIntraUtil.getParseDate(dateInYYYYMMDD);
            return PhzIntraUtil.getFormatDateDisplay(date);
        }
        return "";
    }

    public static String formatTime(final Integer startTime, final Integer endTime) {
        try {
            int hours = 0;
            int minutes = 0;
            if (endTime != null) {
                final LocalTime newLocalTime = getTimeDifference(startTime, endTime);
                hours = newLocalTime.getHour();
                minutes = newLocalTime.getMinute();
            }

            return getFormatTimeDisplay(hours, minutes);

        } catch (Exception e) {
            final String errorText =
                    String.format("(Error: start time '%d' - end time %d)", startTime, endTime);
            LOG.error(errorText, e);
            return errorText;
        }

    }

    public static LocalTime getTimeDifference(final Integer startTime, final Integer endTime) {
        final LocalTime startLocalTime =
                LocalTime.parse(startTime.toString(), DateTimeFormatter.ofPattern("Hmm"));
        LocalTime endLocalTime = LocalTime.parse(endTime.toString(), DateTimeFormatter.ofPattern("Hmm"));
        if (endLocalTime.isBefore(startLocalTime)) {
            final int hoursInADay = 24;
            endLocalTime = endLocalTime.plusHours(hoursInADay);
        }
        return endLocalTime.minusHours(startLocalTime.getHour())
                .minusMinutes(startLocalTime.getMinute());
    }

    public static String getFormatTimeDisplay(final LocalTime localTime) {
        return String.format("%d%s %d%s",
                localTime.getHour(), ResourceBundleProvider.msg("unit_hour"),
                localTime.getMinute(), ResourceBundleProvider.msg("unit_minute"));
    }

    public static String getFormatTimeDisplay(final int hours, final int minutes) {
        return String.format("%d%s %d%s",
                hours, ResourceBundleProvider.msg("unit_hour"),
                minutes, ResourceBundleProvider.msg("unit_minute"));
    }

    public static String getFormatDateDisplay(final LocalDate date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static String getFormatDateDisplay(final Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static Date getParseDate(final String dateInYYYYMMDD) throws ParseException {
        return DATE_FORMAT.parse(dateInYYYYMMDD);
    }

    public static String toDateString(final LocalDate date) {
        return PHZ_DATE_FORMAT.format(date);
    }

    public static Date getMinDate(final List<TimecardsGetResponse> timecards) {
        try {
            Date minDate = null;
            for (TimecardsGetResponse timecard : timecards) {
                Date date = getParseDate(timecard.getDatum());
                if (minDate == null) {
                    minDate = date;
                    continue;
                }
                if (date.before(minDate)) {
                    minDate = date;
                }
            }
            return minDate;

        } catch (Exception e) {
            return null;
        }
    }

    public static String getMinDateDisplay(final List<TimecardsGetResponse> timecards) {
        try {
            final Date date = getMinDate(timecards);
            return getFormatDateDisplay(date);

        } catch (Exception e) {
            return "error";
        }
    }

    public static Date getLatestDate(final List<TimecardsGetResponse> timecards) {
        try {
            Date returnDate = null;
            for (TimecardsGetResponse timecard : timecards) {
                Date date = getParseDate(timecard.getDatum());
                if (returnDate == null) {
                    returnDate = date;
                    continue;
                }
                if (date.after(returnDate)) {
                    returnDate = date;
                }
            }
            return returnDate;

        } catch (Exception e) {
            return null;
        }
    }

    public static String getMaxDateDisplay(final List<TimecardsGetResponse> timecards) {
        try {
            final Date date = getLatestDate(timecards);
            return getFormatDateDisplay(date);

        } catch (Exception e) {
            return "error";
        }
    }

    public static String resolveProjectId(final List<Project> projects, final Integer projectId) {
        for (Project project : projects) {
            final Integer id = project.getId();
            if (id != null && id.equals(projectId)) {
                return project.getName();
            }
        }
        return null;
    }
}
