package com.solid.copilot.network.base.usecase

interface BaseUseCaseNoResult<in Parameter> {
    suspend fun execute(param: Parameter)
}