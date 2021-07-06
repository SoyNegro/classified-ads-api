package com.thedarksideofcode.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessCredential(
    val apiKey: String,
    val password: String,
    val owningUser: String,
    val permissions: String
)
