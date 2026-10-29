/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;

/**
 * WebFlux support shared by the Maven and Gradle customizers. Contract tests generated
 * for WebFlux use REST Assured's WebTestClient module instead of the MockMvc one.
 *
 * @author Moritz Halbritter
 */
final class StubbornContractWebFlux {

	private static final String WEBFLUX_ID = "webflux";

	private StubbornContractWebFlux() {
	}

	static boolean isPresent(Build build) {
		return build.dependencies().has(WEBFLUX_ID);
	}

	static void addRestAssuredWebTestClient(Build build) {
		build.dependencies()
			.add("rest-assured-spring-web-test-client",
					Dependency.withCoordinates("io.rest-assured", "spring-web-test-client")
						.scope(DependencyScope.TEST_COMPILE));
	}

}
