package com.projectronin.blueprint

import com.projectronin.product.contracttest.LocalContractTestExtension
import com.projectronin.product.contracttest.contractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * This test is an example of how to write a local contact test.  See inline comments.  It relies on configuration in student-data-service/src/localContractTest/resources/test.properties,
 * and on docker-compose.yml.
 */
@ExtendWith(LocalContractTestExtension::class) // this starts docker before any tests are run, and shuts it down when they're all done
class OpenEndpointTest {
    @Test
    fun `should be able to retrieve actuator without auth`() = contractTest {
        val body = executeRequest("/actuator") { it.bodyString() }

        assertThat(body).contains("beans")
    }

    @Test
    fun `should load swagger ui`() = contractTest {
        val body = executeRequest("/swagger-ui.html") { it.bodyString() }

        assertThat(body).contains("<title>Swagger UI</title>")
    }

    @Test
    fun `should be able to get the contract`() = contractTest {
        val body = executeRequest("/v3/api-docs") { it.bodyString() }

        assertThat(body).contains("/api/v1/student")
    }
}
