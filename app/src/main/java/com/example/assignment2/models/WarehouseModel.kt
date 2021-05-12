package com.example.assignment2.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize


@IgnoreExtraProperties

@Parcelize
data class WarehouseModel(var uid: String = "",
                          var stname: String = "",
                          var stquantity: String = "",
                          var stcountry:String= "",
                          var pallettype: String ="",
                          var palletnumber:String = "")
                        : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "stname" to stname,
            "stquantity" to stquantity,
            "stcountry" to stcountry,
            "pallettype" to pallettype,
            "palletnumber" to palletnumber
        )
    }
}


