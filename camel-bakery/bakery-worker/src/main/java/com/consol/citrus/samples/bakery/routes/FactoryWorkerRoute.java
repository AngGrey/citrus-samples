/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.samples.bakery.routes;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Christoph Deppisch
 * @since 2.3.1
 */
@Component
public class FactoryWorkerRoute extends RouteBuilder {

    @Value("${FACTORY_TYPE}")
    private String factoryType = "default";

    @Value("${FACTORY_COSTS}")
    private String factoryCosts = "1000";

    @Value("${REPORT_PORT_8080_TCP_ADDR}")
    private String reportServerHost = "localhost";

    @Value("${REPORT_PORT_8080_TCP_PORT}")
    private String reportServerPort = "8080";

    @Override
    public void configure() throws Exception {
        from("jms:queue:factory." + factoryType + ".inbound").routeId(factoryType + "_factory")
            .setHeader("name", xpath("order/@type"))
            .setHeader("amount", xpath("order/amount/text()"))
            .delay(constant(factoryCosts))
            .setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .setBody(constant(""))
            .to("http://" + reportServerHost + ":" + reportServerPort + "/report/services/reporting");
    }
}
