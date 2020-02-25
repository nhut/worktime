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
package fi.donhut.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import javax.faces.context.FacesContext;

/**
 * Main application.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@SpringBootApplication
@EntityScan(basePackages = "fi.donhut")
@ComponentScan(basePackages = "fi.donhut")
public class WorkTimeApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WorkTimeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WorkTimeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        LOG.info("Running JSF version: {}", FacesContext.class.getPackage().getImplementationVersion());
    }
}
