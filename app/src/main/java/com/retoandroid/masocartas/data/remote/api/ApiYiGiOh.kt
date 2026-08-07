package com.retoandroid.masocartas.data.remote.api

import com.retoandroid.masocartas.data.remote.dto.CardResponseDto
import com.retoandroid.masocartas.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiYiGiOh {

    @GET("cardinfo.php")
    suspend fun getCards(
        @Query("num") num: Int = Constants.PAGE_SIZE,
        @Query("offset") offset: Int = 0,
        @Query("fname") fname: String? = null
    ): CardResponseDto

    @GET("cardinfo.php")
    suspend fun getCardById(@Query("id") id: Long): CardResponseDto
}