package com.app.bickup_user.tracking_status

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bickup_user.R
import kotlinx.android.synthetic.main.row_card_view_status.view.*
import java.lang.Long.parseLong
import java.text.SimpleDateFormat
import java.util.*

class MyAdapter(val list:ArrayList<Status>, val list_uncheck:ArrayList<Status>):RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    var list_uncheck_: ArrayList<Status>? = null
      init {
          this.list_uncheck_=list_uncheck
      }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
          val view=LayoutInflater.from(parent!!.context).inflate(R.layout.row_card_view_status,parent,false)
          return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder?, position: Int) {
          holder!!.findFun(list[position],position,list_uncheck_)
    }
    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        private val context: Context =itemView.context
        fun findFun(statusModel: Status, position: Int, list_uncheck_: ArrayList<Status>?){

            itemView.txt_booking_status_time.text=getDateTime(statusModel.timestamp.toString())
            itemView.txt_booking_status.text= statusModel.status.toString()
            itemView.img_booking_status.setImageResource(R.drawable.de_checkbox)


            Log.d("TAGS","Uncheck -  ${list_uncheck_!!.size}")


            val i:Int =list_uncheck_!!.size

            if(i>position){
                itemView.txt_booking_status_time.text=getDateTime(statusModel.timestamp.toString())
                itemView.txt_booking_status.text= statusModel.status.toString()
                itemView.img_booking_status.setImageResource(R.drawable.ac_checkbox)
            }







/*

            if(position==0){
                itemView.txt_booking_status_time.text=getDateTime(statusModel.timestamp)
                itemView.txt_booking_status.text=statusModel.name
                itemView.img_booking_status.setImageResource(R.drawable.ac_checkbox)
            }
*/



        }


        private fun getDateTime(s: String): String? {
            try {
                val sdf = SimpleDateFormat("HH:mm a")
                val netDate = Date(parseLong(s))
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }

    }


}