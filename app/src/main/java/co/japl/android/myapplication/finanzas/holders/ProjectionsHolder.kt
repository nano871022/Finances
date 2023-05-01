package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionsHolder(val view:View) : IRecapHolder<ProjectionsHolder> {
    private val svc:IProjectionsSvc = ProjectionsImpl(ConnectDB(view.context),view)
    private lateinit var items:TextView
    private lateinit var total:TextView
    private lateinit var dtClose:TextView
    private lateinit var monthClose:TextView
    private lateinit var valueClose:TextView
    private lateinit var dtFar:TextView
    private lateinit var monthFar:TextView
    private lateinit var valueFar:TextView
    private lateinit var btnDetail:MaterialButton
    private lateinit var btnAdd:MaterialButton

    override fun setFields(actions: View.OnClickListener?) {
        items = view.findViewById(R.id.tv_num_items_pjs)
        total = view.findViewById(R.id.tv_tot_save_pjs)
        dtClose = view.findViewById(R.id.tv_dt_close_pjs)
        monthClose = view.findViewById(R.id.tv_month_close_pjs)
        valueClose = view.findViewById(R.id.tv_save_close_pjs)
        dtFar = view.findViewById(R.id.tv_dt_far_pjs)
        monthFar = view.findViewById(R.id.tv_month_far_pjs)
        valueFar = view.findViewById(R.id.tv_save_far_pjs)
        btnDetail = view.findViewById(R.id.btn_detail_pjs)
        btnAdd = view.findViewById(R.id.btn_add_pjs)
        btnDetail.setOnClickListener(actions)
        btnAdd.setOnClickListener(actions)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(fn: ((ProjectionsHolder) -> Unit)?) {
        val close = svc.getClose()
        val far = svc.getFar()
        val tot = svc.getTotal()

        items.text = tot.first.toString()
        total.text = NumbersUtil.COPtoString(tot.second)
        dtClose.text = DateUtils.localDateToString(close.first)
        monthClose.text = close.second.toString()
        valueClose.text = NumbersUtil.COPtoString(close.third)
        dtFar.text = DateUtils.localDateToString(far.first)
        monthFar.text = far.second.toString()
        valueFar.text = NumbersUtil.COPtoString(far.third)
    }

}