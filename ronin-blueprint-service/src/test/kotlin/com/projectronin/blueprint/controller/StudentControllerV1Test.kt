package com.projectronin.blueprint.controller

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.service.StudentService
import com.projectronin.product.common.exception.NotFoundException
import com.projectronin.rest.restroninblueprint.v1.models.CreateStudentRequestBody
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import java.time.Instant
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class StudentControllerV1Test {
    private val studentService = mockk<StudentService>(relaxed = true)
    private val controller = StudentControllerV1(studentService)

    @BeforeEach
    fun setup() {
        clearAllMocks() // must ensure mocks in clean state at beginning of each test.
    }

    @Test
    fun `addStudent works`() = runTest {
        val student = buildStudent()

        coEvery { studentService.create(match { it.sameData(student) }) } returns student

        val response = controller.addStudent(
            CreateStudentRequestBody(
                firstName = student.firstName,
                lastName = student.lastName,
                birthDate = student.birthDate,
                favoriteNumber = student.favoriteNumber
            )
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)

        assertThat(response.body?.id).isEqualTo(student.id)

        coVerify {
            studentService.create(match { it.sameData(student) })
        }
    }

    @Test
    fun `getStudentById works`() = runTest {
        val student = buildStudent()

        coEvery { studentService.findById(student.id) } returns student

        val response = controller.getStudentById(student.id)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        with(response.body ?: fail("no body")) {
            assertThat(id).isEqualTo(student.id)
            assertThat(firstName).isEqualTo(student.firstName)
            assertThat(lastName).isEqualTo(student.lastName)
            assertThat(birthDate).isEqualTo(student.birthDate)
            assertThat(favoriteNumber).isEqualTo(student.favoriteNumber)
            assertThat(createdAt.toInstant()).isEqualTo(student.createdAt)
            assertThat(updatedAt.toInstant()).isEqualTo(student.updatedAt)
        }

        coVerify(exactly = 1) { studentService.findById(student.id) }
    }

    @Test
    fun `getStudentById fails with unknown student id`() = runTest {
        val studentId = UUID.randomUUID()

        coEvery { studentService.findById(studentId) } throws(NotFoundException(studentId.toString()))

        assertThrows<NotFoundException> {
            controller.getStudentById(studentId)
        }
    }

    @Test
    fun `getAllStudents works`() = runTest {
        val students = listOf(buildStudent())

        coEvery { studentService.findAll() } returns students

        val response = controller.getAllStudents()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val studentContracts = response.body ?: fail("no body")
        assertThat(studentContracts).isEqualTo(students.map { it.toContract() })
    }
}

private fun buildStudent(): Student {
    val now = Instant.now()
    return Student(
        firstName = "Jane",
        lastName = "Doe",
        birthDate = LocalDate.parse("1999-04-01"),
        favoriteNumber = 334,
        createdAt = now,
        updatedAt = now
    )
}

private fun Student.sameData(student: Student): Boolean =
    firstName == student.firstName &&
        lastName == student.lastName &&
        birthDate == student.birthDate &&
        favoriteNumber == student.favoriteNumber
