package com.nano871022.finances.core.domain.di

import com.nano871022.finances.core.domain.inbound.GetTaxDeclarationUseCaseImpl
import com.nano871022.finances.core.domain.inbound.GetTaxHistoryUseCaseImpl
import com.nano871022.finances.core.domain.inbound.GetTaxProjectionUseCaseImpl
import com.nano871022.finances.core.domain.inbound.SavePatrimonyAssetUseCaseImpl
import com.nano871022.finances.iport.ports.inbound.GetTaxDeclarationUseCase
import com.nano871022.finances.iport.ports.inbound.GetTaxHistoryUseCase
import com.nano871022.finances.iport.ports.inbound.GetTaxProjectionUseCase
import com.nano871022.finances.iport.ports.inbound.SavePatrimonyAssetUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TaxCoreModule {

    @Binds
    abstract fun bindGetTaxDeclarationUseCase(impl: GetTaxDeclarationUseCaseImpl): GetTaxDeclarationUseCase

    @Binds
    abstract fun bindGetTaxHistoryUseCase(impl: GetTaxHistoryUseCaseImpl): GetTaxHistoryUseCase

    @Binds
    abstract fun bindGetTaxProjectionUseCase(impl: GetTaxProjectionUseCaseImpl): GetTaxProjectionUseCase

    @Binds
    abstract fun bindSavePatrimonyAssetUseCase(impl: SavePatrimonyAssetUseCaseImpl): SavePatrimonyAssetUseCase
}
