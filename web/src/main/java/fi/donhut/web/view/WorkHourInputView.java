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
package fi.donhut.web.view;

import fi.donhut.web.Constants;
import fi.donhut.web.client.phz.PhzIntraUtil;
import fi.donhut.web.client.phz.PhzProjectApi;
import fi.donhut.web.client.phz.PhzTimeProjectsApi;
import fi.donhut.web.model.Project;
import fi.donhut.web.model.WorkHour;
import fi.donhut.web.util.ResourceBundleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Manages work hour input view.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@ViewScoped
public class WorkHourInputView implements Serializable {

    private static final long serialVersionUID = 7961555446170187445L;
    private static final Logger LOG = LoggerFactory.getLogger(WorkHourInputView.class);
    private static final DateTimeFormatter DATE_yyyyMM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter DATE_yyyyMMdd_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINUTES_IN_HOUR = 60;

    @Inject
    private PhzProjectApi phzProjectService;
    @Inject
    private PhzTimeProjectsApi phzTimeProjectsService;

    private LocalDate currentDate = LocalDate.now().withDayOfMonth(1);
    private String selectedYearAndMonth = DATE_yyyyMM_FORMATTER.format(currentDate);

    private List<Project> projects;
    private String errorFetchProjects;

    private Set<String> yearAndMonthItems = new TreeSet<>(Comparator.reverseOrder());
    private List<WorkHour> workHourInputs = new ArrayList<>();
    private String errorFetchWorkInputs = null;

    public WorkHourInputView() {
        LOG.trace("{} constructor initialized.", this.getClass().getSimpleName());
    }

    @PostConstruct
    public void init() {
        getProjectsData();
        getWorkHourInputs(currentDate);
        yearAndMonthItems.add(selectedYearAndMonth);
        final int monthsInAYear = 12;
        for (int i = 1; i < monthsInAYear; i++) {
            currentDate = currentDate.minusMonths(1);
            yearAndMonthItems.add(DATE_yyyyMM_FORMATTER.format(currentDate));
        }
    }

    private void getProjectsData() {
        if (phzProjectService.isEnabled()) {
            try {
                projects = phzProjectService.projects();
                errorFetchProjects = null;
            } catch (Exception e) {
                LOG.error("Failed to fetch projectsGet.", e);
                errorFetchProjects = ResourceBundleProvider.serverMsg("fail_fetch_projects_info");
            }
        }
    }

    private void getWorkHourInputs(final LocalDate currDate) {
        workHourInputs.clear();
        if (phzTimeProjectsService.isEnabled()) {
            try {
                workHourInputs.addAll(phzTimeProjectsService.getWorkHourInputs(
                        String.valueOf(currDate.getYear()), String.valueOf(currDate.getMonthValue())));
                if (!workHourInputs.isEmpty()) {
                    workHourInputs.sort(Comparator.comparing(WorkHour::getDateS)
                            .thenComparing(WorkHour::getId));
                }
                errorFetchWorkInputs = null;

            } catch (Exception e) {
                LOG.error("Failed to fetch getWorkHourInputs.", e);
                errorFetchWorkInputs = ResourceBundleProvider.serverMsg("fetch_workhours_fail");
            }
        }
    }

    public String getSelectedYearAndMonth() {
        return selectedYearAndMonth;
    }

    public void setSelectedYearAndMonth(final String selYearAndMonth) {
        this.selectedYearAndMonth = selYearAndMonth;
    }

    public Set<String> getYearAndMonthItems() {
        return yearAndMonthItems;
    }

    public List<WorkHour> getWorkHourInputs() {
        return workHourInputs;
    }

    public String getGetTotalWorkHourInputs() {
        return getGetTotalWorkHourInputs(this.workHourInputs);
    }

    public String getGetTotalWorkHourInputsPerDay(final WorkHour workHour) {
        final List<WorkHour> dayWorkHours = this.workHourInputs.stream()
                .filter(input -> workHour.getDateS().equals(input.getDateS()))
                .collect(Collectors.toList());
        return getGetTotalWorkHourInputs(dayWorkHours);
    }

    private String getGetTotalWorkHourInputs(final List<WorkHour> workHourInputs) {
        if (workHourInputs.isEmpty()) {
            return "";
        }
        try {
            int hoursWithLunch = 0;
            int minutesWithLunch = 0;
            for (WorkHour input : workHourInputs) {
                hoursWithLunch += input.getHour();
                minutesWithLunch += input.getMinute();
            }
            int hoursInMinuteWithLunch = minutesWithLunch / MINUTES_IN_HOUR;
            hoursWithLunch += hoursInMinuteWithLunch;
            minutesWithLunch = minutesWithLunch % MINUTES_IN_HOUR;

            final String totalHourAndMinutesWithLunchTimeText =
                    PhzIntraUtil.getFormatTimeDisplay(hoursWithLunch, minutesWithLunch);

            int minutesWithoutLunch = minutesWithLunch + (MINUTES_IN_HOUR * hoursWithLunch);
            for (WorkHour input : workHourInputs) {
                if (Constants.PROJECT_ID_LUNCH == input.getProjectId()) {
                    minutesWithoutLunch -= (input.getMinute() + input.getHour() * MINUTES_IN_HOUR);
                }
            }
            int hoursInMinuteWithoutLunch = minutesWithoutLunch / MINUTES_IN_HOUR;
            minutesWithoutLunch = minutesWithoutLunch % MINUTES_IN_HOUR;
            final String totalHourAndMinutesWithoutLunchTimeText =
                    PhzIntraUtil.getFormatTimeDisplay(hoursInMinuteWithoutLunch, minutesWithoutLunch);

            final String totalHourAndMinutesConsultingText = getConsultingHouAndMinutesText(workHourInputs);

            return String.format("%s (%s), %s (%s), %s (%s)",
                    totalHourAndMinutesWithLunchTimeText,
                    ResourceBundleProvider.msg("total").toLowerCase(),

                    totalHourAndMinutesWithoutLunchTimeText,
                    ResourceBundleProvider.msg("without_lunchtime").toLowerCase(),

                    totalHourAndMinutesConsultingText,
                    ResourceBundleProvider.msg("consult_work").toLowerCase());

        } catch (Exception e) {
            LOG.error("getGetTotalWorkHourInputs", e);
            return null;
        }
    }

    private static String getConsultingHouAndMinutesText(final List<WorkHour> workHourInputs) {
        if (workHourInputs == null) {
            return "";
        }
        int minutes = 0;
        for (WorkHour input : workHourInputs) {
            if (input.getProjectId().equals(Constants.PROJECT_ID_CONSULTING)) {
                minutes += input.getHour() * MINUTES_IN_HOUR;
                minutes += input.getMinute();
            }
        }
        int hours = minutes / MINUTES_IN_HOUR;
        minutes = minutes % MINUTES_IN_HOUR;
        return PhzIntraUtil.getFormatTimeDisplay(hours, minutes);
    }

    public void selectedYearAndMonthSelectionChanged(final AjaxBehaviorEvent event)  {
        final String selected = getSelectedYearAndMonth();
        LOG.trace("Selection value changed to {}.", selected);
        currentDate = LocalDate.parse(selected + "-01", DATE_yyyyMMdd_FORMATTER);
        getWorkHourInputs(currentDate);
    }

    public String getProjectText(final Integer projectId) {
        if (projectId == null) {
            return "";
        }
        if (projects != null) {
            final String projectName = PhzIntraUtil.resolveProjectId(projects, projectId);
            if (projectName != null) {
                return projectName;
            }
        }
        switch (projectId) {
            case Constants.PROJECT_ID_CONSULTING:
            case Constants.PROJECT_ID_LUNCH:
                return ResourceBundleProvider.msg("project_id_" + projectId);

            default:
                return String.valueOf(projectId);
        }
    }

    public String getDateToDisplay(final String dateS) {
        try {
            LocalDate date = LocalDate.parse(dateS, PhzIntraUtil.PHZ_DATE_FORMAT);
            return PhzIntraUtil.getFormatDateDisplay(date);
        } catch (Exception e) {
            return dateS;
        }
    }

    public String getProjectTooltip(final Integer projectId) {
        if (projects != null) {
            for (Project project : projects) {
                final Integer id = project.getId();
                if (id != null && id.equals(projectId)) {
                    return project.getDescription();
                }
            }
        }
        return "";
    }

    public String getErrorFetchProjects() {
        return errorFetchProjects;
    }

    public String getErrorFetchWorkInputs() {
        return errorFetchWorkInputs;
    }
}
