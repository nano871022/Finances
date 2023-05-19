package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.adapter.ListPaidAdapter
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecyclerView
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.textview.MaterialTextView
import java.time.format.TextStyle
import java.util.*

class PaidListHolder(val view:View): IHolder<PaidDTO>, IRecyclerView<PaidDTO> {

    private lateinit var recycler: RecyclerView
    private lateinit var value: MaterialTextView
    private lateinit var period: MaterialTextView

    override fun setFields(actions: View.OnClickListener?) {
        recycler = view.findViewById(R.id.rv_paid_list)
        value = view.findViewById(R.id.tv_value_pdl)
        period = view.findViewById(R.id.tv_period_pdl)
        recycler.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(values: PaidDTO) {
        value.text = NumbersUtil.COPtoString(values.value)
        period.text = "${values.date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${values.date.year}"
    }

    override fun downLoadFields(): PaidDTO {
        TODO("Not yet implemented")
    }

    override fun cleanField() {
        TODO("Not yet implemented")
    }

    override fun validate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun loadRecycler(data: MutableList<PaidDTO>) {
        ListPaidAdapter(data)?.let{
            recycler.adapter = it
        }
    }
}