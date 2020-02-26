package ru.laushkina.rates.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import ru.laushkina.rates.R;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {
    @NonNull
    @VisibleForTesting
    List<RateViewModel> rates;
    @NonNull
    private final Subject<RateViewModel> onClickSubject = PublishSubject.create();
    @NonNull
    private final ValueChangeListener listener;

    RateAdapter(@NonNull List<RateViewModel> rates, @NonNull ValueChangeListener listener) {
        this.rates = rates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rate_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        RateViewModel rate = rates.get(position);

        holder.rateImageView.setImageDrawable(holder.context.getResources().getDrawable(rate.getImageId()));
        holder.rateShortText.setText(rate.getShortName());
        holder.rateText.setText(rate.getName());

        String amount = formatAmount(rate.getAmount(), rate.showAmount());
        holder.valueEditView.setText(amount);

        TextWatcher textWatcher = new FirstValueTextWatcher(listener, rate);
        holder.valueEditView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                holder.valueEditView.addTextChangedListener(textWatcher);
            } else {
                holder.valueEditView.removeTextChangedListener(textWatcher);
            }
        });
        holder.valueEditView.setSelection(holder.valueEditView.length());

        holder.container.setOnClickListener(view -> onRateClicked(position, rate));
    }

    @VisibleForTesting
    void onRateClicked(int position, @NonNull RateViewModel rate) {
        onClickSubject.onNext(rate);
        rates.remove(position);
        rates.add(0, rate);
        notifyItemMovedToTheTop(position);
    }

    void updateRates(@NonNull List<RateViewModel> rates) {
        this.rates = rates;
        notifyDataSetChanged();
    }

    @NonNull
    Subject<RateViewModel> getPositionClicks() {
        return onClickSubject;
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    @NonNull
    @VisibleForTesting
    static String formatAmount(@Nullable Float amount, boolean showValue) {
        if (amount == null || !showValue) {
            return "";
        }

        float floatValue = amount;
        if (amount == (long) floatValue)
            return String.format(Locale.US, "%d", (long) floatValue);
        else
            return String.format(Locale.US, "%.2f", amount);
    }

    @VisibleForTesting
    void notifyItemMovedToTheTop(int position) {
        notifyItemMoved(position, 0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final Context context;
        @NonNull
        private final ImageView rateImageView;
        @NonNull
        private final TextView rateShortText;
        @NonNull
        private final TextView rateText;
        @NonNull
        private final EditText valueEditView;
        @NonNull
        private final View container;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            rateImageView = itemView.findViewById(R.id.rate_image);
            rateShortText = itemView.findViewById(R.id.rate_short_text);
            rateText = itemView.findViewById(R.id.rate_text);
            valueEditView = itemView.findViewById(R.id.rate_value);
            container = itemView;
        }
    }

    static class FirstValueTextWatcher implements TextWatcher {
        @NonNull
        private final ValueChangeListener listener;
        @NonNull
        private final RateViewModel rateViewModel;

        FirstValueTextWatcher(@NonNull ValueChangeListener listener,
                              @NonNull RateViewModel viewModel) {
            this.listener = listener;
            this.rateViewModel = viewModel;
        }

        @Override
        public void afterTextChanged(Editable s) {
            listener.afterValueChange(rateViewModel);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
            listener.beforeValueChange(rateViewModel);
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            String value = s.toString();
            if (!String.valueOf(rateViewModel.getAmount()).equals(value)) {
                listener.onValueChange(rateViewModel, value);
            }
        }
    }

    interface ValueChangeListener {
        void beforeValueChange(@NonNull RateViewModel rate);

        void afterValueChange(@NonNull RateViewModel rate);

        void onValueChange(@NonNull RateViewModel rate, @NonNull String value);
    }
}
