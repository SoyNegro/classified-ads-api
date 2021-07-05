package com.thedarksideofcode.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class Classified(
    val id: ObjectId = ObjectId.get(),
    val category: String,
    val subcategory: String,
    val country: String,
    val state: String,
    val city: String?,
    val title: String,
    val content: String,
    val viewCount: Int = 0,
    val publishedDate: LocalDateTime = LocalDateTime.now(),
    val updatedDate:LocalDateTime = LocalDateTime.now(),
    val expirationDate: LocalDateTime = LocalDateTime.now().plusMonths(2),
    val gallery: MutableList<Picture>? = mutableListOf(),
    val publisherName: String,
    val publisherEmail: String,
    val publisherPhone: String?,
    val owningUser: String, // Attribute unique enough to distinguish this classified domain, shall not be their id
)