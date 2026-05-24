package co.japl.finances.core.adapters.inbound.implement.di

import co.com.japl.finances.iports.inbounds.common.GetTaxDeclarationUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxHistoryUseCase
import co.com.japl.finances.iports.inbounds.common.GetTaxProjectionUseCase
import co.com.japl.finances.iports.inbounds.common.SavePatrimonyAssetUseCase
import co.com.japl.finances.iports.inbounds.dian.IGetTaxDeclarationUseCase
import co.japl.finances.core.adapters.inbound.implement.dian.GetTaxDeclarationUseCaseImpl
import co.japl.finances.core.adapters.inbound.implement.dian.GetTaxHistoryUseCaseImpl
import co.japl.finances.core.adapters.inbound.implement.dian.GetTaxProjectionUseCaseImpl
import co.japl.finances.core.adapters.inbound.implement.dian.SavePatrimonyAssetUseCaseImpl
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
    abstract fun bindIGetTaxDeclarationUseCase(impl: co.japl.finances.core.usercases.implement.dian.GetTaxDeclarationUseCaseImpl): IGetTaxDeclarationUseCase

    @Binds
    abstract fun bindGetTaxHistoryUseCase(impl: GetTaxHistoryUseCaseImpl): GetTaxHistoryUseCase

    @Binds
    abstract fun bindGetTaxProjectionUseCase(impl: GetTaxProjectionUseCaseImpl): GetTaxProjectionUseCase

    @Binds
    abstract fun bindSavePatrimonyAssetUseCase(impl: SavePatrimonyAssetUseCaseImpl): SavePatrimonyAssetUseCase
}