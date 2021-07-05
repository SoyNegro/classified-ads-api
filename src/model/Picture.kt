package com.thedarksideofcode.model

import kotlinx.serialization.Serializable
import org.bson.types.Binary
import org.bson.types.ObjectId
import java.util.*

@Serializable
data class Picture(
    val id: ObjectId = ObjectId.get(),
    val title: String,
    val picture: Binary?
){
    fun getPictureAsEncodedString(): String? {
        return Base64.getEncoder().encodeToString(picture!!.data)
    }

}