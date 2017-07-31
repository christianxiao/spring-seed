import static ch.qos.logback.classic.Level.*
//How to use: this = logbackParent
enableJpaSqlLogger = {
    logbackParent.logger('org.hibernate.SQL', DEBUG)
    logbackParent.logger('org.hibernate.type', TRACE)
    logbackParent.logger('org.hibernate.type.BasicTypeRegistry', WARN)
}

disableJdbcSqlLogger = {
    logbackParent.logger('jdbc.sqlonly', OFF)
    logbackParent.logger('jdbc.sqltiming', OFF)
    logbackParent.logger('jdbc.audit', OFF)
    logbackParent.logger('jdbc.resultset', OFF)
    logbackParent.logger('jdbc.resultsettable', OFF)
    logbackParent.logger('jdbc.connection', OFF)
}

enableDevelopJdbcSqlLogger = {
    logbackParent.logger('jdbc.sqlonly', OFF)
    logbackParent.logger('jdbc.sqltiming', DEBUG)
    logbackParent.logger('jdbc.audit', OFF)
    logbackParent.logger('jdbc.resultset', WARN)
    logbackParent.logger('jdbc.resultsettable', WARN)
    logbackParent.logger('jdbc.connection', OFF)
}

enableProductionJdbcSqlLogger = {
    logbackParent.logger('jdbc.sqlonly', OFF)
    logbackParent.logger('jdbc.sqltiming', WARN)
    logbackParent.logger('jdbc.audit', OFF)
    logbackParent.logger('jdbc.resultset', WARN)
    logbackParent.logger('jdbc.resultsettable', WARN)
    logbackParent.logger('jdbc.connection', OFF)
}
