/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.jee.jpa.recipes;

import org.springframework.sbm.test.RecipeIntegrationTestSupport;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class MigratePersistenceXmlToBootRecipeTest {

    @Test
    void migrateEclipseLinkPersistence() {
        String targetDirName = "eclipselink-jpa";
        Path sourceDir = Path.of("./testcode/jee/jpa").resolve(targetDirName).resolve("given");
        String recipeName = "migrate-jpa-to-spring-boot";

        RecipeIntegrationTestSupport.initializeProject(sourceDir, targetDirName)
                .andApplyRecipe(recipeName);

        Path applicationProperties = RecipeIntegrationTestSupport.getResultDir(targetDirName).resolve("src/main/resources/application.properties");
        assertThat(applicationProperties).hasContent(
                """
                        spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
                        spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
                        spring.datasource.username=scott
                        spring.datasource.password=tiger
                        """
        );

        Path pomXml = RecipeIntegrationTestSupport.getResultDir(targetDirName).resolve("pom.xml");
        assertThat(pomXml).hasContent(
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>org.example</groupId>
                            <artifactId>eclipselink-jpa</artifactId>
                            <version>1.0-SNAPSHOT</version>
                            <properties>
                                <maven.compiler.source>11</maven.compiler.source>
                                <maven.compiler.target>11</maven.compiler.target>
                            </properties>
                            <dependencies>
                                <dependency>
                                    <groupId>org.eclipse.persistence</groupId>
                                    <artifactId>org.eclipse.persistence.jpa</artifactId>
                                    <version>2.7.10</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-data-jpa</artifactId>
                                    <version>2.6.3</version>
                                    <exclusions>
                                        <exclusion>
                                            <groupId>org.hibernate</groupId>
                                            <artifactId>hibernate-core</artifactId>
                                        </exclusion>
                                    </exclusions>
                                </dependency>
                                <dependency>
                                    <groupId>com.h2database</groupId>
                                    <artifactId>h2</artifactId>
                                    <version>2.1.210</version>
                                    <scope>runtime</scope>
                                </dependency>
                            </dependencies>
                        </project>
                        """
        );

        Path eclipselinkConfig = RecipeIntegrationTestSupport.getResultDir(targetDirName).resolve("src/main/java/com/example/EclipseLinkJpaConfiguration.java");
        assertThat(eclipselinkConfig).hasContent(
                """
                        package com.example;
                                                
                        import org.eclipse.persistence.config.PersistenceUnitProperties;
                        import org.eclipse.persistence.logging.SessionLog;
                        import org.springframework.beans.factory.ObjectProvider;
                        import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
                        import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.context.annotation.EnableLoadTimeWeaving;
                        import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
                        import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
                        import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
                        import org.springframework.transaction.jta.JtaTransactionManager;
                                                
                        import javax.sql.DataSource;
                        import java.util.HashMap;
                        import java.util.Map;
                                                
                        @Configuration
                        @EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
                        public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {
                                                
                            protected EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
                                super(dataSource, properties, jtaTransactionManager);
                            }
                                                
                            @Override
                            protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
                                return new EclipseLinkJpaVendorAdapter();
                            }
                                                
                            @Override
                            protected Map<String, Object> getVendorProperties() {
                                Map<String, Object> map = new HashMap<>();
                                                
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE");
                                map.put(PersistenceUnitProperties.VALIDATE_EXISTENCE, "PersistenceUnitProperties.VALIDATE_EXISTENCE");
                                map.put(PersistenceUnitProperties.SESSION_NAME, "PersistenceUnitProperties.SESSION_NAME");
                                map.put("eclipselink.concurrency.manager.waittime", "concurrency.manager.waittime");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT, "PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD, "PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_USER, "PersistenceUnitProperties.COORDINATION_JNDI_USER");
                                map.put(PersistenceUnitProperties.WEAVING_LAZY, "PersistenceUnitProperties.WEAVING_LAZY");
                                map.put(PersistenceUnitProperties.JPQL_PARSER, "PersistenceUnitProperties.JPQL_PARSER");
                                map.put("eclipselink.persisencexml.default", "persisencexml.default");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND, "PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND");
                                map.put(PersistenceUnitProperties.BATCH_WRITING, "PersistenceUnitProperties.BATCH_WRITING");
                                map.put(PersistenceUnitProperties.CACHE_STATEMENTS, "PersistenceUnitProperties.CACHE_STATEMENTS");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT, "PersistenceUnitProperties.COMPOSITE_UNIT");
                                map.put(PersistenceUnitProperties.JPQL_VALIDATION, "PersistenceUnitProperties.JPQL_VALIDATION");
                                map.put(PersistenceUnitProperties.TARGET_DATABASE, "PersistenceUnitProperties.TARGET_DATABASE");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER, "PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER");
                                map.put(PersistenceUnitProperties.THROW_EXCEPTIONS, "PersistenceUnitProperties.THROW_EXCEPTIONS");
                                map.put(PersistenceUnitProperties.LOGGING_CONNECTION, "PersistenceUnitProperties.LOGGING_CONNECTION");
                                map.put(PersistenceUnitProperties.COORDINATION_NAMING_SERVICE, "PersistenceUnitProperties.COORDINATION_NAMING_SERVICE");
                                map.put(PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE, "PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE");
                                map.put("eclipselink.concurrency.manager.allow.concurrencyexception", "concurrency.manager.allow.concurrencyexception");
                                map.put(PersistenceUnitProperties.LOGGING_EXCEPTIONS, "PersistenceUnitProperties.LOGGING_EXCEPTIONS");
                                map.put(PersistenceUnitProperties.DATABASE_EVENT_LISTENER, "PersistenceUnitProperties.DATABASE_EVENT_LISTENER");
                                map.put(PersistenceUnitProperties.WEAVING_EAGER, "PersistenceUnitProperties.WEAVING_EAGER");
                                map.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE, "PersistenceUnitProperties.CREATE_JDBC_DDL_FILE");
                                map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML");
                                map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT");
                                map.put(PersistenceUnitProperties.LOGGING_FILE, "PersistenceUnitProperties.LOGGING_FILE");
                                map.put(PersistenceUnitProperties.LOGGING_TIMESTAMP, "PersistenceUnitProperties.LOGGING_TIMESTAMP");
                                map.put(PersistenceUnitProperties.COORDINATION_PROTOCOL, "PersistenceUnitProperties.COORDINATION_PROTOCOL");
                                map.put(PersistenceUnitProperties.WEAVING_CHANGE_TRACKING, "PersistenceUnitProperties.WEAVING_CHANGE_TRACKING");
                                map.put(PersistenceUnitProperties.SQL_CAST, "PersistenceUnitProperties.SQL_CAST");
                                map.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, "PersistenceUnitProperties.SESSION_CUSTOMIZER");
                                map.put(PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES, "PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES");
                                map.put("eclipselink.concurrency.manager.maxfrequencytodumptinymessage", "concurrency.manager.maxfrequencytodumptinymessage");
                                map.put(PersistenceUnitProperties.WEAVING_FETCHGROUPS, "PersistenceUnitProperties.WEAVING_FETCHGROUPS");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP, "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP");
                                map.put("eclipselink.concurrency.manager.maxsleeptime", "concurrency.manager.maxsleeptime");
                                map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, "PersistenceUnitProperties.DDL_GENERATION_MODE");
                                map.put("eclipselink.nosql.property", "nosql.property");
                                map.put("eclipselink.concurrency.manager.allow.interruptedexception", "concurrency.manager.allow.interruptedexception");
                                map.put("eclipselink.connection-pool.read", "connection-pool.read");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE, "PersistenceUnitProperties.METADATA_SOURCE");
                                map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY, "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY");
                                map.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE, "PersistenceUnitProperties.DROP_JDBC_DDL_FILE");
                                map.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "PersistenceUnitProperties.CACHE_SHARED_DEFAULT");
                                map.put(PersistenceUnitProperties.ID_VALIDATION, "PersistenceUnitProperties.ID_VALIDATION");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE, "PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE");
                                map.put(PersistenceUnitProperties.WEAVING, "PersistenceUnitProperties.WEAVING");
                                map.put(PersistenceUnitProperties.COORDINATION_ASYNCH, "PersistenceUnitProperties.COORDINATION_ASYNCH");
                                map.put(PersistenceUnitProperties.ORM_SCHEMA_VALIDATION, "PersistenceUnitProperties.ORM_SCHEMA_VALIDATION");
                                map.put(PersistenceUnitProperties.TARGET_SERVER, "PersistenceUnitProperties.TARGET_SERVER");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_URL, "PersistenceUnitProperties.COORDINATION_RMI_URL");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER, "PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER");
                                map.put(PersistenceUnitProperties.PROFILER, "PersistenceUnitProperties.PROFILER");
                                map.put("eclipselink.concurrency.manager.maxfrequencytodumpmassivemessage", "concurrency.manager.maxfrequencytodumpmassivemessage");
                                map.put(PersistenceUnitProperties.DDL_GENERATION, "PersistenceUnitProperties.DDL_GENERATION");
                                map.put(PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES, "PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES");
                                map.put("eclipselink.concurrency.manager.allow.readlockstacktrace", "concurrency.manager.allow.readlockstacktrace");
                                map.put("eclipselink.cache.type", "cache.type");
                                map.put(PersistenceUnitProperties.PARTITIONING_CALLBACK, "PersistenceUnitProperties.PARTITIONING_CALLBACK");
                                map.put("eclipselink.connection-pool", "connection-pool");
                                map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE, "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE");
                                map.put(PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS, "PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS");
                                map.put(PersistenceUnitProperties.CLASSLOADER, "PersistenceUnitProperties.CLASSLOADER");
                                map.put(PersistenceUnitProperties.TEMPORAL_MUTABLE, "PersistenceUnitProperties.TEMPORAL_MUTABLE");
                                map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS, "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES");
                                map.put(PersistenceUnitProperties.LOGGING_SESSION, "PersistenceUnitProperties.LOGGING_SESSION");
                                map.put(PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS, "PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS");
                                map.put("eclipselink.connection-pool.sequence", "connection-pool.sequence");
                                map.put(PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES, "PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES");
                                map.put(PersistenceUnitProperties.PARTITIONING, "PersistenceUnitProperties.PARTITIONING");
                                map.put(PersistenceUnitProperties.TUNING, "PersistenceUnitProperties.TUNING");
                                map.put(PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE, "PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE");
                                map.put(PersistenceUnitProperties.APP_LOCATION, "PersistenceUnitProperties.APP_LOCATION");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_TOPIC, "PersistenceUnitProperties.COORDINATION_JMS_TOPIC");
                                map.put(PersistenceUnitProperties.NATIVE_SQL, "PersistenceUnitProperties.NATIVE_SQL");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT, "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT");
                                map.put(PersistenceUnitProperties.DEPLOY_ON_STARTUP, "PersistenceUnitProperties.DEPLOY_ON_STARTUP");
                                map.put(PersistenceUnitProperties.LOGGING_LEVEL, "PersistenceUnitProperties.LOGGING_LEVEL");
                                map.put(PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY, "PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY");
                                map.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, "PersistenceUnitProperties.BATCH_WRITING_SIZE");
                                map.put(PersistenceUnitProperties.LOGGING_THREAD, "PersistenceUnitProperties.LOGGING_THREAD");
                                map.put(PersistenceUnitProperties.SESSIONS_XML, "PersistenceUnitProperties.SESSIONS_XML");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES, "PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE, "PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY, "PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY");
                                map.put(PersistenceUnitProperties.ORACLE_PROXY_TYPE, "PersistenceUnitProperties.ORACLE_PROXY_TYPE");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_FACTORY, "PersistenceUnitProperties.COORDINATION_JMS_FACTORY");
                                map.put(PersistenceUnitProperties.NOSQL_CONNECTION_SPEC, "PersistenceUnitProperties.NOSQL_CONNECTION_SPEC");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE");
                                map.put("eclipselink.cache.size", "cache.size");
                                map.put(PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS, "PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS");
                                map.put("eclipselink.descriptor.customizer", "descriptor.customizer");
                                map.put(PersistenceUnitProperties.FLUSH_CLEAR_CACHE, "PersistenceUnitProperties.FLUSH_CLEAR_CACHE");
                                map.put("eclipselink.jdbc.property", "jdbc.property");
                                map.put(PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION, "PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_HOST, "PersistenceUnitProperties.COORDINATION_JMS_HOST");
                                map.put(PersistenceUnitProperties.WEAVING_INTERNAL, "PersistenceUnitProperties.WEAVING_INTERNAL");
                                map.put(PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY, "PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY");
                                map.put("eclipselink.ddl.table-creation-suffix", "ddl.table-creation-suffix");
                                map.put(PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION, "PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION");
                                map.put(PersistenceUnitProperties.JDBC_CONNECTOR, "PersistenceUnitProperties.JDBC_CONNECTOR");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_XML_URL, "PersistenceUnitProperties.METADATA_SOURCE_XML_URL");
                                map.put(PersistenceUnitProperties.COORDINATION_CHANNEL, "PersistenceUnitProperties.COORDINATION_CHANNEL");
                                map.put(PersistenceUnitProperties.CACHE_STATEMENTS_SIZE, "PersistenceUnitProperties.CACHE_STATEMENTS_SIZE");
                                                
                                return map;
                            }
                                                
                            @Bean
                            public InstrumentationLoadTimeWeaver loadTimeWeaver() throws Throwable {
                                InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
                                return loadTimeWeaver;
                            }
                        }
                        """);
    }

}
