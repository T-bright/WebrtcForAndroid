package com.tbright.webrtcdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbright.webrtcdemo.adapter.DevicesAdapter
import com.tbright.webrtcdemo.utils.SearchLanDevicesUtils
import com.tbright.webrtcdemo.utils.SocketManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val devicesList = arrayListOf<String>()
    private val devicesAdapter by lazy {
        DevicesAdapter(devicesList).apply {
            setOnItemClickListener { adapter, view, position ->
                SocketManager.startClientSocket("http://${devicesList[position]}:5555/")
            }
        }
    }


    //华为荣耀ip：192.168.16.2
    //努比亚ip：192.168.16.35
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createChatRoom.setOnClickListener {
//            RoomActivity.start(this@MainActivity,"")
            thread {
                SocketManager.startServerSocket()
            }
        }

        searchChatRoom.setOnClickListener {
//            devicesList.clear()
//            devicesList.add("192.168.16.2")
//            devicesAdapter.notifyDataSetChanged()
//            SocketManager.startWebSocket("192.168.16.2")
            SearchLanDevicesUtils().search{ip->
                runOnUiThread {
                    devicesList.add(ip)
                    devicesAdapter.notifyDataSetChanged()
                }
            }
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = devicesAdapter
    }
}
