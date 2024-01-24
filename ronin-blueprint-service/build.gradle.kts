@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(roningradle.plugins.buildconventions.spring.service)
    alias(roningradle.plugins.localct)
}

dependencies {
    implementation(project(":ronin-blueprint-database"))
    implementation(productcommon.product.spring.webflux.starter)
    implementation(productcommon.bundles.spring.data)
    implementation(productcommon.okhttp)
    implementation(productcommon.kotlin.coroutines.core)
    implementation(productcommon.kotlinlogging)
    implementation(libs.contract.rest.ronin.blueprint.v1)

    runtimeOnly(productcommon.mysql.connector)

    developmentOnly(productcommon.spring.devtools)

    testImplementation(productcommon.bundles.spring.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(productcommon.bundles.testcontainers)
    testImplementation(productcommon.product.spring.jwt.auth.mocks)
    testImplementation(productcommon.kotlinx.coroutines.test)

    localContractTestImplementation(productcommon.product.contract.test.common)
    localContractTestImplementation(productcommon.product.spring.jwt.auth.testutils)
}

sourceSets {
    localContractTest {
        compileClasspath = configurations.localContractTestCompileClasspath.get() + configurations.compileClasspath.get()
    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}
