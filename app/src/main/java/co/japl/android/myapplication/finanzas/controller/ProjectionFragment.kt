package co.japl.android.myapplication.finanzas.controller

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.ProjectionsImpl
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.holders.ProjectionHolder
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.putParams.ProjectionsParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectionFragment : Fragment() , OnClickListener{
    private lateinit var holder:IHolder<ProjectionDTO>

    @Inject lateinit var svc:IProjectionsSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_projection, container, false)
        activity?.let {
            holder = ProjectionHolder(root, it.supportFragmentManager)
            holder.setFields(this)
            holder.cleanField()
            loadData()
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadData(){
        arguments?.let {
            val id = ProjectionsParams.download(it)
            if(id > 0){
                svc.get(id.toInt()).ifPresent {
                    holder.loadFields(it)
                }
            }
        }
    }

    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val response = svc.save(dto)
            if(dto.id == 0 && response > 0){
                dto.id = response.toInt()
                Toast.makeText(context,R.string.toast_successful_insert,Toast.LENGTH_LONG).show()
                ProjectionsParams.toBack(findNavController())
            }else if(dto.id > 0 && response > 0){
                Toast.makeText(context,R.string.toast_successful_update,Toast.LENGTH_LONG).show()
                ProjectionsParams.toBack(findNavController())
            }else{
                Toast.makeText(context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_cancel_prj->ProjectionsParams.toBack(findNavController())
            R.id.btn_save_prj->save()
        }
    }

}