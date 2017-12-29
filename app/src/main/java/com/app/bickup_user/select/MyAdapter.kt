package com.app.bickup_user.select

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.app.bickup_user.R
import kotlinx.android.synthetic.main.row_layout.view.*


class MyAdapter(val context: Context, val list: ArrayList<Model>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private val filterList: MutableList<Model>?

    init {
        this.filterList = ArrayList<Model>()
        this.filterList.addAll(this.list)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.bindFun(filterList!![position])

        holder.check.setOnCheckedChangeListener(null);
        holder.check.isChecked = filterList[position].isSelected;

        holder.check.setOnCheckedChangeListener({ _, isChecked ->
            filterList[holder.adapterPosition].isSelected = isChecked
        })

    }

    override fun getItemCount(): Int = filterList?.size ?:0


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var check: CheckBox = itemView.findViewById<View>(R.id.check) as CheckBox


        val context: Context = itemView.context
        fun bindFun(m: Model) {
            itemView.tv_name.text = m.name
           // Picasso.with(context).load(m.avatar).into(itemView.img);


        }
    }

    fun filter(text: String) {
        Thread(Runnable {
            filterList!!.clear()
            if (TextUtils.isEmpty(text)) {
                filterList.addAll(list)
            } else {
                for (item in list) {
                    if (item.name.toLowerCase().contains(text.toLowerCase()) || item.name.toLowerCase().contains(text.toLowerCase())) {
                        filterList.add(item)
                    }
                }
            }

            (context as Activity).runOnUiThread {
                notifyDataSetChanged()
            }
        }).start()

    }

}