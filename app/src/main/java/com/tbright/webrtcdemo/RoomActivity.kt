package com.tbright.webrtcdemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tbright.webrtcdemo.utils.SocketManager
import com.tbright.webrtcdemo.utils.extraDelegate

class RoomActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context, ip: String) {
            context.startActivity(Intent(context, RoomActivity::class.java).apply {
                putExtra("ip", ip)
            })
        }
    }

    private var ip by extraDelegate("ip","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        SocketManager().startServerSocket()
    }
}