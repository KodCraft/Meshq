package az.kodcraft.meshq.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.meshq.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send the token to your server
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d("MyFirebaseService", "sendRegistrationToServer: $token")

        // Get user ID (adjust this to your app's logic)
        val userId = UserManager.getUser().id

        // Create a map of user ID and token
        val userTokenData = mapOf(
            "user_id" to userId,
            "fcm_token" to token
        )

        // Save the token to Firestore
        val db = FirebaseFirestore.getInstance()

        db.collection("user_tokens_collection").add(userTokenData)
            .addOnSuccessListener {
                Log.d("MyFirebaseService", "Token saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("MyFirebaseService", "Error saving token", e)
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("MyFirebaseService", "onMessageReceived: ${remoteMessage.messageType}")
        // Handle the message
        remoteMessage.notification?.let {
            sendNotification(it.body, it.title)
        }
    }

    private fun sendNotification(messageBody: String?, title: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "meshq_default_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(az.kodcraft.core.R.drawable.ic_dumbbell)
            .setColor(PrimaryTurq.toArgb())
            .setContentTitle(title ?: "Meshq")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }
}
