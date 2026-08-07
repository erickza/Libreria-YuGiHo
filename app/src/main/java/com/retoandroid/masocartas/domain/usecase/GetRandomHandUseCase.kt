package com.retoandroid.masocartas.domain.usecase

import com.retoandroid.masocartas.domain.repository.CardRepository
import javax.inject.Inject

class GetRandomHandUseCase @Inject constructor(
    private val repository: CardRepository
) {
    suspend operator fun invoke(size: Int = 5) = repository.getRandomHand(size)
}