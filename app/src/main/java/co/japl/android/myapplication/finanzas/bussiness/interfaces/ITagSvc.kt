package co.japl.android.myapplication.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import java.util.Optional

interface ITagSvc : SaveSvc<TagDTO>{
}