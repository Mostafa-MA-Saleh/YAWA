package com.gmail.mostafa.ma.saleh.yawa.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Mostafa Saleh on 05/01/2017.
 */

@Parcelize
data class City(@SerializedName("id")
           @Expose
           val id: String,
           @SerializedName("name")
           @Expose
           val name: String) : Parcelable
