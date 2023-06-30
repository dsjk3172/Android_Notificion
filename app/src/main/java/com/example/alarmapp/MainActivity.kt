package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.alarmapp.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //binding 초기화
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        //알람 설정
        binding.timeBtn.setOnClickListener {

            var timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "Time Picker")
        }

        //알람 취소
        binding.alarmCancelBtn.setOnClickListener {
            cancleAlarm()
        }

        val intent = Intent(this, Schedule::class.java)

        val ScheduleBtn: Button = findViewById(R.id.button_schedule)
        ScheduleBtn.setOnClickListener{
            startActivity(intent)
        }
    }

    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {

        var c = Calendar.getInstance()

        //시간 설정
        c.set(Calendar.HOUR_OF_DAY, hourOfDay) //시
        c.set(Calendar.MINUTE, minute)//분
        c.set(Calendar.SECOND, 0)//초

        updateTimeText(c)
        startAlarm(c)
    }

    //화면에 시간 지정
    private fun updateTimeText(c: Calendar) {

        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        binding.timeText.text = "알람시간: "
        binding.timeText.append(curTime)
    }

    //알람 설정
    private fun startAlarm(c: Calendar){
        //알람매니저 선언
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlertReceiver:: class.java)

        //데이터 담기
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        intent.putExtra("time", curTime)
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE)

        //설정 시간이 현재시간 이전이면 +1일
        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1)
        }

        val repeatInterval = AlarmManager.INTERVAL_DAY

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis, repeatInterval, pendingIntent)
    }

    //알람 취소
    private fun cancleAlarm(){
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlertReceiver:: class.java)
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)
        binding.timeText.text = "알람취소"

    }
}