package com.fantasy.lulutong.activity.me;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;
import com.fantasy.lulutong.activity.MainActivity;
import com.fantasy.lulutong.activity.WelcomeActivity;
import com.fantasy.lulutong.util.ActivityCollector;
import com.fantasy.lulutong.util.GlobalVariable;
import com.fantasy.lulutong.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * “个人信息”模块，功能：查看和修改
 * @author Fantasy
 * @version 1.0, 2017-02-21
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;
    private RelativeLayout relativeEmail;
    private RelativeLayout relativePhone;
    private RelativeLayout relativeRealName;
    private RelativeLayout relativeCertificateType;
    private RelativeLayout relativeNum;
    private RelativeLayout relativeVerification;

    private TextView textName;
    private TextView textEmail;
    private TextView textPhone;
    private TextView textRealName;
    private TextView textCertificateType;
    private TextView textNum;
    private TextView textVerification;

    private StringRequest stringRequest;
    private SharedPreferences prefes;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_user_info_back);
        relativeEmail = (RelativeLayout) findViewById(R.id.relative_user_info_email);
        relativePhone = (RelativeLayout) findViewById(R.id.relative_user_info_phone);
        relativeRealName = (RelativeLayout) findViewById(R.id.relative_user_info_real_name);
        relativeCertificateType = (RelativeLayout) findViewById(R.id.relative_user_info_type);
        relativeNum = (RelativeLayout) findViewById(R.id.relative_user_info_num);
        relativeVerification = (RelativeLayout) findViewById(R.id.relative_user_info_verification);

        textName = (TextView) findViewById(R.id.text_user_info_name);
        textEmail = (TextView) findViewById(R.id.text_user_info_email);
        textPhone = (TextView) findViewById(R.id.text_user_info_phone);
        textRealName = (TextView) findViewById(R.id.text_user_info_real_name);
        textCertificateType = (TextView) findViewById(R.id.text_user_info_type);
        textNum = (TextView) findViewById(R.id.text_user_info_num);
        textVerification = (TextView) findViewById(R.id.text_user_info_verification);

        prefes = PreferenceManager.getDefaultSharedPreferences(UserInfoActivity.this);
        editor = prefes.edit();
        //http://主机名/LuLuTongManagementSystem/UserInfoServlet?type=null&name=null&value=null
        url = GlobalVariable.HOST + "/UserInfoServlet";

        relativeBack.setOnClickListener(this);
        relativeEmail.setOnClickListener(this);
        relativePhone.setOnClickListener(this);
        relativeRealName.setOnClickListener(this);
        relativeCertificateType.setOnClickListener(this);
        relativeNum.setOnClickListener(this);
        relativeVerification.setOnClickListener(this);

        initUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_user_info_back:
                finish();
                break;
            case R.id.relative_user_info_email:
                Intent intent = new Intent(this, UserInfoEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_user_info_phone:

                break;
            case R.id.relative_user_info_real_name:

                break;
            case R.id.relative_user_info_type:

                break;
            case R.id.relative_user_info_num:

                break;
            case R.id.relative_user_info_verification:

                break;
            default:
                break;
        }
    }

    /**
     * 发送请求给服务器，查看用户个人信息
     */
    private void initUserInfo() {
        progressDialog = ProgressDialog.show(UserInfoActivity.this, "", "正在加载...", false, false);

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getString("code")) {
                                case "0":
                                    JSONObject data = jsonObject.getJSONObject("data");

                                    editor.putString("email", data.getString("email"));
                                    editor.putString("phone", data.getString("phone"));
                                    editor.putString("real_name", data.getString("real_name"));
                                    editor.putString("certificate_type",
                                            data.getString("certificate_type"));
                                    editor.putString("certificate_num",
                                            data.getString("certificate_num"));
                                    editor.putString("verification",
                                            data.getString("verification"));
                                    editor.commit();

                                    textName.setText(prefes.getString("name", null));
                                    textEmail.setText(prefes.getString("email", null));
                                    textPhone.setText(prefes.getString("phone", null));
                                    textRealName.setText(prefes.getString("real_name", null));
                                    textCertificateType.setText(
                                            prefes.getString("certificate_type", null));
                                    textNum.setText(prefes.getString("certificate_num", null));
                                    textVerification.setText(prefes.getString("verification", null));

                                    //progressDialog.dismiss();
                                    break;
                                case "100": // 严重错误，获取不到合法权限，要求重新登录
                                    //progressDialog.dismiss();
                                    alertDialog = new AlertDialog.Builder(UserInfoActivity.this);
                                    alertDialog.setTitle("加载失败");
                                    alertDialog.setMessage(jsonObject.getString("message"));
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    editor.putString("name", null);
                                                    editor.putString("real_name", null);
                                                    editor.commit();
                                                    Intent intent = new Intent(UserInfoActivity.this,
                                                            WelcomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        alertDialog = new AlertDialog.Builder(UserInfoActivity.this);
                        alertDialog.setTitle("加载失败");
                        alertDialog.setMessage("网络异常！");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("确定", null);
                        alertDialog.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("type", "all");
                map.put("name", prefes.getString("name", null));
                map.put("value", prefes.getString("name", null));
                return map;
            }
        };
        VolleySingleton.getVolleySingleton(this.getApplicationContext())
                .addToRequestQueue(stringRequest);
    }

}
