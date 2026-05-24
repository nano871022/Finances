package co.com.japl.finances.iports.inbounds.dian

import co.com.japl.finances.iports.dtos.dian.Form210DTO

interface IGetTaxDeclarationUseCase {
    suspend fun getTaxDeclaration(year: Int): Form210DTO
}
