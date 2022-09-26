package com.nvc.foodmanager.data.source.firebase

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nvc.foodmanager.model.Notification
import com.nvc.foodmanager.model.Order
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.sql.Timestamp

class OrderRemote(
    private val orderRef: DatabaseReference,
    private val orderNotificationRef: DatabaseReference
) {
    fun getDataRemote() = callbackFlow<Result<List<Order>>> {
        val orderListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val order = snapshot.children.map {
                    it.getValue(Order::class.java)
                }
                this@callbackFlow.trySendBlocking(Result.success(order.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }
        }
        orderRef.addListenerForSingleValueEvent(orderListener)
        awaitClose {
            orderRef.removeEventListener(orderListener)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun updateStatus(order: Order) = callbackFlow<Boolean> {
        val taskSuccess = OnSuccessListener<Void> {
            orderNotificationRef.child(order.userID).push().apply {
                val sdf = SimpleDateFormat("HH:mm:ss dd/MM/yyyy")
                val message = if(order.status==1) "Đơn hàng của bạn đã được giao hàng công"
                else "Đơn hàng của bạn đã bị hủy vì hết hàng"
                setValue(
                    Notification(
                        key!!, message, order.id, sdf.format(
                            Timestamp(System.currentTimeMillis())
                        ).toString()
                    )
                )
            }
            trySendBlocking(true)
        }
        val taskFailure = OnFailureListener {
            trySendBlocking(false)
        }

        orderRef.child(order.id)
            .updateChildren(
                mapOf(
                    "status" to order.status,
                )
            )
            .addOnSuccessListener(taskSuccess)
            .addOnFailureListener(taskFailure)
        awaitClose {
            //remove callback
        }
    }
}