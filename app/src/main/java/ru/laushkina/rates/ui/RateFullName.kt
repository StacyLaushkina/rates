package ru.laushkina.rates.ui

import android.util.Log
import android.util.Pair
import ru.laushkina.rates.R
import ru.laushkina.rates.model.RateShortName

class RateFullName {
    companion object {
        private const val TAG = "[RateFullName]"

        fun getFullName(shortName: RateShortName): Pair<Int, Int> {
            return when (shortName) {
                RateShortName.AUD -> Pair(R.string.rates_aud, R.drawable.ic_rate_aud_image)
                RateShortName.BGN -> Pair(R.string.rates_bgn, R.drawable.ic_rate_bgn_image)
                RateShortName.BRL -> Pair(R.string.rates_brl, R.drawable.ic_rate_brl_image)
                RateShortName.CAD -> Pair(R.string.rates_cad, R.drawable.ic_rate_cad_image)
                RateShortName.CHF -> Pair(R.string.rates_chf, R.drawable.ic_rate_chf_image)
                RateShortName.CNY -> Pair(R.string.rates_cny, R.drawable.ic_rate_chy_image)
                RateShortName.CZK -> Pair(R.string.rates_czk, R.drawable.ic_rate_czk_image)
                RateShortName.EUR -> Pair(R.string.rates_eur, R.drawable.ic_rate_eur_image)
                RateShortName.DKK -> Pair(R.string.rates_dkk, R.drawable.ic_rate_dkk_image)
                RateShortName.GBP -> Pair(R.string.rates_gbp, R.drawable.ic_rate_gbp_image)
                RateShortName.HKD -> Pair(R.string.rates_hkd, R.drawable.ic_rate_hkd_image)
                RateShortName.HRK -> Pair(R.string.rates_hrk, R.drawable.ic_rate_hrk_image)
                RateShortName.HUF -> Pair(R.string.rates_huf, R.drawable.ic_rate_huf_image)
                RateShortName.IDR -> Pair(R.string.rates_idr, R.drawable.ic_rate_idr_image)
                RateShortName.ILS -> Pair(R.string.rates_ils, R.drawable.ic_rate_ils_image)
                RateShortName.INR -> Pair(R.string.rates_inr, R.drawable.ic_rate_inr_image)
                RateShortName.ISK -> Pair(R.string.rates_isk, R.drawable.ic_rate_isk_image)
                RateShortName.JPY -> Pair(R.string.rates_jpy, R.drawable.ic_rate_jpy_image)
                RateShortName.KRW -> Pair(R.string.rates_krw, R.drawable.ic_rate_krw_image)
                RateShortName.MXN -> Pair(R.string.rates_mxn, R.drawable.ic_rate_mxn_image)
                RateShortName.MYR -> Pair(R.string.rates_myr, R.drawable.ic_rate_myr_image)
                RateShortName.NOK -> Pair(R.string.rates_nok, R.drawable.ic_rate_nok_image)
                RateShortName.NZD -> Pair(R.string.rates_nzd, R.drawable.ic_rate_nzd_image)
                RateShortName.PHP -> Pair(R.string.rates_php, R.drawable.ic_rate_php_image)
                RateShortName.PLN -> Pair(R.string.rates_pln, R.drawable.ic_rate_pln_image)
                RateShortName.RON -> Pair(R.string.rates_ron, R.drawable.ic_rate_ron_image)
                RateShortName.RUB -> Pair(R.string.rates_rub, R.drawable.ic_rate_rub_image)
                RateShortName.SEK -> Pair(R.string.rates_sek, R.drawable.ic_rate_sek_image)
                RateShortName.SGD -> Pair(R.string.rates_sgd, R.drawable.ic_rate_sgd_image)
                RateShortName.THB -> Pair(R.string.rates_thb, R.drawable.ic_rate_thb_image)
                RateShortName.USD -> Pair(R.string.rates_usd, R.drawable.ic_rate_us_image)
                RateShortName.ZAR -> Pair(R.string.rates_zar, R.drawable.ic_rate_zar_image)
                else -> {
                    Log.e(TAG, "Could not find name and image for:$shortName")
                    Pair(R.string.rates_unknown, R.drawable.ic_rate_unknown_image)
                }
            }
        }
    }
}