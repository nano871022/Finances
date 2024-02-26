package co.japl.android.myapplication.finanzas.controller.paids

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IHolder
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IPaidSvc
import co.japl.android.myapplication.finanzas.holders.PaidHolder
import co.japl.android.myapplication.finanzas.putParams.PaidsParams
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaidFragment : Fragment() , OnClickListener{
    private lateinit var holder: IHolder<PaidDTO>

    @Inject lateinit var service:IPaidSvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_paid, container, false)
        holder = PaidHolder(root,parentFragmentManager)
        holder.setFields(this)
        holder.cleanField()
        load()
        return root
    }

    private fun load(){
        arguments?.let {
            PaidsParams.download(it)?.let {
                holder.loadFields(it)
            }
        }
    }

    private fun save(){
        if(holder.validate()){
            val dto = holder.downLoadFields()
            val response = service.save(dto)
            if(response > 0 && dto.id > 0){
                Toast.makeText( context,R.string.toast_successful_update, Toast.LENGTH_SHORT).show()
                PaidsParams.toBack(findNavController())
            }else if(response > 0 && dto.id == 0){
                Toast.makeText(context,R.string.toast_successful_insert,Toast.LENGTH_SHORT).show()
                PaidsParams.toBack(findNavController())
            }else{
                Toast.makeText(context,R.string.toast_unsuccessful_insert,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_cancel_p->PaidsParams.toBack(findNavController())
            R.id.btn_save_p->save()
        }
    }
}