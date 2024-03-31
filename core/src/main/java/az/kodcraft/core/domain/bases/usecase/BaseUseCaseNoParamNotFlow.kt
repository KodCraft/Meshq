package com.solid.copilot.network.base.usecase

interface BaseUseCaseNoParamNotFlow<out Result> {
    suspend fun execute(): Result
}
