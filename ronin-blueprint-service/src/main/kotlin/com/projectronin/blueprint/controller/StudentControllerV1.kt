package com.projectronin.blueprint.controller

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.service.StudentService
import com.projectronin.product.common.auth.annotations.PreAuthEmployeesOnly
import com.projectronin.rest.restroninblueprint.v1.controllers.StudentsController
import com.projectronin.rest.restroninblueprint.v1.models.CreateStudentRequestBody
import com.projectronin.rest.restroninblueprint.v1.models.CreateStudentResponseBody
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import com.projectronin.rest.restroninblueprint.v1.models.Student as StudentContract

/**
 * StudentController handles the REST calls for student objects.
 *
 * NOTE: it is assumed that Authorization was successful _PRIOR_
 *  to hitting any of the endpoints below.
 *
 *
 */
@RestController
class StudentControllerV1(
    private val studentService: StudentService
) : StudentsController {

    private val logger = KotlinLogging.logger { }

    @PreAuthEmployeesOnly
    override suspend fun addStudent(
        createStudentRequestBody: CreateStudentRequestBody
    ): ResponseEntity<CreateStudentResponseBody> = runBlocking {
        val student = studentService.create(createStudentRequestBody.toModel())
        logger.info("Created student with id: ${student.id}")

        ResponseEntity(CreateStudentResponseBody(student.id), HttpStatus.CREATED)
    }

    @PreAuthEmployeesOnly
    override suspend fun getStudentById(id: UUID): ResponseEntity<StudentContract> = runBlocking {
        logger.info { "Retrieving student record '$id'" }
        ResponseEntity(studentService.findById(id).toContract(), HttpStatus.OK)
    }

    @PreAuthEmployeesOnly
    override suspend fun getAllStudents(): ResponseEntity<List<StudentContract>> = runBlocking {
        logger.info { "Retrieving all student records" }
        ResponseEntity(studentService.findAll().map { it.toContract() }, HttpStatus.OK)
    }
}

/**
 * Converts a create request into a model
 *
 * Decided to use model instead of entity to not duplicate dataMap conversion logic
 */
private fun CreateStudentRequestBody.toModel(now: Instant = Instant.now()) = Student(
    id = UUID.randomUUID(),
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    favoriteNumber = favoriteNumber,
    createdAt = now,
    updatedAt = now
)

internal fun Student.toContract() = StudentContract(
    id = id,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    favoriteNumber = favoriteNumber,
    createdAt = createdAt.toUtcOffsetDateTime(),
    updatedAt = updatedAt.toUtcOffsetDateTime()
)

internal fun Instant.toUtcOffsetDateTime(): OffsetDateTime = OffsetDateTime.ofInstant(this, ZoneOffset.UTC)
