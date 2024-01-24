package com.projectronin.blueprint.repository.jpa

import jakarta.persistence.Basic
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "student")
class StudentEntity(
    @Id
    @Basic
    @JdbcTypeCode(SqlTypes.CHAR)
    var id: UUID,
    var firstName: String,
    var lastName: String,
    var birthDate: LocalDate?,
    var favoriteNumber: Int?,
    var createdAt: Instant,
    var updatedAt: Instant
) {
    companion object
}
