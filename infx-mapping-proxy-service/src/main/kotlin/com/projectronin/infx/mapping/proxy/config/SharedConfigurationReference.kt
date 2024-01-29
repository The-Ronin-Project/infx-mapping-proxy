package com.projectronin.infx.mapping.proxy.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux

/**
 * Class to load in config definitions that live outside of the current project
 *   (i.e. the 'common' project)
 */
@ComponentScan("com.projectronin.product.common.config")
@Configuration
@EnableConfigurationProperties(MappingProxyProperties::class)
@EnableWebFlux
class SharedConfigurationReference
