/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import java.nio.file.Files;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for generation of projects that depend on Stubborn Contract Verifier.
 *
 * @author Moritz Halbritter
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRequestedDependency("stubborn-contract-verifier")
class StubbornContractProjectGenerationConfiguration {

	/**
	 * Default contracts location of the Maven plugin.
	 */
	private static final String MAVEN_CONTRACTS_DIRECTORY = "src/test/resources/contracts";

	/**
	 * Default contracts location of the Gradle plugin.
	 */
	private static final String GRADLE_CONTRACTS_DIRECTORY = "src/contractTest/resources/contracts";

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	StubbornContractProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	ProjectContributor stubbornContractDirectoryContributor(Build build) {
		String directory = (build instanceof MavenBuild) ? MAVEN_CONTRACTS_DIRECTORY : GRADLE_CONTRACTS_DIRECTORY;
		return (projectRoot) -> Files.createDirectories(projectRoot.resolve(directory));
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	StubbornContractMavenBuildCustomizer stubbornContractMavenBuildCustomizer() {
		return new StubbornContractMavenBuildCustomizer(resolveBom());
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	StubbornContractGroovyDslGradleBuildCustomizer stubbornContractGroovyDslGradleBuildCustomizer() {
		return new StubbornContractGroovyDslGradleBuildCustomizer(resolveBom());
	}

	@Bean
	@ConditionalOnBuildSystem(id = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	StubbornContractKotlinDslGradleBuildCustomizer stubbornContractKotlinDslGradleBuildCustomizer() {
		return new StubbornContractKotlinDslGradleBuildCustomizer(resolveBom());
	}

	private BillOfMaterials resolveBom() {
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms().get("stubborn-contract");
		return bom.resolve(this.description.getPlatformVersion());
	}

}
