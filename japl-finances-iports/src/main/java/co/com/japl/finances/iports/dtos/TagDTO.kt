package co.com.japl.finances.iports.dtos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TagDTO(
    val id:Int,
    val create:LocalDate,
    val name:String,
    val active:Boolean
): Parcelable

