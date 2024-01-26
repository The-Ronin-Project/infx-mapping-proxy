package com.projectronin.infx.mapping.proxy.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Class to load in config definitions that live outside of the current project
 *   (i.e. the 'common' project)
 */
@ComponentScan("com.projectronin.product.common.config")
@Configuration
@EnableConfigurationProperties(MappingProxyProperties::class)
class SharedConfigurationReference
