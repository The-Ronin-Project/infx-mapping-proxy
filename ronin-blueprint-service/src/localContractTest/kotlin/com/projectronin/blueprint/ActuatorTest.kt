package com.projectronin.blueprint

import com.projectronin.product.contracttest.LocalContractTestExtension
import com.projectronin.product.contracttest.contractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * This test is an example of how to write a local contact test.  See inline comments.  It relies on configuration in ronin-blueprint-service/src/localContractTest/resources/test.properties,
 * and on docker-compose.yml.
 */
@ExtendWith(LocalContractTestExtension::class) // this starts docker before any tests are run, and shuts it down when they're all done
class ActuatorTest {
    @Test
    fun `should be able to retrieve actuator without auth`() = contractTest {
        val serviceInfo = executeRequest("/actuator/info") { response ->
            response.readBodyTree().get("serviceInfo")
        }

        assertThat(serviceInfo).isNotNull()
        assertThat(serviceInfo.isObject).isTrue()
        assertThat(serviceInfo["version"]?.textValue()).isNotBlank()
        assertThat(serviceInfo["lastTag"]?.textValue()).isNotBlank()
        assertThat(serviceInfo["commitDistance"]?.isInt).isTrue()
        assertThat(serviceInfo["gitHashFull"]?.textValue()).isNotBlank()
        assertThat(serviceInfo["branchName"]?.textValue()).isNotBlank()
        assertThat(serviceInfo["dirty"]?.isBoolean).isTrue()
    }
}
