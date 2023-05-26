package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.enums.KindofProjectionEnum
import co.japl.android.myapplication.finanzas.holders.ListProjectionHolder
import co.japl.android.myapplication.finanzas.holders.ProjectionsHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IListHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import co.japl.android.myapplication.utils.NumbersUtil
import java.time.LocalDate
import java.time.Period

class ListProjectFragment : Fragment(){
    private lateinit var holder: IListHolder<ListProjectionHolder,ProjectionDTO>
    private lateinit var svc:IProjectionsSvc
    private lateinit var kindOfList:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_projection, container, false)
        svc = ProjectionsImpl(ConnectDB(root.context),root)
        kindOfList = root.resources.getStringArray(R.array.kind_of_projection_list)
        val data = svc.getAllActive().toMutableList()
            holder = ListProjectionHolder(root,findNavController())
            holder.setFields(null)
            holder.loadFields {
                val tot = data.sumOf {
                    val index = kindOfList.indexOf(it.type)
                    val months = KindofProjectionEnum.values()[index].months
                    it.quote * (months - Period.between(LocalDate.now(), it.end)
                        .toTotalMonths()).toBigDecimal()
                }
                it.items.text = data.count().toString()
                it.total.text = NumbersUtil.toString(tot)
            }
            holder.loadRecycler(data)
        return root
    }
}