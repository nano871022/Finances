package co.com.japl.finances.iports.outbounds

import java.math.BigDecimal

interface IInputPort {

    fun getTotalInputs():BigDecimal?
}