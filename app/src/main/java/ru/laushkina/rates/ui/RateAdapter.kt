package ru.laushkina.rates.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.laushkina.rates.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class RateAdapter(@VisibleForTesting val rates: MutableList<RateViewModel>,
                       private val valueChangeListeners: ValueChangeListener,
                       private val clickListener: RateClickListener) : RecyclerView.Adapter<RateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.rate_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rate = rates[position]

        holder.rateImageView.setImageDrawable(holder.context.resources.getDrawable(rate.imageId))
        holder.rateShortText.text = rate.shortName
        holder.rateText.setText(rate.name)

        setValue(holder, rate.amount, rate.showAmount)

        val intTextWatcher: TextWatcher = FirstValueTextWatcher(valueChangeListeners, rate)
        holder.valueIntEditView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.valueIntEditView.addTextChangedListener(intTextWatcher)
            } else {
                holder.valueIntEditView.removeTextChangedListener(intTextWatcher)
            }
        }

        holder.valueIntEditView.setSelection(holder.valueIntEditView.length())

        holder.container.setOnClickListener { onRateClicked(position, rate) }
    }

    @VisibleForTesting
    fun setValue(holder: ViewHolder, amount: Float?, showValue: Boolean) {
        if (amount == null || !showValue) {
            holder.valueIntEditView.setText("")
            holder.valueFractionTextView.text = ""
            return
        }

        val long = amount.toLong()
        holder.valueIntEditView.setText(String.format(Locale.US, "%d", long))

        val fraction = ((amount - long) * 100).roundToLong()
        holder.valueFractionTextView.text = String.format(Locale.US, "%d", fraction)
    }

    fun updateRates(rates: List<RateViewModel>) {
        this.rates.clear()
        this.rates.addAll(rates)
        notifyDataSetChanged()
    }

    @VisibleForTesting
    fun onRateClicked(position: Int, rate: RateViewModel) {
        synchronized(this) {
            rates.removeAt(position)
            rates.add(0, rate)
        }
        notifyItemMovedToTheTop(position)
        clickListener.onClicked(position, rate)
    }

    @VisibleForTesting
    fun notifyItemMovedToTheTop(position: Int) {
        notifyItemMoved(position, 0)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val context: Context = itemView.context
        internal val rateImageView: ImageView = itemView.findViewById(R.id.rate_image)
        internal val rateShortText: TextView = itemView.findViewById(R.id.rate_short_text)
        internal val rateText: TextView = itemView.findViewById(R.id.rate_text)
        internal val valueIntEditView: EditText = itemView.findViewById(R.id.rate_value_int)
        internal val valueFractionTextView: TextView = itemView.findViewById(R.id.rate_value_fraction)
        internal val container: View = itemView
    }

    internal class FirstValueTextWatcher(private val listener: ValueChangeListener,
                                         private val viewModel: RateViewModel) : TextWatcher {
        override fun afterTextChanged(s: Editable) { }

        override fun beforeTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence, start: Int,
                                   before: Int, count: Int) {
            val value = s.toString()
            if (viewModel.amount.toString() != value) {
                listener.onValueChange(viewModel, value)
            }
        }
    }

    interface ValueChangeListener {
        fun onValueChange(rate: RateViewModel, value: String)
    }

    interface RateClickListener {
        fun onClicked(position: Int, rate: RateViewModel)
    }
}