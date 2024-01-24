package com.projectronin.blueprint.service

import com.projectronin.blueprint.model.Student
import com.projectronin.blueprint.repository.StudentRepository
import java.util.UUID

class StudentService(
    private val repository: StudentRepository
) {
    suspend fun create(student: Student) = repository.save(student)

    suspend fun findById(id: UUID) = repository.findById(id)

    suspend fun findAll() = repository.findAll()
}
