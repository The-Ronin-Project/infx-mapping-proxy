package com.projectronin.blueprint.repository

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.repository.jpa.StudentEntityRepository
import com.projectronin.database.helpers.MysqlVersionHelper
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@SpringBootTest(
    properties = [
        MysqlVersionHelper.MYSQL_SPRING_DATASOURCE_OCI
    ]
)
class StudentRepositoryTest @Autowired constructor(
    val repository: StudentRepository,
    val jpaRepository: StudentEntityRepository
) {

    @AfterEach
    fun deleteObjects() {
        jpaRepository.deleteAll()
    }

    @Test
    fun `can save and load`() {
        runBlocking {
            val uuid = UUID.randomUUID()
            val student = Student(
                id = uuid,
                firstName = "Claude",
                lastName = "Morales",
                birthDate = null,
                favoriteNumber = null,
                createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS),
                updatedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
            repository.save(student)

            val foundStudent = repository.findById(uuid)
            assertThat(foundStudent).isEqualTo(student)
        }
    }
}
