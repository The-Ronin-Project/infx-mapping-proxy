package com.projectronin.infx.mapping.proxy.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ronin.infx.proxy")
class MappingProxyProperties (
    val mappingUrl: String = "http://localhost:5000"
)
