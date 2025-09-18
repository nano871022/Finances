package co.com.japl.module.paid.views.monthly.list.fakes

import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import java.math.BigDecimal
import java.time.LocalDateTime

class FakeSmsPort : ISmsPort {
    override fun createBySms(
        name: String,
        value: BigDecimal,
        date: LocalDateTime,
        codeAccount: Int
    ): Boolean {
        return true
    }
}
