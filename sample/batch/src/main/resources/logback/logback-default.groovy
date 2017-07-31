//evaluate(org.springframework.util.ResourceUtils.getURL('classpath:logback/logback-common.groovy').text)
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.TRACE

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n"
    }
}

logger("com.profullstack", INFO)
root(TRACE, ["CONSOLE"])

