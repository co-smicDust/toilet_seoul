package com.example.toilet_seoul

import com.google.android.gms.maps.model.LatLng
import java.text.FieldPosition

data class Review(
    var userNm: String?,

    var ratingStar: Float?,

    var contentR: String?,

    var image: Int = 0
){}