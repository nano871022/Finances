package co.com.japl.module.credit.pojo

import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.dtos.GracePeriodDTO

data class CreditPeriodGraceDTO (
    val credit:CreditDTO,
    val periodGrace:Boolean
)