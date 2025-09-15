package co.com.japl.module.creditcard.views.fakesSvc

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import java.time.LocalDateTime

class BoughtSmsPortFake : IBoughtSmsPort {
    override fun createBySms(
        name: String,
        value: Double,
        date: LocalDateTime,
        codeCreditRate: Int,
        kind: KindInterestRateEnum
    ) {
    }
}
