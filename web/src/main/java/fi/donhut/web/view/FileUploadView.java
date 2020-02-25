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

import fi.donhut.web.model.dataimport.JiraImportRow;
import fi.donhut.web.model.dataimport.TimecardSummary;
import fi.donhut.web.service.JiraImportService;
import fi.donhut.web.util.ResourceBundleProvider;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages file upload view.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@ViewScoped
public class FileUploadView implements Serializable {

    private static final long serialVersionUID = -7844057030078761262L;
    private static final Logger LOG = LoggerFactory.getLogger(FileUploadView.class.getName());

    public enum STATUS {
        INITIAL,
        UPLOAD_SUCCESS,
        UPLOAD_FAIL,
        TIMECARD_CREATE_SUCCESS,
        TIMECARD_CREATE_FAIL,
        WORKHOUR_CREATE_SUCCESS,
        WORKHOUR_CREATE_FAIL;

        public boolean isInitial() {
            return this == INITIAL;
        }
        public boolean isUploadSuccess() {
            return this == UPLOAD_SUCCESS;
        }
        public boolean isUploadFail() {
            return this == UPLOAD_FAIL;
        }
        public boolean isTimecardCreateSuccess() {
            return this == TIMECARD_CREATE_SUCCESS;
        }
        public boolean isTimecardCreateFail() {
            return this == TIMECARD_CREATE_FAIL;
        }
        public boolean isWorkHourCreateSuccess() {
            return this == WORKHOUR_CREATE_SUCCESS;
        }
        public boolean isWorkHourCreateFail() {
            return this == WORKHOUR_CREATE_FAIL;
        }
    }

    @Inject
    private FacesContext facesContext;
    @Inject
    private JiraImportService jiraImportService;

    private STATUS status = STATUS.INITIAL;
    private boolean addLunchTime = true;
    private List<SelectItem> csvSeparatorList = Arrays.asList(
            new SelectItem(",", ","), new SelectItem(";", ";"),
            new SelectItem(":", ":"), new SelectItem("\t", "TAB")
    );
    private String csvSeparator = ","; // default is comma.

    private String errorFileUploadErrorMsg;
    private List<TimecardSummary> timecardsCreateSummary = new ArrayList<>();
    private String errorTimecardCreateMsg;
    private String errorWorkHourCreateMsg;

    private TimeCardCreateDialog timeCardCreateDialog;
    private WorkHourCreateDialog workHourCreateDialog;

    public FileUploadView() {
        LOG.trace("{} constructor initialized.", this.getClass().getSimpleName());
    }

    private void init() {
        status = STATUS.INITIAL;
        errorFileUploadErrorMsg = null;
        errorTimecardCreateMsg = null;

        timecardsCreateSummary.clear();
        timeCardCreateDialog = null;
        workHourCreateDialog = null;
    }

    public void handleFileUpload(final FileUploadEvent event) {
        final UploadedFile file = event.getFile();
        final String fileName = file.getFileName();
        try {
            init();
            final char separatorChar = csvSeparator.equals("TAB") ? '\t' : csvSeparator.charAt(0);
            final List<JiraImportRow> importRows = jiraImportService.readUploadFile(file, separatorChar);

            timecardsCreateSummary = jiraImportService.createTimecardsCreateSummary(importRows, addLunchTime);
            timeCardCreateDialog = new TimeCardCreateDialog(timecardsCreateSummary);
            status = STATUS.UPLOAD_SUCCESS;

            final String fileUploadSuccessText = ResourceBundleProvider.serverMsg("file_upload_success");
            final FacesMessage message = new FacesMessage("",
                    MessageFormat.format(fileUploadSuccessText, fileName, importRows.size()));
            facesContext.addMessage(null, message);

        } catch (Exception e) {
            status = STATUS.UPLOAD_FAIL;
            LOG.error("Failed to handle uploaded file '{}'.", fileName, e);

            final String errorDetailText = (e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage());
            final String errorText = MessageFormat.format(ResourceBundleProvider.serverMsg("file_upload_failed"), fileName, errorDetailText);
            errorFileUploadErrorMsg = errorText;

            final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", errorText);
            facesContext.addMessage(null, message);
        }
    }

    public void addLunchTimeChanged(final AjaxBehaviorEvent event)  {
        LOG.trace("Add lunchtime changed to {}.", isAddLunchTime());
    }

    public void csvSeparatorSelectionChanged(final AjaxBehaviorEvent event)  {
        LOG.trace("Csv separator changed to {}.", getCsvSeparator());
    }

    public STATUS getStatus() {
        return status;
    }

    public boolean isAddLunchTime() {
        return addLunchTime;
    }

    public void setAddLunchTime(boolean addLunchTime) {
        this.addLunchTime = addLunchTime;
    }

    public List<SelectItem> getCsvSeparatorList() {
        return csvSeparatorList;
    }

    public void setCsvSeparatorList(List<SelectItem> csvSeparatorList) {
        this.csvSeparatorList = csvSeparatorList;
    }

    public String getCsvSeparator() {
        return csvSeparator;
    }

    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    public String getErrorFileUploadErrorMsg() {
        return errorFileUploadErrorMsg;
    }

    public String getErrorTimecardCreateMsg() {
        return errorTimecardCreateMsg;
    }

    public String getErrorWorkHourCreateMsg() {
        return errorWorkHourCreateMsg;
    }

    public TimeCardCreateDialog getTimeCardCreateDialog() {
        return timeCardCreateDialog;
    }

    public WorkHourCreateDialog getWorkHourCreateDialog() {
        return workHourCreateDialog;
    }

    public class TimeCardCreateDialog implements Serializable, SelectableDataModel<TimecardSummary> {

        private static final long serialVersionUID = 191509454028231568L;

        private final List<TimecardSummary> timecardSummaries;
        private List<TimecardSummary> selected;
        private boolean skipTimecard = false;

        public TimeCardCreateDialog(List<TimecardSummary> timecardSummaries) {
            this.timecardSummaries = timecardSummaries;
            selected = new ArrayList<>(timecardSummaries); // default, all is selected.
        }

        public List<TimecardSummary> getTimecardSummaries() {
            return timecardSummaries;
        }

        public List<TimecardSummary> getSelected() {
            return selected;
        }

        public void setSelected(final List<TimecardSummary> selected) {
            this.selected = selected;
        }

        public void yesCreateTimecards() {
            try {
                jiraImportService.createTimecardsToPhzIntra(selected);
                status = STATUS.TIMECARD_CREATE_SUCCESS;

            } catch (Exception e) {
                status = STATUS.TIMECARD_CREATE_FAIL;
                LOG.error("Failed to create time cards. {}", selected, e);
                errorTimecardCreateMsg = e.getMessage();
            }
            createNextStep();
        }

        public void noSkipCreateTimecards() {
            createNextStep();
            status = STATUS.TIMECARD_CREATE_SUCCESS;
            skipTimecard = true;
        }

        private void createNextStep() {
            workHourCreateDialog = new WorkHourCreateDialog(timecardSummaries);
        }

        @Override
        public TimecardSummary getRowKey(final TimecardSummary selectedObject) {
            for (TimecardSummary timecardSummary : timecardSummaries) {
                if (selectedObject.equals(timecardSummary)) {
                    return timecardSummary;
                }
            }
            return null;
        }

        @Override
        public TimecardSummary getRowData(final String id) {
            for (TimecardSummary timecardSummary : timecardSummaries) {
                if (id.equals(timecardSummary.getId())) {
                    return timecardSummary;
                }
            }
            return null;
        }

        public boolean isSkipTimecard() {
            return skipTimecard;
        }
    }

    public class WorkHourCreateDialog implements Serializable, SelectableDataModel<TimecardSummary> {

        private static final long serialVersionUID = -988781715346042920L;

        private final List<TimecardSummary> timecardSummaries;
        private List<TimecardSummary> selected = new ArrayList<>();

        public WorkHourCreateDialog(final List<TimecardSummary> timecardSummaries) {
            this.timecardSummaries = timecardSummaries;
            selected.addAll(timecardSummaries); // as default all is selected.
        }

        public List<TimecardSummary> getTimecardSummaries() {
            return timecardSummaries;
        }

        public List<TimecardSummary> getSelected() {
            return selected;
        }

        public void setSelected(List<TimecardSummary> selected) {
            this.selected = selected;
        }

        @Override
        public TimecardSummary getRowKey(final TimecardSummary selectedObject) {
            for (TimecardSummary timecardSummary : timecardSummaries) {
                if (selectedObject.equals(timecardSummary)) {
                    return timecardSummary;
                }
            }
            return null;
        }

        @Override
        public TimecardSummary getRowData(final String id) {
            for (TimecardSummary timecardSummary : timecardSummaries) {
                if (id.equals(timecardSummary.getId())) {
                    return timecardSummary;
                }
            }
            return null;
        }

        public void yesCreateWorkHours() {
            try {
                jiraImportService.createWorkHoursToPhz(timecardsCreateSummary);
                status = STATUS.WORKHOUR_CREATE_SUCCESS;
            } catch (Exception e) {
                errorWorkHourCreateMsg = e.getMessage();
                status = STATUS.WORKHOUR_CREATE_FAIL;
            }
        }

        public void noSkipCreateWorkHours() {
            status = STATUS.WORKHOUR_CREATE_SUCCESS;
        }
    }
}