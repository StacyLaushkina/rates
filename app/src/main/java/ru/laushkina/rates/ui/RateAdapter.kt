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

open class RateAdapter(@VisibleForTesting val rates: MutableList<RateViewModel>,
                  private val listener: ValueChangeListener): RecyclerView.Adapter<RateAdapter.ViewHolder>() {
    private val onClickSubject: Subject<RateViewModel> = PublishSubject.create()

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

        val amount = formatAmount(rate.amount, rate.showAmount)
        holder.valueEditView.setText(amount)

        val textWatcher: TextWatcher = FirstValueTextWatcher(listener, rate)
        holder.valueEditView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.valueEditView.addTextChangedListener(textWatcher)
            } else {
                holder.valueEditView.removeTextChangedListener(textWatcher)
            }
        }
        holder.valueEditView.setSelection(holder.valueEditView.length())

        holder.container.setOnClickListener { onRateClicked(position, rate) }
    }

    companion object {
        @VisibleForTesting
        fun formatAmount(amount: Float?, showValue: Boolean): String {
            if (amount == null || !showValue) {
                return ""
            }

            val longAmount = amount.toLong()
            return if (amount == longAmount.toFloat()) String.format(Locale.US, "%d", longAmount)
            else String.format(Locale.US, "%.2f", amount)
        }
    }

    fun updateRates(rates: List<RateViewModel>) {

        this.rates.clear()
        this.rates.addAll(rates)
        notifyDataSetChanged()
    }

    @VisibleForTesting
    fun onRateClicked(position: Int, rate: RateViewModel) {
        onClickSubject.onNext(rate)
        synchronized(this) {
            rates.removeAt(position)
            rates.add(0, rate)
        }
        notifyItemMovedToTheTop(position)
    }

    @VisibleForTesting
    fun notifyItemMovedToTheTop(position: Int) {
        notifyItemMoved(position, 0)
    }

    fun getPositionClicks(): Subject<RateViewModel> {
        return onClickSubject
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val context: Context = itemView.context
        internal val rateImageView: ImageView = itemView.findViewById(R.id.rate_image)
        internal val rateShortText: TextView = itemView.findViewById(R.id.rate_short_text)
        internal val rateText: TextView = itemView.findViewById(R.id.rate_text)
        internal val valueEditView: EditText = itemView.findViewById(R.id.rate_value)
        internal val container: View = itemView
    }

    internal class FirstValueTextWatcher(private val listener: ValueChangeListener,
                                         private val viewModel: RateViewModel) : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            listener.afterValueChange(viewModel)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int,
                                       count: Int, after: Int) {
            listener.beforeValueChange(viewModel)
        }

        override fun onTextChanged(s: CharSequence, start: Int,
                                   before: Int, count: Int) {
            val value = s.toString()
            if (viewModel.amount.toString() != value) {
                listener.onValueChange(viewModel, value)
            }
        }
    }

    interface ValueChangeListener {
        fun beforeValueChange(rate: RateViewModel)
        fun afterValueChange(rate: RateViewModel)
        fun onValueChange(rate: RateViewModel, value: String)
    }
}