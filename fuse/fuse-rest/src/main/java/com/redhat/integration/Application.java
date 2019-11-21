/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.redhat.integration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
                new CamelHttpTransportServlet(), "/product-service/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            // @formatter:off
            restConfiguration()
                    //Enable swagger endpoint.

                .contextPath("/product-service").apiContextPath("/api-doc")
                    .apiProperty("api.title", "Camel REST API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiProperty("api.specification.contentType.json", "application/vnd.oai.openapi+json;version=2.0")
                    .apiProperty("api.specification.contentType.yaml", "application/vnd.oai.openapi;version=2.0")
                    .apiProperty("host", "0.0.0.0:8080") //by default 0.0.0.0
                    .apiProperty("port", "8080")
                    .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

            rest("/products").description("Product REST service")
                .get("/").description("The list of all the orders")
                    .route().routeId("orders-api")
                    .to("sql:select * from orders?" +
                        "dataSource=dataSource&" +
                        "outputClass=com.redhat.integration.Order")
                    .endRest()
                .get("order/{id}").description("Details of an order by id")
                    .route().routeId("order-api")
                    .to("sql:select * from orders where id = :#${header.id}?" +
                        "dataSource=dataSource&outputType=SelectOne&" +
                        "outputClass=com.redhat.integration.Order");

            rest("/products").description("Product REST service")
                .post("/placeorder").type(Order.class)
                    .description("Order generator")
                    .route().routeId("order-place-api")
                    //.bean("orderService", "generateOrder")
                        .to("sql:insert into orders (id, item, amount, description, processed) values " +
                            "(:#${body.id} , :#${body.item}, :#${body.amount}, :#${body.description}, false)?" +
                            "dataSource=dataSource")
                    .log("Inserted new order ${body.id}")
                    .endRest();
            // @formatter:on

        }
    }
}