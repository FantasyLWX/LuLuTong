package com.fantasy.lulutong.activity.me;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;
import com.fantasy.lulutong.activity.WelcomeActivity;
import com.fantasy.lulutong.util.ActivityCollector;
import com.fantasy.lulutong.util.GlobalVariable;
import com.fantasy.lulutong.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证信息修改模块
 * @author Fantasy
 * @version 1.0, 2017-02-22
 */
public class UserInfoVerificationActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;
    private EditText editVerification;
    private Button btnSave;

    private SharedPreferences prefes;
    private SharedPreferences.Editor editor;
    private StringRequest stringRequest;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info_verification);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_alter_verification_back);
        editVerification = (EditText) findViewById(R.id.edit_alter_verification);
        btnSave = (Button) findViewById(R.id.btn_alter_verification);

        prefes = PreferenceManager.getDefaultSharedPreferences(UserInfoVerificationActivity.this);
        editor = prefes.edit();
        editVerification.setText(prefes.getString("verification", null));
        // 当有修改才可以保存
        editVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editVerification.getText().toString().equals(
                        prefes.getString("verification", null))) {
                    btnSave.setEnabled(false);
                } else {
                    btnSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        relativeBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_alter_verification_back:
                finish();
                break;
            case R.id.btn_alter_verification:
                if (verify()) {
                    saveVerification();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证输入的信息是否合法
     * @return 如果合法，则返回true，否则返回false
     */
    private boolean verify() {
        if (TextUtils.isEmpty(editVerification.getText().toString())) {
            editVerification.requestFocus();
            Toast.makeText(UserInfoVerificationActivity.this, "请输入预留验证信息",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editVerification.getText().toString().length() > 30) {
            editVerification.requestFocus();
            Toast.makeText(UserInfoVerificationActivity.this, "预留验证信息不能超过30个字符",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 发送请求给服务器，保存修改的验证信息
     */
    private void saveVerification() {
        progressDialog = ProgressDialog.show(UserInfoVerificationActivity.this, "",
                "正在保存...", false, false);
        //http://主机名/LuLuTongManagementSystem/UserInfoServlet?type=null&name=null&value=null
        url = GlobalVariable.HOST + "/UserInfoServlet";

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getString("code")) {
                                case "0": // 保存成功
                                    ActivityCollector.finishPreviousOne();
                                    Intent intent = new Intent(UserInfoVerificationActivity.this,
                                            UserInfoActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "100": // 严重错误，获取不到合法权限，要求重新登录
                                    alertDialog = new AlertDialog.Builder(
                                            UserInfoVerificationActivity.this);
                                    alertDialog.setTitle("保存失败");
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
                                                    Intent intent = new Intent(
                                                            UserInfoVerificationActivity.this,
                                                            WelcomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    alertDialog.show();
                                    break;
                                case "101":  // 普通错误，仅仅提示而已
                                    alertDialog = new AlertDialog.Builder(
                                            UserInfoVerificationActivity.this);
                                    alertDialog.setTitle("保存失败");
                                    alertDialog.setMessage(jsonObject.getString("message"));
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("确定", null);
                                    alertDialog.show();
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
                        alertDialog = new AlertDialog.Builder(UserInfoVerificationActivity.this);
                        alertDialog.setTitle("保存失败");
                        alertDialog.setMessage("网络异常！");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("确定", null);
                        alertDialog.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("type", "verification");
                map.put("name", prefes.getString("name", null));
                map.put("value", editVerification.getText().toString());
                return map;
            }
        };
        VolleySingleton.getVolleySingleton(this.getApplicationContext())
                .addToRequestQueue(stringRequest);
    }

}
