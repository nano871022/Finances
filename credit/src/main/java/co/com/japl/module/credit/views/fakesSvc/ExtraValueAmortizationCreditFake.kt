package co.com.japl.module.credit.views.fakesSvc

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.inbounds.credit.IExtraValueAmortizationCreditPort

class ExtraValueAmortizationCreditFake : IExtraValueAmortizationCreditPort{
    override fun getAll(id: Int): List<ExtraValueAmortizationDTO> {
        TODO("Not yet implemented")
    }

    override fun save(id: Int, numQuotes: Long, value: Double): Int {
        TODO("Not yet implemented")
    }
}