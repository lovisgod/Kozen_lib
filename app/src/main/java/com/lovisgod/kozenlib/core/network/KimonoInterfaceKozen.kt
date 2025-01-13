package com.lovisgod.kozenlib.core.network

import com.lovisgod.kozenlib.core.data.utilsData.Constants
import com.lovisgod.kozenlib.core.network.models.*
import com.lovisgod.kozenlib.core.utilities.simplecalladapter.Simple
import retrofit2.http.*

interface KimonoInterfaceKozen {

    @POST(Constants.ISW_TOKEN_URL)
    fun getISWToken( @Body request: TokenRequestModelKozen):
            Simple<TokenConfigResponseKozen>

    @Headers("Accept: application/xml")
    @POST(Constants.KIMONO_END_POINT)
    fun makePurchase( @Body request: PurchaseRequestKozenKozen):
            Simple<PurchaseResponseKozen>

    @Headers("Accept: application/xml")
    @POST(Constants.KIMONO_END_POINT)
    fun makeCashout(@Body request: TransferRequestKozenKozen, @Header("Authorization") token: String ):
            Simple<PurchaseResponseKozen>
}