package com.solid.network.base.usecase

import kotlinx.coroutines.flow.Flow

interface BaseUseCaseNoParam<out Result> {
    suspend fun execute(): Flow<Result>
}
