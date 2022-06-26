package com.example.toilet_seoul

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Toilet(val toiletNum: Int? = null, val toiletNm: String? = null, val rdnmadr: String? = null, val lnmadr: String? = null, val unisexToiletYn: String? = null,
                  val menToiletBowlNumber: Int? = null, val menUrineNumber: Int? = null, val menHandicapToiletBowlNumber: Int? = null,
                  val menHandicapUrinalNumber: Int? = null, val menChildrenToiletBowlNumber: Int? = null, val menChildrenUrinalNumber: Int? = null,
                  val ladiesToiletBowlNumber: Int? = null, val ladiesHandicapToiletBowlNumber: Int? = null, val ladiesChildrenToiletBowlNumber: Int? = null,
                  val phoneNumber: String? = null, val openTime: String? = null, val latitude: Double? = null, val longitude: Double? = null,
                  val emgBellYn: String? = null, val enterentCctvYn: String? = null, val dipersExchgPosi: String? = null, val review: String? = null): Serializable {

    @Exclude
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "toiletNum" to toiletNum,
            "toiletNm" to toiletNm,
            "rdnmadr" to rdnmadr,
            "lnmadr" to lnmadr,
            "unisexToiletYn" to unisexToiletYn,
            "menToiletBowlNumber" to menToiletBowlNumber,
            "menUrineNumber" to menUrineNumber,
            "menHandicapToiletBowlNumber" to menHandicapToiletBowlNumber,
            "menHandicapUrinalNumber" to menHandicapUrinalNumber,
            "menChildrenToiletBowlNumber" to menChildrenToiletBowlNumber,
            "menChildrenUrinalNumber" to menChildrenUrinalNumber,
            "ladiesToiletBowlNumber" to ladiesToiletBowlNumber,
            "ladiesHandicapToiletBowlNumber" to ladiesHandicapToiletBowlNumber,
            "ladiesChildrenToiletBowlNumber" to ladiesChildrenToiletBowlNumber,
            "phoneNumber" to phoneNumber,
            "openTime" to openTime,
            "latitude" to latitude,
            "longitude" to longitude,
            "emgBellYn" to emgBellYn,
            "enterentCctvYn" to enterentCctvYn,
            "dipersExchgPosi" to dipersExchgPosi
        )
    }

}

