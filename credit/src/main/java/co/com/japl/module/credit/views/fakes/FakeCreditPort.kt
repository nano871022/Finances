package co.com.japl.module.credit.views.fakes

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.RecapCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.credit.ICreditPort
import java.time.LocalDate
import java.time.YearMonth

class FakeCreditPort : ICreditPort {
    override fun getCreditEnable(period: YearMonth): List<CreditDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getCreditsEnables(period: YearMonth): List<RecapCreditDTO> {
        TODO("Not yet implemented")
    }

    override fun getCredit(code: Int): CreditDTO? {
        TODO("Not yet implemented")
    }

}
