package com.example.alarmapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(base: Context?) : ContextWrapper(base) {

    private val channelID = "channelID"
    private val channelNm = "channelNm"

    init {
        //안드로이드 버전이 오레오거나 이상이면 채널 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
    }

    //채널 생성
    private fun createChannel(){
        var channel = NotificationChannel(channelID, channelNm,
            NotificationManager.IMPORTANCE_DEFAULT)

        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        getManager().createNotificationChannel(channel)
    }

    //NotificationManager 생성
    fun getManager(): NotificationManager{
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    //notification 설정
    fun  getChannelNOtification(time: String?):NotificationCompat.Builder{

        return NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle(time)
            .setContentText("알람발생")
            .setSmallIcon(R.drawable.icon_example)
    }
}