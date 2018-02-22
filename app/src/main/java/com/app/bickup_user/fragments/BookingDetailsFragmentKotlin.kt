package com.app.bickup_user.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.app.bickup_user.GlobleVariable.GloableVariable
import com.app.bickup_user.GlobleVariable.GloableVariable.is_check_image_product
import com.app.bickup_user.GoodsActivity
import com.app.bickup_user.R
import com.app.bickup_user.TrackDriverActivity
import com.app.bickup_user.adapter.GoodAddAdapter
import com.app.bickup_user.adapter.GoodsImagesAdapter
import com.app.bickup_user.interfaces.GoodsImagesInterface
import com.app.bickup_user.model.GoodsAddModel
import com.app.bickup_user.model.Helper
import com.app.bickup_user.model.User
import com.app.bickup_user.utility.CommonMethods
import com.app.bickup_user.utility.ConstantValues
import com.facebook.FacebookSdk.getApplicationContext
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.gson.Gson
import com.immigration.restservices.APIService
import com.immigration.restservices.ApiUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.DecimalFormat
import java.util.*


class BookingDetailsFragmentKotlin : Fragment(), View.OnClickListener, GoodsImagesInterface {
   private var Image_Sp: SharedPreferences? = null
   private var Image_Sp1: SharedPreferences? = null
   private var array_ImageBitmap: JSONArray? = null
   private var mTypefaceRegular: Typeface? = null
   private var mTypefaceBold: Typeface? = null
   private var mActivityReference: GoodsActivity? = null
   private var btnConfirmBooking: Button? = null
   private var edtContactNumber: EditText? = null
   private var edtContactName: EditText? = null
   private var txtPickupLocation: TextView? = null
   private var txtDropLocation: TextView? = null
   private var txtPickupContactname: TextView? = null
   private var txtDropContactname: TextView? = null
   private var txtPickupContactNumber: TextView? = null
   private var txtDropContactNumber: TextView? = null
   private var txtDescription: TextView? = null
   private var txtPriceWithHelper: TextView? = null
   private var txtTotalAmount: TextView? = null
   private var btnPaidByMe: TextView? = null
   private var btnOther: TextView? = null
   private var txtHelerString: TextView? = null
   private var listImages: ArrayList<Bitmap>? = null
   private var recyclerView: RecyclerView? = null
   private var types_good_recyclerView: RecyclerView? = null
   private var lists: ArrayList<GoodsAddModel>? = null
   private var goodAddAdapter: GoodAddAdapter? = null
   private var response: String? = null
   private var types_good: ArrayList<String>? = null
   private var select_Data: String? = null
   private var helper_check = 0
   private var list_helper: MutableList<Helper>? = null
   private var helper_json: String? = null
   private var helper_prices: String? = null
   private var sp_heper: SharedPreferences? = null
   private var img_helpers: ImageView? = null
   private val helper_id = ""
   private val helper_price = ""
   private val helper_person_count = ""
   private var Total_prices = 0.0
   private var mCoordinatorLayout: CoordinatorLayout? = null
   private var circularProgressView: CircularProgressView? = null
   private var array_list_helper: JSONArray? = null
   private var data: String? = null
   private var list_no_of_helper: String? = null
   private var typesOfGoodArray: JSONArray? = null
   lateinit var apiService: APIService
   
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      
      apiService = ApiUtils.apiService
      
      lists = ArrayList()
      list_helper = ArrayList()
      GloableVariable.Tag_paid_by_type = "1"
      
      Image_Sp = getApplicationContext().getSharedPreferences("Image_Sp", 0)
      Image_Sp1 = getApplicationContext().getSharedPreferences("LoginInfos", 0)
   }
   
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                             savedInstanceState: Bundle?): View? {
      // Inflate the layout for this fragment
      val view = inflater.inflate(R.layout.fragment_booking_detail, container, false)
      initializeViews(view)
      is_check_image_product = 2
      return view
   }
   
   private fun setImagesList() {
      val bitmap1: Bitmap? = null
      val bitmap2: Bitmap? = null
      val bitmap3: Bitmap? = null
      val bitmap4: Bitmap? = null
      if (listImages != null) {
         val goodsImagesAdapter = GoodsImagesAdapter(mActivityReference, listImages)
         val mLayoutManager = LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false)
         recyclerView!!.layoutManager = mLayoutManager
         recyclerView!!.itemAnimator = DefaultItemAnimator()
         recyclerView!!.adapter = goodsImagesAdapter
      }
   }
   
   override fun onResume() {
      super.onResume()
      //      ---------------------------------------------------Helper--------------------------------
      sp_heper = activity!!.getSharedPreferences("helper", 0)
      helper_json = sp_heper!!.getString("key_helper", "")
      Log.d("TAGS Data:", "helper_json" + helper_json!!)
      
      try {
         val arr = JSONArray(helper_json!!.toString())
         for (i in 0 until helper_json!!.length) {
            val obj = arr.getJSONObject(i)
            list_helper!!.add(Helper(obj.getString("person_count"), obj.getString("helper_id"), obj.getString("price")))
         }
      } catch (e: JSONException) {
         e.printStackTrace()
      }
      //---------------------------------Type of Goods------------------------------------
      val sp = activity!!.getSharedPreferences("LoginInfos", 0)
      select_Data = sp.getString("key", "")
      Log.d("TAGS Data:", select_Data)
      
      
      if (select_Data != null) {
         lists!!.clear()
         var array: JSONArray? = null
         try {
            array = JSONArray(select_Data)
            
            for (i in 0 until array.length()) {
               val obj = array.getJSONObject(i)
               lists!!.add(GoodsAddModel(obj.getString("id"), obj.getString("name")))
            }
         } catch (e: JSONException) {
            e.printStackTrace()
         }
      }
      
      typesOfGoodArray = JSONArray()
      types_good = ArrayList()
      for (n in lists!!) {
         types_good!!.add(n.name)
         typesOfGoodArray!!.put(n.name)
      }
      //------------------------------------------------------------------------------------------
   }
   
   private fun initializeViews(view: View) {
      types_good_recyclerView = view.findViewById(R.id.types_good_recyclerView)
      mCoordinatorLayout = view.findViewById(R.id.cordinatorlayout)
      circularProgressView = view.findViewById(R.id.progress_view)
      
      mTypefaceRegular = Typeface.createFromAsset(mActivityReference!!.assets, ConstantValues.TYPEFACE_REGULAR)
      mTypefaceBold = Typeface.createFromAsset(mActivityReference!!.assets, ConstantValues.TYPEFACE_BOLD)
      recyclerView = view.findViewById<View>(R.id.recyclerImages_booking) as RecyclerView
      setImagesList()
      AddGoodOption()
      
      btnConfirmBooking = view.findViewById<View>(R.id.btn_confirm_booking) as Button
      btnPaidByMe = view.findViewById<View>(R.id.btn_paid_by_me) as TextView
      btnOther = view.findViewById<View>(R.id.paid_by_other) as TextView
      btnPaidByMe!!.setOnClickListener(this)
      btnOther!!.setOnClickListener(this)
      btnConfirmBooking!!.setOnClickListener(this)
      btnConfirmBooking!!.setOnClickListener(this)
      
      btnPaidByMe!!.setOnClickListener(this)
      btnOther!!.setOnClickListener(this)
      btnPaidByMe!!.tag = false
      btnOther!!.tag = true
      btnConfirmBooking!!.typeface = mTypefaceRegular
      btnConfirmBooking!!.typeface = mTypefaceRegular
      btnPaidByMe!!.typeface = mTypefaceRegular
      btnOther!!.typeface = mTypefaceRegular
      
      
      img_helpers = view.findViewById(R.id.img_helpers)
      
      
      edtContactName = view.findViewById<View>(R.id.edt_contact_person_name) as EditText
      edtContactNumber = view.findViewById<View>(R.id.edt_contact_person_number) as EditText
      txtPickupContactname = view.findViewById<View>(R.id.value_pickup_contact_name) as TextView
      txtPickupContactNumber = view.findViewById<View>(R.id.value_pickup_contact_number) as TextView
      txtPickupLocation = view.findViewById<View>(R.id.value_pickup_location) as TextView
      
      edtContactNumber!!.typeface = mTypefaceRegular
      edtContactName!!.typeface = mTypefaceRegular
      txtPickupContactNumber!!.typeface = mTypefaceRegular
      txtPickupContactname!!.typeface = mTypefaceRegular
      txtPickupLocation!!.typeface = mTypefaceRegular
      
      txtDropContactname = view.findViewById<View>(R.id.value_drop_contact_name) as TextView
      txtDropContactNumber = view.findViewById<View>(R.id.value_drop_contact_number) as TextView
      txtDropLocation = view.findViewById<View>(R.id.value_drop_location) as TextView
      
      txtDropContactNumber!!.typeface = mTypefaceRegular
      txtDropContactname!!.typeface = mTypefaceRegular
      txtDropLocation!!.typeface = mTypefaceRegular
      
      txtDescription = view.findViewById<View>(R.id.value_description) as TextView
      txtPriceWithHelper = view.findViewById<View>(R.id.value_amount_string) as TextView
      txtTotalAmount = view.findViewById<View>(R.id.value_total_string) as TextView
      txtHelerString = view.findViewById<View>(R.id.txt_helper_string) as TextView
      
      txtDescription!!.typeface = mTypefaceRegular
      txtPriceWithHelper!!.typeface = mTypefaceRegular
      txtTotalAmount!!.typeface = mTypefaceBold
      txtHelerString!!.typeface = mTypefaceRegular
      
      
      txtPickupLocation!!.text = GloableVariable.Tag_pickup_location_address
      txtPickupContactname!!.text = GloableVariable.Tag_pickup_contact_name
      txtPickupContactNumber!!.text = GloableVariable.Tag_pickup_contact_number
      
      
      txtDropLocation!!.text = GloableVariable.Tag_drop_location_address
      txtDropContactname!!.text = GloableVariable.Tag_drop_contact_name
      txtDropContactNumber!!.text = GloableVariable.Tag_drop_contact_number
      
      
      if (GloableVariable.Tag_paid_by_type == "1") {
         edtContactName!!.setText(GloableVariable.Tag_pickup_contact_name)
         edtContactNumber!!.setText(GloableVariable.Tag_pickup_contact_number)
      }
      
      
      sp_heper = activity!!.getSharedPreferences("helper", 0)
      helper_json = sp_heper!!.getString("key_helper", "")
      try {
         val arr = JSONArray(helper_json!!.toString())
         for (i in 0 until helper_json!!.length) {
            val obj = arr.getJSONObject(i)
            list_helper!!.add(Helper(obj.getString("person_count"), obj.getString("helper_id"), obj.getString("price")))
         }
      } catch (e: JSONException) {
         e.printStackTrace()
      }
      val help = GloableVariable.Tag_helper
      
      if (help == "2") {
         helper_check = 2
         img_helpers!!.visibility = View.VISIBLE
         img_helpers!!.setImageResource(R.drawable.ac_double_helper)
         val mylist = ArrayList<HashMap<String, String>>()
         var map: HashMap<String, String>
         for (h in list_helper!!) {
            if (h.helper_person_count == "2") {
               map = HashMap()
               map.put("helper_id", h.helper_id)
               map.put("price", h.helper_price)
               helper_prices = h.helper_price
               mylist.add(map)
            }
         }
         data = Gson().toJson(mylist)
         Log.d("TAGS-", "helper_id 2:" + data!!)
      }
      
      
      if (help == "1") {
         helper_check = 1
         img_helpers!!.visibility = View.VISIBLE
         img_helpers!!.setImageResource(R.drawable.sing_helper)
         val mylist = ArrayList<HashMap<String, String>>()
         var map: HashMap<String, String>
         for (h in list_helper!!) {
            if (h.helper_person_count == "1") {
               map = HashMap()
               map.put("helper_id", h.helper_id)
               map.put("price", h.helper_price)
               helper_prices = h.helper_price
               mylist.add(map)
            }
         }
         data = Gson().toJson(mylist)
         Log.d("TAGS-", "helper_id 1:" + data!!)
      }
      
      
      if (help == "0") {
         helper_check = 0
         array_list_helper = JSONArray()
         img_helpers!!.visibility = View.GONE
         helper_prices = 0.00.toString()
         val mylistt = ArrayList<String>()
         data = Gson().toJson(mylistt)
         Log.d("TAGS-", "helper_id 0:" + data!!)
      }
      val mylist_no_of_helper = ArrayList<HashMap<String, String>>()
      val map: HashMap<String, String>
      map = HashMap()
      map.put("no_of_helpers", helper_check.toString())
      mylist_no_of_helper.add(map)
      list_no_of_helper = Gson().toJson(mylist_no_of_helper)
      Log.d("TAGS-", "helper_id :" + list_no_of_helper!!)
      
      
      
      txtDescription!!.text = GloableVariable.Tag_Good_Details_Description
      setTypeFaces(view)
      var value = 0.0
      var values22 = 0.0
      var value2 = 0.0
      var values = 0.0
      try {
         value = java.lang.Double.parseDouble(GloableVariable.Tag_total_price)
         values = java.lang.Double.parseDouble(DecimalFormat("#.##").format(value))
         //  Log.d("TAGS", String.valueOf(values));
      } catch (e: Exception) {
      }
      
      try {
         value2 = java.lang.Double.parseDouble(helper_prices)
         values22 = java.lang.Double.parseDouble(DecimalFormat("#.##").format(value2))
         // Log.d("TAGS", String.valueOf(values22));}
      } catch (e: Exception) {
      }
      
      Total_prices = values + values22
      txtPriceWithHelper!!.text = "$ " + (values.toString() + " +$" + helper_prices).toString()
      txtTotalAmount!!.text = "= $$Total_prices"
      GloableVariable.Tag_total_final_prices = "" + Total_prices
      //  Validate();
   }
   
   private fun setTypeFaces(view: View) {
      val txtPickupLocation = view.findViewById<View>(R.id.label_pickup_location) as TextView
      val txtPickupContact = view.findViewById<View>(R.id.label_pickup_contact) as TextView
      val txtDropLocation = view.findViewById<View>(R.id.label_drop_location) as TextView
      val txtDropContact = view.findViewById<View>(R.id.label_drop_contact) as TextView
      val txtLabelOfNumberHelper = view.findViewById<View>(R.id.label_number_of_helpers) as TextView
      // TextView txtContactPersonName=(TextView)view.findViewById(R.id.label_contact_person_name);
      // TextView txtContactPersonNumber=(TextView)view.findViewById(R.id.label_contact_person_number);
      val txtAmountDetails = view.findViewById<View>(R.id.label_amount_details) as TextView
      val txtDescription = view.findViewById<View>(R.id.label_description) as TextView
      val txtTypeDescription = view.findViewById<View>(R.id.label_types_of_goods) as TextView
      
      txtPickupLocation.typeface = mTypefaceRegular
      txtPickupContact.typeface = mTypefaceRegular
      txtAmountDetails.typeface = mTypefaceRegular
      txtDropLocation.typeface = mTypefaceRegular
      txtDropLocation.typeface = mTypefaceRegular
      txtDropContact.typeface = mTypefaceRegular
      txtLabelOfNumberHelper.typeface = mTypefaceRegular
      //  txtContactPersonName.setTypeface(mTypefaceRegular);
      //  txtContactPersonNumber.setTypeface(mTypefaceRegular);
      txtDescription.typeface = mTypefaceRegular
      txtTypeDescription.typeface = mTypefaceRegular
   }
   
   
   private fun validateFields(): Boolean {
      if (!CommonMethods.getInstance().validateEditFeild(edtContactName!!.text.toString())) {
         Toast.makeText(mActivityReference, mActivityReference!!.resources.getString(R.string.txt_vaidate_contact_person_name), Toast.LENGTH_SHORT).show()
         return false
      }
      
      
      if (!CommonMethods.getInstance().validateEditFeild(edtContactNumber!!.text.toString())) {
         Toast.makeText(mActivityReference, mActivityReference!!.resources.getString(R.string.txt_vaidate_mobile), Toast.LENGTH_SHORT).show()
         return false
      } else {
         if (!CommonMethods.getInstance().validateMobileNumber(edtContactNumber!!.text.toString(), 6)) {
            Toast.makeText(mActivityReference, mActivityReference!!.resources.getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show()
            return false
         }
      }
      return true
   }
   
   override fun onAttach(context: Context) {
      super.onAttach(context)
      mActivityReference = context as GoodsActivity
   }
   
   override fun onDetach() {
      super.onDetach()
   }
   
   private fun showPopUp(s: String?) {
      GloableVariable.Tag_booking_id = s
      val openDialog = Dialog(mActivityReference!!)
      openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
      openDialog.setContentView(R.layout.booking_confirmation_dialog)
      openDialog.setTitle("Custom Dialog Box")
      openDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
      /*  TextView travellerName = (TextView)openDialog.findViewById(R.id.txt_traveller_name_dialog);

        TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);
        Button btnDone = (Button)openDialog.findViewById(R.id.btn_done);
*/
      val btnAgree = openDialog.findViewById<Button>(R.id.btn_done)
      val edt_pickup_location = openDialog.findViewById<TextView>(R.id.edt_pickup_location)
      val edt_drop_location = openDialog.findViewById<TextView>(R.id.edt_drop_location)
      val txt_total = openDialog.findViewById<TextView>(R.id.txt_total)
      val txt_booking_id = openDialog.findViewById<TextView>(R.id.txt_booking_id)
      
      edt_pickup_location.text = GloableVariable.Tag_pickup_location_address
      edt_drop_location.text = GloableVariable.Tag_drop_location_address
      txt_total.text = "Total: $" + GloableVariable.Tag_total_final_prices
      val ss = GloableVariable.Tag_booking_id
      if (ss == null) {
         txt_booking_id.text = ""
      } else
         txt_booking_id.text = "#" + ss
      
      
      btnAgree.setOnClickListener {
         openDialog.dismiss()
         Clear_Preference()
         val intent = Intent(mActivityReference, TrackDriverActivity::class.java)
         startActivity(intent)
      }
      /*  btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
                Intent intent=new Intent(DriverActivity.this,GoodsActivity.class);
                startActivity(intent);
            }
        });*/
      openDialog.show()
   }
   
   override fun onClick(view: View) {
      val handlerGoodsNavigations = mActivityReference
      val id = view.id
      when (id) {
         R.id.btn_confirm_booking -> if (validateFields()) {
            GloableVariable.Tag_paid_by_name = edtContactName!!.text.toString()
            GloableVariable.paid_by_contact_number = edtContactNumber!!.text.toString()
            /*        Log.d("TAGS", "Paid price: " + GloableVariable.Tag_total_price);
                    Log.d("TAGS", "Paid type: " + GloableVariable.Tag_paid_by_type);
                    Log.d("TAGS", "Paid name: " + GloableVariable.Tag_paid_by_name);
                    Log.d("TAGS", "Paid num: " + GloableVariable.paid_by_contact_number);

                    Log.d("TAGS", "Paid Good type: " + GloableVariable.Tag_type_of_goods);
*/
            
            editProfile()
         }
         R.id.btn_paid_by_me -> {
            btnPaidByMe!!.background = mActivityReference!!.resources.getDrawable(R.drawable.sm_btn)
            btnOther!!.setBackgroundColor(mActivityReference!!.resources.getColor(R.color.white))
            btnPaidByMe!!.setTextColor(mActivityReference!!.resources.getColor(R.color.white))
            btnOther!!.setTextColor(mActivityReference!!.resources.getColor(R.color.grey_text_color))
            btnPaidByMe!!.tag = true
            btnOther!!.tag = false
            
            GloableVariable.Tag_paid_by_type = "1"
            
            
            edtContactName!!.setText(GloableVariable.Tag_pickup_contact_name)
            edtContactNumber!!.setText(GloableVariable.Tag_pickup_contact_number)
         }
         R.id.paid_by_other -> {
            btnPaidByMe!!.setBackgroundColor(mActivityReference!!.resources.getColor(R.color.white))
            btnOther!!.background = mActivityReference!!.resources.getDrawable(R.drawable.sm_btn)
            btnPaidByMe!!.setTextColor(mActivityReference!!.resources.getColor(R.color.grey_text_color))
            btnOther!!.setTextColor(mActivityReference!!.resources.getColor(R.color.white))
            btnPaidByMe!!.tag = false
            btnOther!!.tag = true
            GloableVariable.Tag_paid_by_type = "2"
            edtContactName!!.setText("")
            edtContactNumber!!.setText("")
         }
      }
   }
   
   
   override  fun setImagelist(listimage: ArrayList<Bitmap>) {
      this.listImages = listimage
   }
   
   private fun AddGoodOption() {
      goodAddAdapter = GoodAddAdapter(mActivityReference, lists)
      val mLayoutManager = LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false)
      types_good_recyclerView!!.layoutManager = mLayoutManager
      types_good_recyclerView!!.itemAnimator = DefaultItemAnimator()
      types_good_recyclerView!!.adapter = goodAddAdapter
   }
   
   
   private fun Validate() {
      array_ImageBitmap = JSONArray()
      for (i in listImages!!.indices) {
         val b = listImages!![i]
         array_ImageBitmap!!.put(onCaptureImageResult(b))
      }
      //  Log.d("TAGS","setImageBitmap  : "+array_ImageBitmap.toString());
   }
   
   /*  if (i == 1) {

                if (b != null) {
                    imgFile2 = onCaptureImageResult(b);
                    m2.setImageBitmap(b);

                }
            }*/
   private fun onCaptureImageResult(bitmap: Bitmap): File {
      val imgFile: File
      val bytes = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
      imgFile = File(Environment.getExternalStorageDirectory(),
       System.currentTimeMillis().toString() + ".jpg")
      val fo: FileOutputStream
      try {
         imgFile.createNewFile()
         fo = FileOutputStream(imgFile)
         fo.write(bytes.toByteArray())
         fo.close()
      } catch (e: FileNotFoundException) {
         e.printStackTrace()
      } catch (e: IOException) {
         e.printStackTrace()
      }
      
      return imgFile
   }
   
   companion object {
      private val MEDIA_TYPE_PNG = MediaType.parse("image/png")
      var Tag = BookingDetailsFragmentKotlin::class.java.simpleName
   }
   
   private fun editProfile() {
      var sss = ""
      if (GloableVariable.Tag_distance == null || GloableVariable.Tag_distance === "") {
         sss = "0"
      } else
         sss = GloableVariable.Tag_distance
      val message = arrayOfNulls<String>(1)
      circularProgressView!!.visibility = View.VISIBLE
      val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
      //--------------maltipart image................
      for (i in listImages!!.indices) {
         val b = listImages!![i]
         builder.addFormDataPart("file", onCaptureImageResult(b).name, RequestBody.create(MEDIA_TYPE_PNG, onCaptureImageResult(b)))
      }
   
      builder.addFormDataPart("type_of_goods", typesOfGoodArray!!.toString())
       .addFormDataPart("helper", data!!)
       .addFormDataPart("no_of_helpers", helper_check.toString())
       .addFormDataPart("pickup_contact_name", GloableVariable.Tag_pickup_contact_name)
       .addFormDataPart("pickup_contact_number", GloableVariable.Tag_pickup_contact_number)
       .addFormDataPart("pickup_comments", GloableVariable.Tag_pickup_comments)
       .addFormDataPart("pickup_home_type", GloableVariable.Tag_pickup_home_type)
       .addFormDataPart("pickup_villa_name", GloableVariable.Tag_pickup_villa_no)
       .addFormDataPart("pickup_building_name", GloableVariable.Tag_pickup_building_name)
       .addFormDataPart("pickup_floor_number", GloableVariable.Tag_pickup_floor_number)
       .addFormDataPart("pickup_unit_number", GloableVariable.Tag_pickup_unit_number)
       .addFormDataPart("pickup_latitude", GloableVariable.Tag_pickup_latitude.toString())
       .addFormDataPart("pickup_longitude", GloableVariable.Tag_pickup_longitude.toString())
       .addFormDataPart("drop_latitude", GloableVariable.Tag_drop_latitude.toString())
       .addFormDataPart("drop_longitude", GloableVariable.Tag_drop_longitude.toString())
       .addFormDataPart("drop_location_address", GloableVariable.Tag_drop_location_address)
       .addFormDataPart("drop_contact_name", GloableVariable.Tag_drop_contact_name)
       .addFormDataPart("drop_contact_number", GloableVariable.Tag_drop_contact_number)
       .addFormDataPart("drop_comments", GloableVariable.Tag_drop_comments)
       .addFormDataPart("drop_home_type", GloableVariable.Tag_drop_home_type)
       .addFormDataPart("drop_villa_name", GloableVariable.Tag_drop_villa_no)
       .addFormDataPart("drop_building_name", GloableVariable.Tag_drop_building_name)
       .addFormDataPart("drop_floor_number", GloableVariable.Tag_drop_floor_number)
       .addFormDataPart("drop_unit_number", GloableVariable.Tag_drop_unit_number)
       .addFormDataPart("ride_description", GloableVariable.Tag_Good_Details_Description)
       .addFormDataPart("distance", sss)
       .addFormDataPart("pickup_time_type", GloableVariable.Tag_Good_Details_Comming_time_type)
       .addFormDataPart("pickup_time", GloableVariable.Tag_Good_Details_Comming_Date_time_Stamp.toString())
       .addFormDataPart("total_price", Total_prices.toString())
       .addFormDataPart("paid_by_type", GloableVariable.Tag_paid_by_type)
       .addFormDataPart("paid_by_name", GloableVariable.Tag_paid_by_name)
       .addFormDataPart("paid_by_contact_number", GloableVariable.paid_by_contact_number)
       .addFormDataPart("pickup_location_address",GloableVariable.Tag_pickup_location_address)
   
   
   
      apiService.postImage(User.getInstance().accesstoken,MultipartBody.Part.create(builder.build()))
       .enqueue(object : Callback<JSONObject>{
          override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>?) {
          
          }
   
          override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
          }
       })
       
       
   }
   


   


   private fun Clear_Preference() {
      val editor = Image_Sp!!.edit()
      editor.clear()
      editor.apply()
      val editor1 = Image_Sp1!!.edit()
      editor1.clear()
      editor1.apply()

      GloableVariable.Tag_drop_location_check = ""
      GloableVariable.Tag_pickup_location_address = ""

      GloableVariable.Tag_pickup_home_type = ""
      GloableVariable.Tag_pickup_villa_no = ""
      GloableVariable.Tag_pickup_building_name = ""
      GloableVariable.Tag_pickup_floor_number = ""
      GloableVariable.Tag_pickup_unit_number = ""





      GloableVariable.Tag_pickup_comments = ""



      GloableVariable.Tag_drop_location_address = ""



      GloableVariable.Tag_drop_home_type = ""
      GloableVariable.Tag_drop_villa_no = ""
      GloableVariable.Tag_drop_building_name = ""
      GloableVariable.Tag_drop_floor_number = ""
      GloableVariable.Tag_drop_unit_number = ""






      GloableVariable.Tag_drop_comments = ""




      GloableVariable.Tag_helper = ""
      GloableVariable.Tag_Good_Details_Description = ""

      GloableVariable.Tag_Good_Details_Comming_time_type = ""
      GloableVariable.Tag_Good_Details_Comming_Date_time = ""

      GloableVariable.Tag_type_of_goods = ""

      GloableVariable.Tag_distance = ""
      GloableVariable.Tag_total_price = ""
      GloableVariable.Tag_total_final_prices = ""
      GloableVariable.Tag_booking_id = ""
   }

 
}


