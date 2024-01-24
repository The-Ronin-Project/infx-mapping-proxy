package com.projectronin.blueprint.repository

import com.projectronin.blueprint.model.Student
import com.projectronin.product.common.exception.NotFoundException
import java.util.UUID

interface StudentRepository {
    suspend fun save(model: Student): Student

    suspend fun findByIdOrNull(id: UUID): Student?
    suspend fun findById(id: UUID): Student =
        findByIdOrNull(id) ?: throw NotFoundException(id.toString())

    suspend fun findAll(): List<Student>
}
