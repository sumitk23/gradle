/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.integtests.fixtures.executer


import org.gradle.test.fixtures.file.TestFile
import org.gradle.util.GradleVersion

class LocallyBuiltGradleDistribution extends DefaultGradleDistribution {
    LocallyBuiltGradleDistribution(String version) {
        super(GradleVersion.version(version), getGradleHomeForBinDistribution(version), getBinDistribution(version))
    }

    private static TestFile getBinDistribution(String version) {
        return IntegrationTestBuildContext.INSTANCE.getDistributionsDir().file("gradle-forkpoint/gradle-${version}-bin.zip")
    }

    private static TestFile getGradleHomeForBinDistribution(String version) {
        TestFile gradleHome = IntegrationTestBuildContext.INSTANCE.getDistributionsDir().file("gradle-forkpoint/gradle-${version}")
        if (!gradleHome.isDirectory()) {
            getBinDistribution(version).unzipTo(gradleHome.parentFile)
            TestFile unzippedDirectory = gradleHome.parentFile.listFiles().find { it.name.startsWith("gradle-${getBaseVersion(version)}") && it.isDirectory() }
            unzippedDirectory.renameTo(gradleHome) // default name is `gradle-5.1-20181003160000+0000`
        }
        return gradleHome
    }

    static boolean isLocallyBuiltVersion(String version) {
        return version.contains("-commit-")
    }

    static String getBaseVersion(String version) {
        return version.split('-')[0]
    }

    static File getToolingApiJar(String version) {
        return IntegrationTestBuildContext.INSTANCE.getDistributionsDir().file("gradle-forkpoint/gradle-tooling-api-${version}.jar")
    }
}
