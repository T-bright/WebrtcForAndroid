package com.tbright.webrtcdemo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tbright.webrtcdemo.R
import kotlinx.android.synthetic.main.item_devices.view.*

class DevicesAdapter(resultDatas: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_devices, resultDatas) {
    override fun convert(helper: BaseViewHolder, item: String) {
        with(helper.itemView){
            ip.text = item
        }
    }
}