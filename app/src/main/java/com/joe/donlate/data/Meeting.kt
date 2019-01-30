package com.joe.donlate.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint


sealed class Meeting(val viewType: Int)

data class Room(
    @Transient val title: String = "",
    @Transient val createAt: Timestamp = Timestamp.now(),
    @Transient val deadLine: Timestamp = Timestamp.now(),
    @Transient val maxParticipants: Int = 0,
    @Transient val participants: List<DocumentReference> = emptyList(),
    @Transient val url: String = "",
    @Transient val penaltyTime: Int = 0,
    @Transient val penaltyFee: Int = 0,
    @Transient val coordinate: GeoPoint = GeoPoint(0.0, 0.0)
) : Meeting(viewType = 0)

class AddButton(val click: () -> Unit) : Meeting(viewType = 1)