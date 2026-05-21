package com.nano871022.finances.service.adapter.outbound.di

import com.nano871022.finances.iport.ports.outbound.*
import com.nano871022.finances.service.adapter.outbound.configuration.TaxConfigurationPortImpl
import com.nano871022.finances.service.adapter.outbound.database.PatrimonyPersistenceAdapter
import com.nano871022.finances.service.adapter.outbound.database.TaxHistoryPersistenceAdapter
import com.nano871022.finances.service.adapter.outbound.integration.FinancesModuleIntegrationAdapter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TaxServiceModule {

    @Binds
    abstract fun bindTaxConfigurationPort(impl: TaxConfigurationPortImpl): TaxConfigurationPort

    @Binds
    abstract fun bindExternalFinancialDataPort(impl: FinancesModuleIntegrationAdapter): ExternalFinancialDataPort

    @Binds
    abstract fun bindTaxHistoryPersistencePort(impl: TaxHistoryPersistenceAdapter): TaxHistoryPersistencePort

    @Binds
    abstract fun bindPatrimonyPersistencePort(impl: PatrimonyPersistenceAdapter): PatrimonyPersistencePort
}
