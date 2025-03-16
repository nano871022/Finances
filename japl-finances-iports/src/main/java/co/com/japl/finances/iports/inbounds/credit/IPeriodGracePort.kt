package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.GracePeriodDTO

interface IPeriodGracePort {

    fun add(dto:GracePeriodDTO):Boolean

    fun delete(codeCredit:Int):Boolean

    fun hasGracePeriod(codeCredit: Int):Boolean
}