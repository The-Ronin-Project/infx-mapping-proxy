@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(roningradle.plugins.buildconventions.spring.service)
}

dependencies {
    implementation(productcommon.product.spring.webflux.starter)
    implementation(productcommon.product.spring.httpclient)
    implementation(productcommon.okhttp)
    implementation(productcommon.kotlin.coroutines.core)
    implementation(productcommon.kotlinlogging)

    developmentOnly(productcommon.spring.devtools)

    testImplementation(productcommon.wiremock)
    testImplementation(productcommon.bundles.spring.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(productcommon.product.spring.jwt.auth.mocks)
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}
