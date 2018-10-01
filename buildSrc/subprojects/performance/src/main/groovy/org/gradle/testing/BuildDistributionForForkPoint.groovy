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

package org.gradle.testing

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.eclipse.jgit.api.Git

@CacheableTask
class BuildDistributionForForkPoint extends DefaultTask {
    @Input
    int maxBacktrackCommitCount = 10

    @Input
    String forkPointCommitId

    @Internal
    String branch

    @OutputFile
    File distributionZip = new File(project.rootProject.buildDir,"distributions/gradle-forkpoint-${forkPointCommitId}-bin.zip")

    @TaskAction
    void buildDistribution() {
        def clone = Git.cloneRepository()
        clone.URI = "https://github.com/gradle/gradle.git"
        clone.directory = new File(project.rootProject.buildDir, "tmp/gradle-forkpoint-${forkPointCommitId}")
        def git = clone.call()

        def commitId = git.repository.findRef(maxBacktrackCommitCount).objectId.name()
        println "> Building commit $commitId..."
    }
}
