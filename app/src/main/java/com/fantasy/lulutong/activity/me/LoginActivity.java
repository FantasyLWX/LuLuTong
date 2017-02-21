package com.fantasy.lulutong.activity.me;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;
import com.fantasy.lulutong.activity.MainActivity;
import com.fantasy.lulutong.util.ActivityCollector;
import com.fantasy.lulutong.util.GlobalVariable;
import com.fantasy.lulutong.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

/**
 * “登录”模块
 * @author Fantasy
 * @version 1.0, 2017-02-20
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;
    private EditText editName;
    private EditText editPassword;
    private Button btnLogin;
    private TextView textForgetPassword;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private SharedPreferences.Editor editor;

    private String url;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_login_back);
        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        textForgetPassword = (TextView) findViewById(R.id.text_forget_password);
        editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();

        relativeBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        textForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_login_back:
                finish();
                break;
            case R.id.btn_login: // 登录模块
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    editName.requestFocus();
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.requestFocus();
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(LoginActivity.this, "",
                        "正在登录...", false, false);

                url = GlobalVariable.HOST + "/UserLoginServlet";

                stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("code").equals("0")) {
                                        JSONObject data = jsonObject.getJSONObject("data");

                                        editor.putString("name", data.getString("name"));
                                        editor.putString("password", data.getString("password"));
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

                                        Intent intent = new Intent(LoginActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        alertDialog = new AlertDialog.Builder(
                                                LoginActivity.this);
                                        alertDialog.setTitle("登录失败");
                                        alertDialog.setMessage(
                                                jsonObject.getString("message"));
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("确定", null);
                                        alertDialog.show();
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
                                alertDialog = new AlertDialog.Builder(LoginActivity.this);
                                alertDialog.setTitle("登录失败");
                                alertDialog.setMessage("服务器异常！");
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("确定", null);
                                alertDialog.show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put("name", editName.getText().toString());
                        map.put("password", editPassword.getText().toString());
                        return map;
                    }
                };
                //将StringRequest对象添加进RequestQueue请求队列中
                VolleySingleton.getVolleySingleton(this.getApplicationContext())
                        .addToRequestQueue(stringRequest);
                break;
            case R.id.text_forget_password: // 忘记密码
                Intent intent = new Intent(LoginActivity.this, RetrievePasswordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
