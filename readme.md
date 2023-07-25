# Metrics Consumer Demo

This is a simple demo that queries an Ikasan Metrics Service over configured periods
within a time window.

Log files are written to a `log` directory under the root of the project. Log files are managed
in the [logback-spring.xml](./src/main/resources/logback-spring.xml) configuration file.

Application properties are managed in the [application.properties](./src/main/resources/application.properties) file.

````properties
ikasan.dashboard.base.url=replace me
ikasan.dashboard.rest.username=replace me
ikasan.dashboard.rest.password=replace me
ikasan.dashboard.rest.userAgent=demo-metrics-client

spring.autoconfigure.exclude=org.ikasan.dashboard.DashboardClientAutoConfiguration

metrics.query.time.window.start.milli=1690185600000
metrics.query.time.window.end.milli=1690222524939
metrics.polling.interval.milli=1000
````