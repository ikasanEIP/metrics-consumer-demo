package org.ikasan.metrics.demo;


import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;
import org.ikasan.dashboard.MetricsRestServiceImpl;
import org.ikasan.dashboard.dto.MetricEventImpl;
import org.ikasan.spec.history.ComponentInvocationMetric;
import org.ikasan.spec.history.FlowInvocationMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    /** Logger for this class */
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final long MILLI_IN_MINUTE = 60 * 1000;

    @Autowired
    private MetricsRestServiceImpl metricsRestService;

    @Value("${metrics.query.time.window.start.milli}")
    long timeWindowStartMilli;

    @Value("${metrics.query.time.window.end.milli}")
    long timeWindowEndMilli;

    @Value("${metrics.polling.interval.milli:1000}")
    long pollingIntervalMilli;

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder()
                .environment(new StandardEncryptableEnvironment())
                .sources(Application.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        long startOffset = timeWindowStartMilli;
        long endOffset = 0L;

        HashMap<String, MetricsDetails> componentMetrics = new HashMap<>();

        while (endOffset < timeWindowEndMilli) {
            endOffset = startOffset + MILLI_IN_MINUTE;
            List<FlowInvocationMetric> flowInvocationMetrics = metricsRestService.getMetrics(startOffset, endOffset);

            flowInvocationMetrics.forEach(flowInvocationMetric -> flowInvocationMetric.getFlowInvocationEvents()
                .forEach(c -> {
                    String key = String.format("ModuleName[%s], FlowName[%s], ComponentName[%s]"
                            , flowInvocationMetric.getModuleName(), flowInvocationMetric.getFlowName(), ((ComponentInvocationMetric)c).getComponentName());

                    if(!componentMetrics.containsKey(key)) {
                        componentMetrics.put(key, new MetricsDetails());
                    }

                    componentMetrics.get(key).incrementCount();

                    if (((ComponentInvocationMetric)c).getWiretapFlowEvent() != null) {
                        MetricEventImpl metricEvent = (MetricEventImpl) ((ComponentInvocationMetric)c).getWiretapFlowEvent();

                        logger.info(String.format("ModuleName[%s], FlowName[%s], ComponentName[%s], Date/Time[%s]"
                                , metricEvent.getModuleName(), metricEvent.getFlowName(), metricEvent.getComponentName(), new Date(metricEvent.getTimestamp())));
                        logger.info(metricEvent.getEvent());

                        componentMetrics.get(key).incrementPayloadCount();
                    }
                }));

            startOffset = endOffset;

            Thread.sleep(pollingIntervalMilli);
        }

        componentMetrics.entrySet().forEach(entry -> {
            logger.info(String.format("Component[%s], Occurrence[%s], Associated Payload[%s]", entry.getKey(),
                    entry.getValue().getCount(), entry.getValue().getPayloadCount()));
        });
    }
}
