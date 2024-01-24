package com.projectronin.blueprint.model

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class Student(
    val id: UUID = UUID.randomUUID(),
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val favoriteNumber: Int? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
