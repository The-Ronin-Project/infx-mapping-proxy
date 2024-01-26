package com.projectronin.infx.mapping.proxy.controller

import com.projectronin.infx.mapping.proxy.config.MappingProxyProperties
import com.projectronin.product.common.auth.annotations.PreAuthEmployeesOnly
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MappingController(
    private val properties: MappingProxyProperties,
    private val client: OkHttpClient
) {
    private val logger = KotlinLogging.logger { }

    @PreAuthEmployeesOnly
    @GetMapping("/api/v1/{path}")
    suspend fun proxy(
        @PathVariable(value = "path", required = true) path: String,
        request: ServerHttpRequest
    ): ResponseEntity<ByteArray?> {
        val query = request.uri.query
        val uri = when {
            query != null -> "${properties.mappingUrl}/$path?$query"
            else -> "${properties.mappingUrl}/$path"
        }
        logger.info(uri)

        val mappingRequest = Request.Builder()
            .url(uri)
            .get()
            .build()

        val response = client.newCall(mappingRequest).execute()
        val res = ResponseEntity<ByteArray?>(response.body?.bytes(), HttpStatusCode.valueOf(response.code))

        response.close()
        return res
    }
}
