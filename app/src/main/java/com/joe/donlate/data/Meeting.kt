package com.joe.donlate.data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

sealed class MeetingItemMode
object MeetingItemNormalMode : MeetingItemMode()
object MeetingItemDeleteMode : MeetingItemMode()

data class Meeting(
    @Transient val title: String = "",
    @Transient val createAt: Timestamp = Timestamp.now(),
    @Transient val deadLine: Timestamp = Timestamp.now(),
    @Transient val maxParticipants: Int = 0,
    @Transient val participants: List<DocumentReference> = emptyList(),
    @Transient val url: String = "",
    @Transient val penaltyTime: Int = 0,
    @Transient val penaltyFee: Int = 0,
    @Transient val coordinate: GeoPoint = GeoPoint(0.0, 0.0),
    var mode: MeetingItemMode = MeetingItemNormalMode
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readInt(),
        listOf<DocumentReference>().apply {
            parcel.readList(this, DocumentReference::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeParcelable(createAt, flags)
        parcel.writeParcelable(deadLine, flags)
        parcel.writeInt(maxParticipants)
        parcel.writeString(url)
        parcel.writeInt(penaltyTime)
        parcel.writeInt(penaltyFee)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Meeting> {
            override fun createFromParcel(parcel: Parcel): Meeting {
                return Meeting(parcel)
            }

            override fun newArray(size: Int): Array<Meeting?> {
                return arrayOfNulls(size)
            }
        }
    }
}