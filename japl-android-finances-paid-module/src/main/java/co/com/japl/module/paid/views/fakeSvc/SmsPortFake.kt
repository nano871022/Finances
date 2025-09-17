package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import java.time.LocalDateTime

class SmsPortFake : ISmsPort {
    override fun createBySms(name: String, value: Double, date: LocalDateTime, codeAccount: Int) {
    }
}
