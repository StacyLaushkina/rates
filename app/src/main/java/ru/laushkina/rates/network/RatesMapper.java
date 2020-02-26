package ru.laushkina.rates.network;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.laushkina.rates.model.Rate;

public class RatesMapper {

    @Nullable
    public static List<Rate> mapToRates(@Nullable RatesResponse response) {
        if (response == null || response.Rates == null || response.Source == null) {
            return null;
        }

        List<Rate> result = new ArrayList<>(response.Rates.size() + 1);
        result.add(new Rate(response.Source, 1f, true));
        for (String key : response.Rates.keySet()) {
            result.add(new Rate(key, response.Rates.get(key), false));
        }

        return result;
    }
}
