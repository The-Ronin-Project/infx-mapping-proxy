package com.projectronin.blueprint.service

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.repository.StudentRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class StudentServiceTest {
    private val repository = mockk<StudentRepository>(relaxed = true)
    private val service = StudentService(repository)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `create student invokes repository`() = runTest {
        val student = buildStudent()
        coEvery { repository.save(any()) } answers { value }

        val result = service.create(student)

        assertEquals(student, result)
        coVerify(exactly = 1) { repository.save(any()) }
    }
}

private fun buildStudent(): Student {
    val now = Instant.now()
    return Student(
        firstName = "John",
        lastName = "Doe",
        birthDate = LocalDate.parse("1999-11-29"),
        favoriteNumber = 821,
        createdAt = now,
        updatedAt = now
    )
}
