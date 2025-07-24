package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.GracePeriodDTO

interface IPeriodGracePort {

    fun add(dto: GracePeriodDTO): Boolean

    fun delete(codeCredit: Int): Boolean

    fun hasGracePeriod(codeCredit: Int):Boolean

    fun getList(codeCredit: Int):List<GracePeriodDTO>
}