package com.thedarksideofcode.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class AccessCredential(val apiKey: String,
                            val password: String,
                            val ownerId: ObjectId,
                            val permissions: String)