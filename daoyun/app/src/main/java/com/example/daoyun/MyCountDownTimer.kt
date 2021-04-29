package com.example.daoyun

import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView

class MyCountDownTimer (
        millisInFuture: Long,
        countDownInterval: Long,
        private var tv: Button
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        //倒计时过程中要实现的逻辑
        tv.text=(millisUntilFinished/1000).toString()+"s后重新发送"
    }

    override fun onFinish() {
        //倒计时完成
        tv.isEnabled=true
        tv.text="重新发送"
    }

}