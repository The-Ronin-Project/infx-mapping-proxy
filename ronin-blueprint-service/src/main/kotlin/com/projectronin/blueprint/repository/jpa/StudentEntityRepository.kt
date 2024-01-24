package com.projectronin.blueprint.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface StudentEntityRepository : JpaRepository<StudentEntity, UUID>
