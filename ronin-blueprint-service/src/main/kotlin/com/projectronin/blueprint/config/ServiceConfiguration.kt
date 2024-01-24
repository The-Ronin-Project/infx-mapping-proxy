package com.projectronin.blueprint.config

import com.projectronin.blueprint.repository.StudentRepository
import com.projectronin.blueprint.service.StudentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {
    @Bean
    fun studentService(studentRepository: StudentRepository) = StudentService(studentRepository)
}
