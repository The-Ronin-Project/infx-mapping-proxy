package com.projectronin.blueprint.config

import com.projectronin.blueprint.repository.StudentRepository
import com.projectronin.blueprint.repository.jpa.StudentEntityRepository
import com.projectronin.blueprint.repository.jpa.StudentRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean

@Configuration
class RepositoryConfiguration {
    @Bean
    fun entityStudentRepository() = JpaRepositoryFactoryBean(StudentEntityRepository::class.java)

    @Bean
    fun studentRepository(entityRepository: StudentEntityRepository): StudentRepository =
        StudentRepositoryImpl(entityRepository)
}
