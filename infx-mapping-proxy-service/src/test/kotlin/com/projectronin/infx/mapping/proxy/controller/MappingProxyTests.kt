package com.projectronin.infx.mapping.proxy.controller

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.projectronin.auth.token.RoninUserType
import com.projectronin.infx.mapping.proxy.config.SharedConfigurationReference
import com.projectronin.product.common.testutils.JwtAuthMockConfig
import com.projectronin.product.common.testutils.JwtAuthMockHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["services.assets.uri=http://localhost:8081"]
)
@WireMockTest(httpPort = 8081)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(OutputCaptureExtension::class)
@Import(SharedConfigurationReference::class, JwtAuthMockConfig::class)
@ActiveProfiles("test")
class MappingProxyTests {
    @LocalServerPort
    private val serverPort = 0

    @Autowired
    private var webClient: WebTestClient? = null

    private val DEFAULT_AUTH_VALUE = "Bearer ${JwtAuthMockHelper.defaultToken}"

    @Test
    fun `simple successful request proxied`(output: CapturedOutput) {
        stubFor(
            get(urlEqualTo("/ping"))
                .willReturn(
                    aResponse()
                        .withBody("pong")
                )
        )

        mockAuth()
        webClient!!.get().uri("/api/v1/ping")
            .header(HttpHeaders.AUTHORIZATION, DEFAULT_AUTH_VALUE)
            .exchange()
            .expectStatus().isOk
            .returnResult(Void::class.java)

        verify(
            getRequestedFor(urlEqualTo("/ping"))
        )

        val out = output.out
        assertThat(out).contains("Proxying /api/v1/ping to http://localhost:8081/ping")
    }

    @Test
    fun `simple unauthorized no token not proxied`(output: CapturedOutput) {
        stubFor(
            get(urlEqualTo("/ping"))
                .willReturn(
                    aResponse()
                        .withBody("pong")
                )
        )

        webClient!!.get().uri("/api/v1/ping")
            .exchange()
            .expectStatus().isUnauthorized
            .returnResult(Void::class.java)

        val out = output.out
        assertThat(out).contains("Authentication failed")
    }

    fun mockAuth() {
        val auth = JwtAuthMockHelper.defaultAuthenticationToken(
            roninClaims = JwtAuthMockHelper.defaultRoninClaims(
                userType = RoninUserType.RoninEmployee
            )
        )

        JwtAuthMockHelper.configure(JwtAuthMockHelper.createAuthenticationProvider { auth })
    }
}
