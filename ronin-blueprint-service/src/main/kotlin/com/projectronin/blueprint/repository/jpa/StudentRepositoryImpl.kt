package com.projectronin.blueprint.repository.jpa

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.repository.StudentRepository
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

class StudentRepositoryImpl(
    private val entityRepository: StudentEntityRepository
) : StudentRepository {
    override suspend fun save(model: Student): Student = runBlocking {
        entityRepository.save(model.toEntity()).toModel()
    }

    override suspend fun findByIdOrNull(id: UUID): Student? = runBlocking {
        entityRepository.findById(id).getOrNull()?.toModel()
    }

    override suspend fun findAll(): List<Student> = runBlocking {
        entityRepository.findAll().map { it.toModel() }
    }
}

/**
 * Converts a student entity into a model
 */
private fun StudentEntity.toModel() = Student(
    id = id,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    favoriteNumber = favoriteNumber,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Student.toEntity() = StudentEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    birthDate = birthDate,
    favoriteNumber = favoriteNumber,
    createdAt = createdAt,
    updatedAt = updatedAt
)
