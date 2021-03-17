package com.gaura.learn.imagesearch.api

import com.gaura.learn.imagesearch.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)