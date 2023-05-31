package co.japl.android.myapplication.finanzas.holders

import android.os.Build
import android.view.View
import android.widget.ProgressBar
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
     lateinit var items:TextView
     lateinit var total:TextView
     lateinit var dtClose:TextView
     lateinit var monthClose:TextView
     lateinit var valueClose:TextView
     lateinit var dtFar:TextView
     lateinit var monthFar:TextView
     lateinit var valueFar:TextView
    private lateinit var btnDetail:MaterialButton
    private lateinit var btnAdd:MaterialButton
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.pb_load_pjs)
        btnDetail.setOnClickListener(actions)
        btnAdd.setOnClickListener(actions)
        progressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadFields(fn: ((ProjectionsHolder) -> Unit)?) {
        fn?.invoke(this)
        progressBar.visibility = View.GONE
    }

}