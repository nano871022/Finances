package co.japl.android.myapplication.finanzas.holders

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.interfaces.IRecapHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselStrategy

class RecapHolder(val view:View):IRecapHolder<RecapHolder> {
    lateinit var inputs:TextView
    lateinit var saved:TextView
    lateinit var quoteSaved:TextView
    lateinit var totalPaid:TextView
    lateinit var totalCredits:TextView
    lateinit var totalFix:TextView
    lateinit var totalInputFix:TextView
    lateinit var quoteTC:TextView
    lateinit var totalPaids:TextView
    lateinit var totalInputsPaids:TextView
    lateinit var warning: TextView
    lateinit var limit:TextView
    lateinit var scrollView: HorizontalScrollView
    var graph:CustomDraw? = null

    override fun setFields(actions: View.OnClickListener?) {
        inputs = view.findViewById(R.id.tv_total_inputs_rec)
        saved = view.findViewById(R.id.tv_saved_rec)
        quoteSaved = view.findViewById(R.id.tv_quote_save_rec)
        totalPaid = view.findViewById(R.id.tv_total_paid_rec)
        totalCredits = view.findViewById(R.id.tv_total_credits_rec)
        totalFix = view.findViewById(R.id.tv_total_paids_credits_rec)
        totalInputFix = view.findViewById(R.id.tv_total_inputs_fix)
        quoteTC = view.findViewById(R.id.tv_total_quote_credit_card_rec)
        totalPaids = view.findViewById(R.id.tv_total_paids_rec)
        totalInputsPaids = view.findViewById(R.id.tv_total_input_paids_rec)
        warning= view.findViewById(R.id.tv_warning_credit_card_rec)
        limit= view.findViewById(R.id.tv_limit_credit_card_rec)
        scrollView = view.findViewById(R.id.scroll_rec)
        graph = view.findViewById(R.id.cv_canvas_rec)

    }

    override fun loadFields(fn: ((RecapHolder) -> Unit)?) {
        fn?.let {
            it.invoke(this)
        }
    }

}