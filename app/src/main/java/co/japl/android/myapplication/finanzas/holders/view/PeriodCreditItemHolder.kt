package co.japl.android.myapplication.finanzas.holders.view

import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import co.japl.android.myapplication.finanzas.putParams.PeriodCreditListParams
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textview.MaterialTextView

class PeriodCreditItemHolder(val view:View,val navController: NavController):ViewHolder(view) {
    lateinit var date:MaterialTextView
    lateinit var count:MaterialTextView
    lateinit var value:MaterialTextView
    lateinit var credits:ImageView

    fun loadField(){
        date = view.findViewById(R.id.month_year_pcil)
        count = view.findViewById(R.id.num_credits_pcil)
        value = view.findViewById(R.id.value_pcil)
        credits = view.findViewById(R.id.btn_credits_pcil)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setField(values:PeriodCreditDTO){
        val month = view.resources.getStringArray(R.array.Months)
        date.text = "${month[values.date.monthValue]} ${values.date.year}"
        count.text = values.count.toString()
        value.text = NumbersUtil.COPtoString(values.value)
        credits.setOnClickListener{
            PeriodCreditListParams.newInstance(values.date, navController)
        }
    }
}