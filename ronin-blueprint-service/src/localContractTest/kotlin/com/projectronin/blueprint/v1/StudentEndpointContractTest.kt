package com.projectronin.blueprint.v1

import com.projectronin.product.contracttest.ContractTestContext
import com.projectronin.product.contracttest.LocalContractTestExtension
import com.projectronin.product.contracttest.contractTest
import com.projectronin.product.contracttest.services.ContractTestMySqlService
import com.projectronin.product.contracttest.wiremockReset
import com.projectronin.rest.restroninblueprint.v1.models.CreateStudentRequestBody
import com.projectronin.rest.restroninblueprint.v1.models.CreateStudentResponseBody
import com.projectronin.rest.restroninblueprint.v1.models.Student
import mu.KotlinLogging
import okhttp3.Request
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.time.LocalDate

/**
 * This test is an example of how to write a local contract test.  See inline comments.  It relies on configuration in ronin-blueprint-service/src/localContractTest/resources/test.properties,
 * and on docker-compose.yml.
 */
@ExtendWith(LocalContractTestExtension::class) // this starts docker before any tests are run, and shuts it down when they're all done
class StudentEndpointContractTest {
    private val logger = KotlinLogging.logger {}

    @BeforeEach
    fun setupMethod() {
        wiremockReset()
    }

    @AfterEach
    fun teardownMethod() {
        wiremockReset()
    }

    @Test
    fun `should create and retrieve student`() = contractTest {
        // this sets up a successful mock seki response
        val token = validAuthToken { roninEmployee() }

        // should be fairly straightforward:  call your service with a specific request, and
        val studentId = buildStudentsRequest {
            bearerAuthorization(token)
            post(requestBody(createStudentRequest))
        }.let { request ->
            executeRequest(request, HttpStatus.CREATED) { response ->
                response.readBodyValue<CreateStudentResponseBody>().id
            }
        }

        buildStudentsRequest("/$studentId") { bearerAuthorization(token) }.also { request ->
            val student = executeRequest(request) { response ->
                response.readBodyValue<Student>()
            }

            assertThat(student.id).isEqualTo(studentId)
            assertThat(student.firstName).isEqualTo(createStudentRequest.firstName)
            assertThat(student.lastName).isEqualTo(createStudentRequest.lastName)
            assertThat(student.favoriteNumber).isEqualTo(createStudentRequest.favoriteNumber)
            assertThat(student.birthDate).isEqualTo(createStudentRequest.birthDate)
            assertThat(student.createdAt).isNotNull()
            assertThat(student.updatedAt).isNotNull()
        }

        buildStudentsRequest { bearerAuthorization(token) }.also { request ->
            val students = executeRequest(request) { response ->
                response.readBodyValue<List<Student>>()
            }
            assertThat(students.size).isEqualTo(1)
        }
    }

    @Test
    fun `missing required field in create request should fail`() = contractTest {
        listOf("firstName", "lastName").forEach { requiredFieldName ->
            val request = buildStudentsRequest {
                bearerAuthorization(validAuthToken { roninEmployee() })
                post(
                    requestBody(createStudentRequest) { jsonNode ->
                        jsonNode.removeObjectField(requiredFieldName)
                    }
                )
            }

            executeRequest(request, HttpStatus.BAD_REQUEST) {}
        }
    }

    @Test
    fun `create request with only required fields should succeed`() = contractTest {
        val createRequestBody = CreateStudentRequestBody(
            firstName = "William",
            lastName = "Doi"
        )

        val request = buildStudentsRequest {
            bearerAuthorization(validAuthToken { roninEmployee() })
            post(requestBody(createRequestBody))
        }

        executeRequest(request, HttpStatus.CREATED) {}
    }

    @Test
    fun `should fail when not an employee`() = contractTest {
        val request = buildStudentsRequest {
            bearerAuthorization(validAuthToken())
            post(requestBody(createStudentRequest))
        }

        executeRequest(request, HttpStatus.FORBIDDEN) {}
    }

    @Test
    fun `should fail on bad auth`() = contractTest {
        val request = buildStudentsRequest {
            bearerAuthorization(invalidAuthToken())
            post(requestBody(createStudentRequest))
        }

        executeRequest(request, HttpStatus.UNAUTHORIZED) {}
    }

    @Test
    fun `should fail on no auth`() = contractTest {
        val request = buildStudentsRequest {
            post(requestBody(createStudentRequest))
        }

        executeRequest(request, HttpStatus.UNAUTHORIZED) {}
    }
}

private fun ContractTestContext.buildStudentsRequest(
    path: String = "",
    block: Request.Builder.() -> Unit = {}
) = buildRequest {
    url("$serviceUrl/api/v1/students$path")

    block()
}

private val createStudentRequest = CreateStudentRequestBody(
    firstName = "William",
    lastName = "Doi",
    favoriteNumber = 17,
    birthDate = LocalDate.parse("2012-12-07")
)

private fun ContractTestContext.debugDatabase() =
    with(serviceOfType<ContractTestMySqlService>() ?: fail("no db service")) {
        "dbService".withMetadata(
            "started" to started,
            "dbName" to dbName,
            "port" to mySqlPort,
            "username" to username
        )
    }

private fun String.withMetadata(
    metadata: List<Pair<String, Any>> = emptyList()
) = buildString {
    append(this@withMetadata)
    if (metadata.isNotEmpty()) {
        if (isNotEmpty()) {
            append(" ")
        }

        append(
            metadata.joinToString(", ", "[", "]") { (key, value) ->
                "$key: $value"
            }
        )
    }
}

private fun String.withMetadata(
    vararg metadata: Pair<String, Any>
) = withMetadata(metadata.toList())

private fun String.withMetadata(
    metadata: Map<String, Any>
) = withMetadata(metadata.toList())
