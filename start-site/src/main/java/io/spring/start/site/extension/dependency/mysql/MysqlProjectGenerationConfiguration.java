/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.mysql;

import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeFileCustomizer;
import io.spring.initializr.generator.spring.container.dockercompose.DockerComposeService;
import io.spring.start.site.container.DockerImages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on MySQL.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("mysql")
class MysqlProjectGenerationConfiguration {

	@Bean
	@ConditionalOnRequestedDependency("docker-compose")
	DockerComposeFileCustomizer mysqlDockerComposeFileCustomizer() {
		return (composeFile) -> {
			DockerImages.DockerImage image = DockerImages.mysql();
			composeFile.addService(DockerComposeService.withImage(image.image(), image.tag())
				.name("mysql")
				.imageWebsite(image.website())
				.environment("MYSQL_ROOT_PASSWORD", "verysecret")
				.environment("MYSQL_USER", "myuser")
				.environment("MYSQL_PASSWORD", "secret")
				.environment("MYSQL_DATABASE", "mydatabase")
				.ports(3306)
				.build());
		};
	}

}
