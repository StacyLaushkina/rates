package ru.laushkina.rates.ui

import android.util.Log
import android.util.Pair
import androidx.annotation.DrawableRes
import ru.laushkina.rates.R
import ru.laushkina.rates.model.RateShortName

class RateUIInfo {
    companion object {
        private const val TAG = "[RateFullName]"

        fun getUnknownImageId(): Int {
            return R.drawable.ic_rate_unknown_image
        }

        fun getFullNameAndImage(shortName: RateShortName): Pair<Int, Int> {
            return when (shortName) {
                RateShortName.AED -> Pair(R.string.rates_aud, getUnknownImageId())
                RateShortName.AFN -> Pair(R.string.rates_afn, getUnknownImageId())
                RateShortName.ALL -> Pair(R.string.rates_all, getUnknownImageId())
                RateShortName.AMD -> Pair(R.string.rates_amd, getUnknownImageId())
                RateShortName.ANG -> Pair(R.string.rates_ang, getUnknownImageId())
                RateShortName.AOA -> Pair(R.string.rates_aoa, getUnknownImageId())
                RateShortName.ARS -> Pair(R.string.rates_ars, getUnknownImageId())
                RateShortName.AUD -> Pair(R.string.rates_aud, R.drawable.ic_rate_aud_image)
                RateShortName.AWG -> Pair(R.string.rates_awg, getUnknownImageId())
                RateShortName.AZN -> Pair(R.string.rates_azn, getUnknownImageId())
                RateShortName.BAM -> Pair(R.string.rates_bam, getUnknownImageId())
                RateShortName.BBD -> Pair(R.string.rates_bbd, getUnknownImageId())
                RateShortName.BDT -> Pair(R.string.rates_bdt, getUnknownImageId())
                RateShortName.BGN -> Pair(R.string.rates_bgn, R.drawable.ic_rate_bgn_image)
                RateShortName.BHD -> Pair(R.string.rates_bhd, getUnknownImageId())
                RateShortName.BIF -> Pair(R.string.rates_bif, getUnknownImageId())
                RateShortName.BMD -> Pair(R.string.rates_bmd, getUnknownImageId())
                RateShortName.BND -> Pair(R.string.rates_bnd, getUnknownImageId())
                RateShortName.BOB -> Pair(R.string.rates_bob, getUnknownImageId())
                RateShortName.BRL -> Pair(R.string.rates_brl, R.drawable.ic_rate_brl_image)
                RateShortName.BSD -> Pair(R.string.rates_bsd, getUnknownImageId())
                RateShortName.BTC -> Pair(R.string.rates_btc, getUnknownImageId())
                RateShortName.BTN -> Pair(R.string.rates_btn, getUnknownImageId())
                RateShortName.BWP -> Pair(R.string.rates_bwp, getUnknownImageId())
                RateShortName.BYN -> Pair(R.string.rates_byn, getUnknownImageId())
                RateShortName.BYR -> Pair(R.string.rates_byr, getUnknownImageId())
                RateShortName.BZD -> Pair(R.string.rates_bzd, getUnknownImageId())
                RateShortName.CAD -> Pair(R.string.rates_cad, R.drawable.ic_rate_cad_image)
                RateShortName.CDF -> Pair(R.string.rates_cdf, getUnknownImageId())
                RateShortName.CHF -> Pair(R.string.rates_chf, getUnknownImageId())
                RateShortName.CLP -> Pair(R.string.rates_clp, getUnknownImageId())
                RateShortName.CLF -> Pair(R.string.rates_clf, R.drawable.ic_rate_chf_image)
                RateShortName.CNY -> Pair(R.string.rates_cny, R.drawable.ic_rate_chy_image)
                RateShortName.COP -> Pair(R.string.rates_cop, getUnknownImageId())
                RateShortName.CRC -> Pair(R.string.rates_crc, getUnknownImageId())
                RateShortName.CUC -> Pair(R.string.rates_cuc, getUnknownImageId())
                RateShortName.CUP -> Pair(R.string.rates_cup, getUnknownImageId())
                RateShortName.CVE -> Pair(R.string.rates_cve, getUnknownImageId())
                RateShortName.CZK -> Pair(R.string.rates_czk, R.drawable.ic_rate_czk_image)
                RateShortName.DJF -> Pair(R.string.rates_czk, getUnknownImageId())
                RateShortName.EUR -> Pair(R.string.rates_eur, R.drawable.ic_rate_eur_image)
                RateShortName.DKK -> Pair(R.string.rates_dkk, R.drawable.ic_rate_dkk_image)
                RateShortName.DOP -> Pair(R.string.rates_dop, getUnknownImageId())
                RateShortName.DZD -> Pair(R.string.rates_dzd, getUnknownImageId())
                RateShortName.EGP -> Pair(R.string.rates_egp, getUnknownImageId())
                RateShortName.ERN -> Pair(R.string.rates_ern, getUnknownImageId())
                RateShortName.ETB -> Pair(R.string.rates_etb, getUnknownImageId())
                RateShortName.FJD -> Pair(R.string.rates_fjd, getUnknownImageId())
                RateShortName.FKP -> Pair(R.string.rates_fkp, getUnknownImageId())
                RateShortName.GBP -> Pair(R.string.rates_gbp, R.drawable.ic_rate_gbp_image)
                RateShortName.GEL -> Pair(R.string.rates_gel, getUnknownImageId())
                RateShortName.GGP -> Pair(R.string.rates_ggp, getUnknownImageId())
                RateShortName.GIP -> Pair(R.string.rates_gip, getUnknownImageId())
                RateShortName.GMD -> Pair(R.string.rates_gmd, getUnknownImageId())
                RateShortName.GNF -> Pair(R.string.rates_gnf, getUnknownImageId())
                RateShortName.GTQ -> Pair(R.string.rates_gtq, getUnknownImageId())
                RateShortName.GYD -> Pair(R.string.rates_gyd, getUnknownImageId())
                RateShortName.GHS -> Pair(R.string.rates_ghs, getUnknownImageId())
                RateShortName.HKD -> Pair(R.string.rates_hkd, R.drawable.ic_rate_hkd_image)
                RateShortName.HRK -> Pair(R.string.rates_hrk, R.drawable.ic_rate_hrk_image)
                RateShortName.HUF -> Pair(R.string.rates_huf, R.drawable.ic_rate_huf_image)
                RateShortName.IDR -> Pair(R.string.rates_idr, R.drawable.ic_rate_idr_image)
                RateShortName.ILS -> Pair(R.string.rates_ils, R.drawable.ic_rate_ils_image)
                RateShortName.HNL -> Pair(R.string.rates_hnl, getUnknownImageId())
                RateShortName.HTG -> Pair(R.string.rates_htg, getUnknownImageId())
                RateShortName.IMP -> Pair(R.string.rates_imp, getUnknownImageId())
                RateShortName.IQD -> Pair(R.string.rates_iqd, getUnknownImageId())
                RateShortName.IRR -> Pair(R.string.rates_irr, getUnknownImageId())
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
                RateShortName.JEP -> Pair(R.string.rates_jep, getUnknownImageId())
                RateShortName.JMD -> Pair(R.string.rates_jmd, getUnknownImageId())
                RateShortName.JOD -> Pair(R.string.rates_jod, getUnknownImageId())
                RateShortName.KES -> Pair(R.string.rates_kes, getUnknownImageId())
                RateShortName.KGS -> Pair(R.string.rates_kgs, getUnknownImageId())
                RateShortName.KHR -> Pair(R.string.rates_khr, getUnknownImageId())
                RateShortName.KMF -> Pair(R.string.rates_kmf, getUnknownImageId())
                RateShortName.KPW -> Pair(R.string.rates_kpw, getUnknownImageId())
                RateShortName.KWD -> Pair(R.string.rates_kwd, getUnknownImageId())
                RateShortName.KYD -> Pair(R.string.rates_kyd, getUnknownImageId())
                RateShortName.KZT -> Pair(R.string.rates_kzt, getUnknownImageId())
                RateShortName.LAK -> Pair(R.string.rates_lak, getUnknownImageId())
                RateShortName.LBP -> Pair(R.string.rates_lbp, getUnknownImageId())
                RateShortName.LKR -> Pair(R.string.rates_lkr, getUnknownImageId())
                RateShortName.LRD -> Pair(R.string.rates_lrd, getUnknownImageId())
                RateShortName.LSL -> Pair(R.string.rates_lsl, getUnknownImageId())
                RateShortName.LTL -> Pair(R.string.rates_ltl, getUnknownImageId())
                RateShortName.LVL -> Pair(R.string.rates_lvl, getUnknownImageId())
                RateShortName.LYD -> Pair(R.string.rates_lyd, getUnknownImageId())
                RateShortName.MAD -> Pair(R.string.rates_mad, getUnknownImageId())
                RateShortName.MDL -> Pair(R.string.rates_mdl, getUnknownImageId())
                RateShortName.MGA -> Pair(R.string.rates_mga, getUnknownImageId())
                RateShortName.MKD -> Pair(R.string.rates_mkd, getUnknownImageId())
                RateShortName.MMK -> Pair(R.string.rates_mmk, getUnknownImageId())
                RateShortName.MNT -> Pair(R.string.rates_mnt, getUnknownImageId())
                RateShortName.MOP -> Pair(R.string.rates_mop, getUnknownImageId())
                RateShortName.MRO -> Pair(R.string.rates_mro, getUnknownImageId())
                RateShortName.MUR -> Pair(R.string.rates_mur, getUnknownImageId())
                RateShortName.MVR -> Pair(R.string.rates_mvr, getUnknownImageId())
                RateShortName.MWK -> Pair(R.string.rates_mwk, getUnknownImageId())
                RateShortName.MZN -> Pair(R.string.rates_mzn, getUnknownImageId())
                RateShortName.NAD -> Pair(R.string.rates_nad, getUnknownImageId())
                RateShortName.NGN -> Pair(R.string.rates_ngn, getUnknownImageId())
                RateShortName.NIO -> Pair(R.string.rates_nio, getUnknownImageId())
                RateShortName.NPR -> Pair(R.string.rates_npr, getUnknownImageId())
                RateShortName.OMR -> Pair(R.string.rates_omr, getUnknownImageId())
                RateShortName.PAB -> Pair(R.string.rates_pab, getUnknownImageId())
                RateShortName.PEN -> Pair(R.string.rates_pen, getUnknownImageId())
                RateShortName.PGK -> Pair(R.string.rates_pgk, getUnknownImageId())
                RateShortName.PKR -> Pair(R.string.rates_pkr, getUnknownImageId())
                RateShortName.PYG -> Pair(R.string.rates_pyg, getUnknownImageId())
                RateShortName.QAR -> Pair(R.string.rates_qar, getUnknownImageId())
                RateShortName.RSD -> Pair(R.string.rates_rsd, getUnknownImageId())
                RateShortName.RWF -> Pair(R.string.rates_rwf, getUnknownImageId())
                RateShortName.SAR -> Pair(R.string.rates_sar, getUnknownImageId())
                RateShortName.SBD -> Pair(R.string.rates_sbd, getUnknownImageId())
                RateShortName.SCR -> Pair(R.string.rates_scr, getUnknownImageId())
                RateShortName.SDG -> Pair(R.string.rates_sdg, getUnknownImageId())
                RateShortName.SHP -> Pair(R.string.rates_shp, getUnknownImageId())
                RateShortName.SLL -> Pair(R.string.rates_sll, getUnknownImageId())
                RateShortName.SOS -> Pair(R.string.rates_sos, getUnknownImageId())
                RateShortName.SRD -> Pair(R.string.rates_srd, getUnknownImageId())
                RateShortName.STD -> Pair(R.string.rates_std, getUnknownImageId())
                RateShortName.SVC -> Pair(R.string.rates_svc, getUnknownImageId())
                RateShortName.SYP -> Pair(R.string.rates_syp, getUnknownImageId())
                RateShortName.SZL -> Pair(R.string.rates_szl, getUnknownImageId())
                RateShortName.TJS -> Pair(R.string.rates_tjs, getUnknownImageId())
                RateShortName.TMT -> Pair(R.string.rates_tmt, getUnknownImageId())
                RateShortName.TND -> Pair(R.string.rates_tnd, getUnknownImageId())
                RateShortName.TOP -> Pair(R.string.rates_top, getUnknownImageId())
                RateShortName.TRY -> Pair(R.string.rates_try, getUnknownImageId())
                RateShortName.TTD -> Pair(R.string.rates_ttd, getUnknownImageId())
                RateShortName.TWD -> Pair(R.string.rates_twd, getUnknownImageId())
                RateShortName.TZS -> Pair(R.string.rates_tzs, getUnknownImageId())
                RateShortName.UAH -> Pair(R.string.rates_uah, getUnknownImageId())
                RateShortName.UGX -> Pair(R.string.rates_ugx, getUnknownImageId())
                RateShortName.UYU -> Pair(R.string.rates_uyu, getUnknownImageId())
                RateShortName.UZS -> Pair(R.string.rates_uzs, getUnknownImageId())
                RateShortName.VEF -> Pair(R.string.rates_vef, getUnknownImageId())
                RateShortName.VND -> Pair(R.string.rates_vnd, getUnknownImageId())
                RateShortName.VUV -> Pair(R.string.rates_vuv, getUnknownImageId())
                RateShortName.WST -> Pair(R.string.rates_wst, getUnknownImageId())
                RateShortName.XAF -> Pair(R.string.rates_xaf, getUnknownImageId())
                RateShortName.XAG -> Pair(R.string.rates_xag, getUnknownImageId())
                RateShortName.XAU -> Pair(R.string.rates_xau, getUnknownImageId())
                RateShortName.XCD -> Pair(R.string.rates_xcd, getUnknownImageId())
                RateShortName.XDR -> Pair(R.string.rates_xdr, getUnknownImageId())
                RateShortName.XOF -> Pair(R.string.rates_xof, getUnknownImageId())
                RateShortName.XPF -> Pair(R.string.rates_xpf, getUnknownImageId())
                RateShortName.YER -> Pair(R.string.rates_yer, getUnknownImageId())
                RateShortName.ZMK -> Pair(R.string.rates_zmk, getUnknownImageId())
                RateShortName.ZMW -> Pair(R.string.rates_zmw, getUnknownImageId())
                RateShortName.ZWL -> Pair(R.string.rates_zml, getUnknownImageId())
                else -> {
                    Log.e(TAG, "Could not find name and image for:$shortName")
                    Pair(R.string.rates_unknown, getUnknownImageId())
                }
            }
        }
    }
}