package com.projectronin.blueprint

import com.projectronin.product.contracttest.services.ContractServicesProvider
import com.projectronin.product.contracttest.services.ContractTestMySqlService
import com.projectronin.product.contracttest.services.ContractTestService
import com.projectronin.product.contracttest.services.ContractTestServiceUnderTest
import com.projectronin.product.contracttest.services.ContractTestWireMockService

class StudentDataServicesProvider : ContractServicesProvider {
    override fun provideServices(): List<ContractTestService> {
        return listOf(
            ContractTestServiceUnderTest(
                listOf(
                    ContractTestWireMockService(),
                    ContractTestMySqlService(
                        "test",
                        "test",
                        "student_data"
                    )
                )
            )
        )
    }
}
