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

package com.consol.citrus.samples.bakery;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 * @since 2.3.1
 */
@Test
public class BakeryWeb_OK_Test extends TestNGCitrusTestDesigner {

    @Autowired
    @Qualifier("bakeryOrderEndpoint")
    private JmsEndpoint bakeryOrderEndpoint;

    @Autowired
    @Qualifier("factoryCakeEndpoint")
    private JmsEndpoint factoryCakeEndpoint;

    @Autowired
    @Qualifier("factoryPretzelEndpoint")
    private JmsEndpoint factoryPretzelEndpoint;

    @Autowired
    @Qualifier("factoryBreadEndpoint")
    private JmsEndpoint factoryBreadEndpoint;

    @CitrusTest
    public void routeMessagesContentBased() {
        send(bakeryOrderEndpoint)
                .payload("<order type=\"cake\"><amount>1</amount></order>");

        receive(factoryCakeEndpoint)
                .payload("<order type=\"cake\"><amount>1</amount></order>");

        send(bakeryOrderEndpoint)
                .payload("<order type=\"pretzel\"><amount>1</amount></order>");

        receive(factoryPretzelEndpoint)
                .payload("<order type=\"pretzel\"><amount>1</amount></order>");

        send(bakeryOrderEndpoint)
                .payload("<order type=\"bread\"><amount>1</amount></order>");

        receive(factoryBreadEndpoint)
                .payload("<order type=\"bread\"><amount>1</amount></order>");
    }

    @CitrusTest
    public void routeUnknownOrderType() {
        send(bakeryOrderEndpoint)
                .payload("<order type=\"baguette\"><amount>1</amount></order>");

        receive("jms:factory.unknown.inbound")
                .payload("<order type=\"baguette\"><amount>1</amount></order>");
    }
}
