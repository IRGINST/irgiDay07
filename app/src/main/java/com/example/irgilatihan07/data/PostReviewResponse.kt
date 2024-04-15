package com.example.irgilatihan07.data

import com.google.gson.annotations.SerializedName

data class PostReviewResponse(

    @SerializedName("customerReviews")
    val customerReviews: List<CustomerReviewsItem>,

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
)