package com.app.bickup_user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.controller.NetworkCallBack;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.model.User;
import com.app.bickup_user.pic.SanImagePicker;
import com.app.bickup_user.pic.Sources;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import io.reactivex.functions.Consumer;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, NetworkCallBack {


    private Activity mActivityReference;
    private EditText text_enter_first_name;
    private EditText text_enter_last_name;
    private EditText edtMobileNumber;
    private EditText edtEmailID;
    private Button btnSave;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private TextView tv_header;
    private ImageView imgBack;
    private ImageView userImage;
    private CircularProgressView circularProgressView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private File imgFile;
    private SharedPreferences sharedPreferences;
    private Dialog selectImageDailog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        InitializeViews();
    }


    private void InitializeViews() {
        mActivityReference = this;
        circularProgressView = (CircularProgressView) findViewById(R.id.progress_view);
        tv_header = (TextView) findViewById(R.id.txt_activty_header);
        tv_header.setText(getResources().getString(R.string.txt_edt_profile));

        userImage = (ImageView) findViewById(R.id.userImage);
        imgBack = (ImageView) findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);


        text_enter_first_name = (EditText) findViewById(R.id.edt_first_name);
        text_enter_last_name = (EditText) findViewById(R.id.edt_last_name);

        edtMobileNumber = (EditText) findViewById(R.id.edt_mobile_number_profile);
        edtEmailID = (EditText) findViewById(R.id.edt_email_profile);
        btnSave = (Button) findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(this);

        text_enter_first_name.setTypeface(mTypefaceRegular);
        text_enter_last_name.setTypeface(mTypefaceRegular);

        edtMobileNumber.setTypeface(mTypefaceRegular);
        userImage.setOnClickListener(this);

        edtEmailID.setTypeface(mTypefaceRegular);
        btnSave.setTypeface(mTypefaceRegular);

        text_enter_first_name.setText(User.getInstance().getFirstName());
        text_enter_last_name.setText(User.getInstance().getLastName());
        edtMobileNumber.setText(User.getInstance().getMobileNumber());
        edtEmailID.setText(User.getInstance().getEmail());
        if (User.getInstance().getUserImage() != null) {
            Ion.with(userImage)
                    .placeholder(R.drawable.driver)
                    .error(R.drawable.driver)
                    .load(User.getInstance().getUserImage());
        }
    }

    private boolean validateFields() {
        if (!CommonMethods.getInstance().validateEditFeild(text_enter_first_name.getText().toString())) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_name), Toast.LENGTH_SHORT).show();
            return false;
        }
      if (!CommonMethods.getInstance().validateEditFeild(text_enter_last_name.getText().toString())) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_name_last), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CommonMethods.getInstance().validateMobileNumber(edtMobileNumber.getText().toString(), 6)) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonMethods.getInstance().validateEmailAddress(edtEmailID.getText().toString())) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_emailID), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void prepareuserLogin(final String firstname, final String lastname, final String email) {
        String createUserUrl = WebAPIManager.getInstance().getimageUrl();
        final JsonObject requestBody = new JsonObject();
        editProfile(requestBody, createUserUrl, this, 60 * 1000, 100, firstname, lastname, email);
    }

    private void editProfile(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, String firstname, String lastname, String email) {
        final String[] message = new String[1];
        circularProgressView.setVisibility(View.VISIBLE);
        if (imgFile != null) {
            Ion.with(this)
                    .load("PUT", createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setMultipartFile("file", imgFile)
                    .setMultipartParameter(ConstantValues.USER_FIRSTNAME, firstname)
                    .setMultipartParameter(ConstantValues.USER_LASTNAME, lastname)
                    .setMultipartParameter(ConstantValues.USER_EMAILADDRESS, email)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressView.setVisibility(View.GONE);
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int status = result.getHeaders().code();
                            JsonObject resultObject = result.getResult();
                            String value = String.valueOf(resultObject);
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                message[0] = jsonObject.getString("message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            switch (status) {
                                case 422:
                                    Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                    break;
                                case 400:
                                    Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                case 202:
                                    loginActivity.onSuccess(resultObject, requestCode, status);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Ion.with(this)
                    .load("PUT", createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setMultipartParameter(ConstantValues.USER_FIRSTNAME, firstname)
                    .setMultipartParameter(ConstantValues.USER_LASTNAME, lastname)
                    .setMultipartParameter(ConstantValues.USER_EMAILADDRESS, email)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressView.setVisibility(View.GONE);
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int status = result.getHeaders().code();
                            JsonObject resultObject = result.getResult();
                            switch (status) {
                                case 422:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_user_exist), Toast.LENGTH_SHORT).show();
                                    break;
                                case 400:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                case 202:
                                     loginActivity.onSuccess(resultObject, requestCode, status);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save_profile:
                if (validateFields()) {
                    prepareuserLogin(text_enter_first_name.getText().toString(), text_enter_last_name.getText().toString(), edtEmailID.getText().toString());
                }
                break;
            case R.id.backImage_header:
                finish();
                break;
            case R.id.userImage:
                selectImage();
                break;
        }
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode) {
            case 100:
                ParseuserLoginResponse parseuserLoginResponse = new ParseuserLoginResponse();
             try {
                 parseuserLoginResponse.execute(String.valueOf(data));
             }catch (Exception e){}

                break;

        }
    }

    class ParseuserLoginResponse extends AsyncTask<String, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("response");
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject jsonObject1 = data.getJSONObject("profile_image");
                String imageUrl = jsonObject1.getString("image_url");
                map.put(ConstantValues.USER_EMAILADDRESS, data.getString("email"));
                map.put(ConstantValues.USER_MOBILENUMBER, data.getString("phone_number"));
                map.put(ConstantValues.USER_ID, data.getString("user_id"));
                map.put(ConstantValues.USER_ACCESS_TOKEN, data.getString("access_token"));
                map.put(ConstantValues.USER_FIRSTNAME, data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME, data.getString("last_name"));
                map.put(ConstantValues.COUNTRY_CODE, data.getString("country_code"));
                map.put(ConstantValues.USER_IMAGE, imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag = hashMap.get("flag");
            String message = hashMap.get("message");
            if (!flag.equalsIgnoreCase("2")) {
                if (hashMap.size() > 2) {
                    User.getInstance().updateProfile(EditProfileActivity.this, hashMap.get(ConstantValues.USER_EMAILADDRESS), hashMap.get(ConstantValues.USER_FIRSTNAME), hashMap.get(ConstantValues.USER_LASTNAME), ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE));
                } else {
                    User.getInstance().updateProfile(EditProfileActivity.this, edtEmailID.getText().toString().trim(), text_enter_first_name.getText().toString().trim(), text_enter_last_name.getText().toString().trim(), User.getInstance().getUserImage());
                }
            }

            Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(mActivityReference, message, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onError(String msg) {

    }

//-----------------------------------------------------
    private  void onImagePicked(Object result) {

        if (result instanceof Bitmap) {
            userImage.setImageBitmap((Bitmap) result);
        } else {
            Glide.with(getApplicationContext())
                    .load(result)
                    .into(userImage);

        }
    }

//---------------------------------------------------------------------
    public void selectImage() {
        selectImageDailog = new Dialog(this);
        selectImageDailog.setContentView(R.layout.edit_image_dialog);
        selectImageDailog.setCancelable(true);
        selectImageDailog.setCanceledOnTouchOutside(false);
        LinearLayout camera = selectImageDailog.findViewById(R.id.camera);
        final LinearLayout gallery = selectImageDailog.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromSource(Sources.CAMERA);
                selectImageDailog.dismiss();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  pickImageFromSource(Sources.GALLERY);
                 selectImageDailog.dismiss();
            }
        });

        selectImageDailog.show();
    }

    public void pickImageFromSource(Sources source) {
        SanImagePicker.with(getApplicationContext()).requestImage(source).subscribe(new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) throws Exception {
                onImagePicked(uri);
                String path = SanImagePicker.getPath(getApplicationContext(), uri);
                imgFile = new File(path);
            }
        });
    }
    //-------------------------------------------------------------------------
}
