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
package org.springframework.sbm.build.migration.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class NoPluginRepositoryExistsConditionTest {

    @Test
    void repositoryWithSameUrlAndSnapshotsAndReleasesNotSet() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition noPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        noPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/milestone");
        boolean noRepositoryExists = noPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isFalse();
    }

    @Test
    void repositoryWithSameUrlAndSnapshotsFalseAndReleasesNotSet() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "            <snapshots>\n" +
                        "                <enabled>false</enabled>\n" +
                        "            </snapshots>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/milestone");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isFalse();
    }

    @Test
    void repositoryWithSameUrlAndSnapshotsTrueAndReleasesNotSet() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "            <snapshots>\n" +
                        "                <enabled>true</enabled>\n" +
                        "            </snapshots>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/milestone");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isFalse();
    }

    @Test
    void repositoryWithSameUrlAndSnapshotsNotSetAndReleasesTrue() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "            <releases>\n" +
                        "                <enabled>true</enabled>\n" +
                        "            </releases>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/milestone");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isFalse();
    }

    @Test
    void repositoryWithSameUrlAndSnapshotsNotSetAndReleasesFalse() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "            <releases>\n" +
                        "                <enabled>false</enabled>\n" +
                        "            </releases>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/milestone");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isFalse();
    }

    @Test
    void repositoryWithDifferentUrlAndSnapshotsSetToFalse() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "            <id>milestone</id>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/different");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isTrue();
    }

    @Test
    void repositoryWithDifferentUrlAndSameIdShouldReturnFalse() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <id>repository.spring.milestone</id>\n" +
                        "            <name>Spring Milestone Repository</name>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/different");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isTrue();
    }

    @Test
    void repositoryWithDifferentUrlAndNullIdShouldReturnTrue() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n" +
                        "    http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>group</groupId>\n" +
                        "    <artifactId>artifact</artifactId>\n" +
                        "    <version>0.11.2-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <pluginRepositories>\n" +
                        "        <pluginRepository>\n" +
                        "            <id>repository.spring.milestone</id>\n" +
                        "            <name>Spring Milestone Repository</name>\n" +
                        "            <url>https://repo.spring.io/milestone</url>\n" +
                        "        </pluginRepository>\n" +
                        "    </pluginRepositories>\n" +
                        "</project>";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        NoPluginRepositoryExistsCondition NoPluginRepositoryExistsCondition = new NoPluginRepositoryExistsCondition();
        NoPluginRepositoryExistsCondition.setUrl("https://repo.spring.io/different");
        boolean noRepositoryExists = NoPluginRepositoryExistsCondition.evaluate(context);

        assertThat(noRepositoryExists).isTrue();
    }

}