package org.ikasan.metrics.demo;

import org.ikasan.dashboard.MetricsRestServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @ConfigurationProperties(prefix = "module.rest.connection")
    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory()
    {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();

        // all of the properties can be overwritten using spring properties.
        httpComponentsClientHttpRequestFactory.setReadTimeout(5000);
        httpComponentsClientHttpRequestFactory.setConnectTimeout(5000);
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(5000);

        return httpComponentsClientHttpRequestFactory;
    }

    @Bean
    MetricsRestServiceImpl metricsRestService(Environment environment
            , HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        return new MetricsRestServiceImpl(environment, httpComponentsClientHttpRequestFactory);
    }
}
