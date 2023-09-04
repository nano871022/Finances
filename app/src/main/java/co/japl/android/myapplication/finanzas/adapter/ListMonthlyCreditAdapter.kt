package co.japl.android.myapplication.finanzas.adapter
import co.japl.android.myapplication.finanzas.holders.validations.COPtoBigDecimal
import android.app.AlertDialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.GracePeriodDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.GracePeriodDTO
import co.japl.android.myapplication.finanzas.bussiness.impl.AdditionalCreditImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.CreditFixImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GracePeriodImpl
import co.japl.android.myapplication.finanzas.enums.MoreOptionalItemsCredit
import co.japl.android.myapplication.finanzas.holders.validations.COPToBigDecimal
import co.japl.android.myapplication.finanzas.holders.view.MonthlyCreditItemHolder
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.finanzas.putParams.CreditFixListParams
import co.japl.android.myapplication.finanzas.putParams.CreditFixParams
import co.japl.android.myapplication.utils.DateUtils
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.time.LocalDate

class ListMonthlyCreditAdapter(val data:MutableList<CreditDTO>,val view:View,val inflater: LayoutInflater,val navController: NavController): RecyclerView.Adapter<MonthlyCreditItemHolder>() {
    private lateinit var creditFixSvc:SaveSvc<CreditDTO>
    private lateinit var additionalSvc:SaveSvc<AdditionalCreditDTO>
    private lateinit var gracePeriodSvc:SaveSvc<GracePeriodDTO>
    private lateinit var holder: MonthlyCreditItemHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyCreditItemHolder {
        creditFixSvc = CreditFixImpl(ConnectDB(view.context))
        additionalSvc = AdditionalCreditImpl(ConnectDB(view.context))
        gracePeriodSvc = GracePeriodImpl(ConnectDB(view.context))
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_monthly_credit_item_list,parent,false)
        holder = MonthlyCreditItemHolder(view)
        holder.loadField()
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MonthlyCreditItemHolder, position: Int) {
        holder.setField(data[position]) {
            when (it) {
                MoreOptionalItemsCredit.ADDITIONAL->additional(position)
                MoreOptionalItemsCredit.DELETE -> delete(position)
                MoreOptionalItemsCredit.AMORTIZATION -> amortization(position)
                MoreOptionalItemsCredit.GRACE_PERIOD -> gracePeriod(position)
                MoreOptionalItemsCredit.DELETE_GRACE_PERIOD -> deleteGracePeriod(position)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun deleteGracePeriod(position: Int){
        val dialog = AlertDialog
            .Builder(view.context)
            .setTitle(R.string.do_you_want_to_delete_this_record)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                val gracePeriod = gracePeriodSvc.get(data[position].id)
                if(gracePeriod.isPresent) {
                    if(gracePeriodSvc.delete(gracePeriod.get().id)) {
                        Snackbar.make(
                            view,
                            R.string.delete_grace_period_saved,
                            Snackbar.LENGTH_SHORT
                        ).show()
                            .also {
                                dialog.dismiss()
                                CreditFixParams.toBack(navController)
                            }
                    }else{
                        Snackbar.make(view, R.string.delete_grace_period_unsave, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }else {
                    Snackbar.make(view, R.string.delete_grace_period_unsave, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun gracePeriod(position:Int){
        val inflater = inflater.inflate(R.layout.dialog_grace_period,null)
        val period = inflater.findViewById<EditText>(R.id.ed_period_dgp)
        val start = inflater.findViewById<EditText>(R.id.tv_start_dgp)
        val end = inflater.findViewById<EditText>(R.id.tv_end_dgp)
        val option = inflater.findViewById<RadioGroup>(R.id.rg_option_dgp)
        option.setOnCheckedChangeListener { radioGroup, checked ->
            val checkOption = radioGroup.findViewById<RadioButton>(checked)
            if(checkOption.isChecked){
                when(checked){
                    R.id.rb_previous_dgp->{
                        start.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).minusMonths(1)))
                        period.COPToBigDecimal() ?.toLong()?.let {
                            end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(it-1).minusDays(1)))
                        }}
                    R.id.rb_current_dgp->{
                            start.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1)))
                        period.text.toString()?.toLong()?.let {
                            end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(it).minusDays(1)))
                        }
                                }
                    R.id.rb_next_dgp-> {
                            start.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(1)))
                            period.text.toString()?.toLong()?.let {
                                end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(1 + it).minusDays(1)))
                            }
                        }
                }
            }
        }
        start.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1)))
        val handler = Handler(Looper.getMainLooper())
        period.addTextChangedListener {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                val periods:Long = period.text.toString()?.toLong() ?: 0
                option.findViewById<RadioButton>(option.checkedRadioButtonId)?.let{
                    when(it.id){
                        R.id.rb_previous_dgp->{
                            end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(periods-1).minusDays(1)))
                        }
                        R.id.rb_current_dgp->{
                            end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(periods).minusDays(1)))
                        }
                        R.id.rb_next_dgp->{
                            end.setText(DateUtils.localDateToString(LocalDate.now().withDayOfMonth(1).plusMonths(1+periods).minusDays(1)))
                        }
                    }
                }
            },1000)
        }

        val dialog = AlertDialog.Builder(view.context)
            .setTitle(R.string.grace_period)
            .setView(inflater)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.save, null)
            .create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
            val period = period.text.toString()?.toShort()?:0
            val end = DateUtils.toLocalDate(end.text.toString())
            val create = DateUtils.toLocalDate(start.text.toString())
            val codeCredit = data[position].id.toLong()?:0
            val dto = GracePeriodDTO(0,create,end,codeCredit,period)
            if(gracePeriodSvc.save(dto)>0) {
                Snackbar.make(view, R.string.grace_period_saved, Snackbar.LENGTH_SHORT).show()
                    .also {
                        dialog.dismiss()
                        CreditFixParams.toBack(navController)
                    }
            }else{
                Snackbar.make(view,R.string.grace_period_unsave,Snackbar.LENGTH_SHORT).show()
                    .also{

                    }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun amortization(position:Int){
        CreditFixParams.newInstanceAmortizationMonthlyList(data[position],LocalDate.now(),view.findNavController())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun delete(position:Int){
        val dialog = AlertDialog
            .Builder(view.context)
            .setTitle(R.string.do_you_want_to_delete_this_record)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete, null)
            .create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                (additionalSvc as AdditionalCreditImpl).get(getAdditionalDTO(data[position].id))?.let{
                    if(it.isNotEmpty()){
                        it.forEach {  additionalSvc.delete(it.id) }
                    }
                }
                if (creditFixSvc.delete(data[position].id)) {
                    dialog.dismiss()
                    Snackbar.make(
                        view,
                        R.string.delete_successfull,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.close) {}
                        .show().also {
                            data.removeAt(position)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()
                        }
                } else {
                    Snackbar.make(view, R.string.dont_deleted, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun additional(position:Int){
        CreditFixListParams.newInstanceAdditionalList(data[position].id.toLong(),navController)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAdditionalDTO(creditCode:Int):AdditionalCreditDTO{
        val id = 0
        val name = ""
        val value = BigDecimal.ZERO
        val startDate = LocalDate.MIN
        val endDate = LocalDate.MAX
        return AdditionalCreditDTO(id,name,value,creditCode.toLong(),startDate,endDate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}