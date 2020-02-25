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
package fi.donhut.web.service;

import fi.donhut.client.phz.generated.model.TimeProject;
import fi.donhut.client.phz.generated.model.TimeProjectArray;
import fi.donhut.client.phz.generated.model.Timecard;
import fi.donhut.client.phz.generated.model.TimecardArray;
import fi.donhut.web.client.phz.PhzIntraUtil;
import fi.donhut.web.client.phz.PhzTimeProjectsApi;
import fi.donhut.web.client.phz.PhzTimecardsApi;
import fi.donhut.web.model.dataimport.JiraImportRow;
import fi.donhut.web.model.dataimport.TimecardSummary;
import fi.donhut.web.util.FileUtil;
import fi.donhut.web.util.TimeUtil;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class JiraImportService {

    private static final Logger LOG = LoggerFactory.getLogger(JiraImportService.class);
    private static final int PROJECT_ID_CONSULTING = 236;
    private static final int PROJECT_ID_LUNCH = 228;

    private static final int DEFAULT_LUNCH_TIME_IN_MINUTES = 30;

    @Inject
    private PhzTimecardsApi timeCardsApi;
    @Inject
    private PhzTimeProjectsApi timeProjectsApi;

    public List<JiraImportRow> readUploadFile(final UploadedFile file, final char csvSeparator)
            throws IOException {
        return FileUtil.readCsvStreamData(JiraImportRow.class, file.getFileName(), csvSeparator,
                file.getInputstream());
    }

    public List<TimecardSummary> createTimecardsCreateSummary(
            final List<JiraImportRow> jiraImportRows, final boolean addLunchTime) {
        final Map<LocalDate, List<JiraImportRow>> dateOfRows = new TreeMap<>(
                jiraImportRows.stream()
                        .collect(Collectors.groupingBy(JiraImportRow::getWorkDateInDate)));

        final List<TimecardSummary> timecardSummaries = new ArrayList<>();
        dateOfRows.keySet().forEach(eventDate -> {
            LOG.debug("Processing date '{}' rows...", eventDate);
            final List<JiraImportRow> rows = dateOfRows.get(eventDate);
            TimecardSummary timecardSummary = new TimecardSummary(eventDate, rows, addLunchTime);
            timecardSummaries.add(timecardSummary);
        });
        return timecardSummaries;
    }

    public void createTimecardsToPhzIntra(final List<TimecardSummary> timecardSummaries) {
        if (!timeCardsApi.isEnabled()) {
            return;
        }
        final TimecardArray timecardArray = new TimecardArray();
        for (final TimecardSummary timecardSummary : timecardSummaries) {
            timecardArray.addTimecardsItem(createTimecardForDay(timecardSummary));
        }
        timeCardsApi.timecardsPost(timecardArray);
    }

    private static Timecard createTimecardForDay(final TimecardSummary timecardSummary) {
        final Timecard timecard = new Timecard();
        timecard.setDatum(PhzIntraUtil.PHZ_DATE_FORMAT.format(timecardSummary.getDate()));
        timecard.setAnfang(timecardSummary.getStartTime().format(PhzIntraUtil.PHZ_TIME_FORMAT));
        timecard.setEnde(timecardSummary.getEndTime().format(PhzIntraUtil.PHZ_TIME_FORMAT));
        return timecard;
    }

    public void createWorkHoursToPhz(final List<TimecardSummary> timecardsCreateSummary) {
        final TimeProjectArray timeProjectArray = new TimeProjectArray();
        for (final TimecardSummary timecard : timecardsCreateSummary) {
            final String dateInString = PhzIntraUtil.PHZ_DATE_FORMAT.format(timecard.getDate());
            for (final JiraImportRow row : timecard.getRows()) {
                final TimeProject timeProject = createConsultantWorkRow(dateInString, row);
                timeProjectArray.addTimeprojectsItem(timeProject);
            }
            if (timecard.isIncludeLunchtime()) {
                timeProjectArray.addTimeprojectsItem(createLunchTimeRow(dateInString));
            }
        }
        if (timeProjectsApi.isEnabled()) {
            timeProjectsApi.timeprojectsPost(timeProjectArray);
        }
    }

    private static TimeProject createConsultantWorkRow(final String dateInString, final JiraImportRow row) {
        final TimeProject timeProject = new TimeProject();
        timeProject.datum(dateInString);
        timeProject.setProjekt(PROJECT_ID_CONSULTING);
        final BigDecimal hourAndMinutes = row.getHours();
        final int hours = TimeUtil.getTotalHour(hourAndMinutes);
        timeProject.setH(hours);
        timeProject.setM(TimeUtil.getTotalMinute(hourAndMinutes, hours));
        timeProject.setNote(row.getWorkDescription() == null ? "-" : row.getWorkDescription());
        return timeProject;
    }

    private static TimeProject createLunchTimeRow(final String dateInString) {
        final TimeProject timeProject = new TimeProject();
        timeProject.setProjekt(PROJECT_ID_LUNCH);
        timeProject.datum(dateInString);
        timeProject.setH(0);
        timeProject.setM(DEFAULT_LUNCH_TIME_IN_MINUTES);
        timeProject.setNote("-");
        return timeProject;
    }
}
