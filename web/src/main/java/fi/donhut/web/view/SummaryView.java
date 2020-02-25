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

import fi.donhut.client.phz.generated.myown.TimecardsGetResponse;
import fi.donhut.web.client.phz.PhzTimeProjectsOtherApi;
import fi.donhut.web.model.Bonus;
import fi.donhut.web.util.ResourceBundleProvider;
import fi.donhut.web.view.misc.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages summary view.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Named
@ViewScoped
public class SummaryView implements Serializable {

    private static final long serialVersionUID = -3597474003518116514L;
    private static final Logger LOG = LoggerFactory.getLogger(SummaryView.class);

    @Inject
    private UserManager userManager;

    @Inject
    private PhzTimeProjectsOtherApi timeProjectsOtherApi;

    private List<Bonus> invoices = new ArrayList<>();
    private String errorInvoices;

    public SummaryView() {
        LOG.trace("{} constructor initialized.", this.getClass().getSimpleName());
    }

    @PostConstruct
    public void init() {
        initializeInvoiceData();
    }

    private void initializeInvoiceData() {
        invoices.clear();
        if (timeProjectsOtherApi.isEnabled()) {
            final String username = userManager.getUsername();
            try {
                LOG.trace("Fetching invoices...");
                invoices.addAll(timeProjectsOtherApi.getBonus(username));
                invoices = invoices.stream().sorted(Comparator.comparing(Bonus::getMonthS).reversed())
                        .collect(Collectors.toList());
                LOG.debug("Found total of invoices: {}", invoices.size());
                errorInvoices = null;

            } catch (Exception e) {
                LOG.error("(user={}) Failed to get Bonus info from PHZ API.", username);
                errorInvoices = ResourceBundleProvider.serverMsg("fetch_invoices_fail");
            }

        }
    }

    public List<Bonus> getInvoices() {
        return invoices;
    }

    public List<TimecardsGetResponse> getTimecardsDesc(List<TimecardsGetResponse> timecards) {
        final List<TimecardsGetResponse> reservedList = new ArrayList<>(timecards);
        Collections.reverse(reservedList);
        return reservedList;
    }

    public BigDecimal getInvoiceRate(Bonus bonus) {
        if (bonus == null || bonus.getCumulativeInvoiceabilityRate() == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.FLOOR);
        }
        return BigDecimal.valueOf(bonus.getCumulativeInvoiceabilityRate())
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR);
    }

    public String getErrorInvoices() {
        return errorInvoices;
    }
}
