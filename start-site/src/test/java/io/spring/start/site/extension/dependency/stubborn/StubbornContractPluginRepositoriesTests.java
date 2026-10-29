/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import java.util.List;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.support.MetadataBuildItemResolver;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// TODO Remove once Stubborn Contract 0.2.2 is released
/**
 * Tests for the plugin repositories added by the Stubborn Contract build customizers.
 *
 * @author Moritz Halbritter
 */
class StubbornContractPluginRepositoriesTests extends AbstractExtensionTests {

	private static final String REPOSITORY_ID = "central-snapshots";

	@Test
	void mavenAddsBomRepositoriesAsPluginRepositories() {
		MavenBuild build = new MavenBuild(resolver());
		new StubbornContractMavenBuildCustomizer(bom(List.of(REPOSITORY_ID))).customize(build);
		assertThat(build.pluginRepositories().has(REPOSITORY_ID)).isTrue();
	}

	@Test
	void mavenAddsNoPluginRepositoriesForReleases() {
		MavenBuild build = new MavenBuild(resolver());
		new StubbornContractMavenBuildCustomizer(bom(List.of())).customize(build);
		assertThat(build.pluginRepositories().isEmpty()).isTrue();
	}

	@Test
	void gradleAddsBomRepositoriesAsPluginRepositories() {
		GradleBuild build = new GradleBuild(resolver());
		new StubbornContractGroovyDslGradleBuildCustomizer(bom(List.of(REPOSITORY_ID))).customize(build);
		assertThat(build.pluginRepositories().has(REPOSITORY_ID)).isTrue();
	}

	@Test
	void gradleAddsNoPluginRepositoriesForReleases() {
		GradleBuild build = new GradleBuild(resolver());
		new StubbornContractGroovyDslGradleBuildCustomizer(bom(List.of())).customize(build);
		assertThat(build.pluginRepositories().isEmpty()).isTrue();
	}

	private BuildItemResolver resolver() {
		return new MetadataBuildItemResolver(getMetadata(), Version.parse(SupportedBootVersion.latest().getVersion()));
	}

	private static BillOfMaterials bom(List<String> repositories) {
		BillOfMaterials bom = BillOfMaterials.create("sh.stubborn", "stubborn-contract-dependencies", "1.0.0");
		bom.setVersionProperty("stubborn-contract.version");
		bom.setRepositories(repositories);
		return bom;
	}

}
