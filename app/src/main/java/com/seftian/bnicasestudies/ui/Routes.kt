package com.seftian.bnicasestudies.ui

import com.seftian.bnicasestudies.core.domain.model.PromoItem

sealed class Routes(val route: String) {
    object Home: Routes("home")
    object PaymentDetail: Routes("payment_detail/{idQr}"){
        fun withIdQr(idQr: String) = "payment_detail/${idQr}"
    }
    object QrScreen: Routes("qr_screen")

    object DetailPromo: Routes("detail_promo/{idPromo}"){
        fun withData(idPromo: Int) = "detail_promo/${idPromo}"
    }

    object Portfolio: Routes("portfolio")
}