package com.app.bickup_user.select

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.app.bickup_user.R
import com.app.bickup_user.model.GoodsAndHelper
import com.app.bickup_user.utility.ConstantValues
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_types_goods.*
import kotlinx.android.synthetic.main.toolbar_layout.*






class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {


    lateinit var myAdapter: MyAdapter
    lateinit var list: ArrayList<Model>
    var data: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_types_goods)
//        this.supportActionBar!!.hide()

        data = intent.getStringExtra("key")


        val goodsAndHelper = intent.getParcelableExtra<GoodsAndHelper>(ConstantValues.GOODSDETAILS)



        list_types_goods.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        list = ArrayList()

        for(m in goodsAndHelper.goods ){
            list.add(Model(m.goodsID, m.goodsName, false));
        }


        /* list.add(Model1(12374, "Car", "http://1.bp.blogspot.com/-Mk5AyGkeYnw/Va1k6pum6eI/AAAAAAAAFlg/Tlorcet_EQ8/s1600/20150712_174650.jpg", false))
         list.add(Model1(12133, "Bus", "http://images6.fanpop.com/image/photos/40000000/Karun-kumar-Shekhpurwa-pusauli-karunkumar2525-Neha-kumari-bihar-mohania-katrina-kaif-40040269-60-120.jpg", false))
         list.add(Model1(11253, "Chair", "http://www.tellychakkar.com/sites/tellychakkar.com/files/imagecache/Display_445x297/images/story/2013/03/04/neha.jpg", false))
         list.add(Model1(14543, "A/C", "http://static.sbuys.in/media/registration_users/DRC031.JPG", false))
 */
        myAdapter = MyAdapter(this@MainActivity, list)
        list_types_goods.adapter = myAdapter
        setupSearchView()

        if (data != null) {
            Log.d("TAGS", "Reponse Data With array $data")
            if (data != null && data != "") {
                val d = data!!.split("~")
                for (j in 0 until d.size) {
                    for (i in 0 until list.size) {
                        val m: Model = list[i]
                        if (m.ids == d[j]) {
                            m.isSelected = true
                            myAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }


        txt_activty_header.text=resources.getString(R.string.txt_types)


        backImage_header.setOnClickListener({
            onBackPressed()
        })


        img_tick_toolbar.visibility=View.VISIBLE
        img_tick_toolbar.setOnClickListener({
            onBackPressed()
        })


    }


    private fun setupSearchView() {
        search_view.setIconifiedByDefault(false);
        search_view.setOnQueryTextListener(this);
        search_view.isSubmitButtonEnabled = true;
        search_view.queryHint = "Search Here";
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        myAdapter.filter(newText!!);
        return true;
    }

    override fun onBackPressed() {
        super.onBackPressed()


        val mylist: ArrayList<HashMap<String, String>> = ArrayList()
        var map: HashMap<String, String>
        for (i in 0 until list.size) {
            val m: Model = list[i]
            if (m.isSelected) {
                map = HashMap()
                map.put("id", m.ids)
                map.put("name", m.name)
                mylist.add(map)
                myAdapter.notifyDataSetChanged()
            }

        }
        var data = Gson().toJson(mylist)
        Log.d("TAGS", data)


        val sp = getSharedPreferences("LoginInfos", 0)
        val editor = sp.edit()
        editor.clear();
        editor.commit();

        editor.putString("key", data)
        editor.commit()
    }
}
