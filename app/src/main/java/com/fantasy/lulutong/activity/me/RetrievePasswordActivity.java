package com.fantasy.lulutong.activity.me;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.fantasy.lulutong.util.GlobalVariable;
import com.fantasy.lulutong.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * “找回密码”模块
 * @author Fantasy
 * @version 1.0, 2017-02-21
 */
public class RetrievePasswordActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;
    private EditText editName;
    private EditText editRealName;
    private LinearLayout linearType;
    private TextView textCertificateType;
    private EditText editNum;
    private EditText editVerification;
    private Button btnRetrieve;

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;

    private String url;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_retrieve_password);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_retrieve_password_back);
        editName = (EditText) findViewById(R.id.edit_retrieve_name);
        editRealName = (EditText) findViewById(R.id.edit_retrieve_real_name);
        linearType = (LinearLayout) findViewById(R.id.linear_retrieve_type);
        textCertificateType = (TextView) findViewById(R.id.text_retrieve_type);
        editNum = (EditText) findViewById(R.id.edit_retrieve_num);
        editVerification = (EditText) findViewById(R.id.edit_retrieve_verification);
        btnRetrieve = (Button) findViewById(R.id.btn_retrieve);

        relativeBack.setOnClickListener(this);
        linearType.setOnClickListener(this);
        btnRetrieve.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_retrieve_password_back:
                finish();
                break;
            case R.id.linear_retrieve_type: // 选择证件类型
                final String[] items = new String[]{ "二代身份证", "港澳通行证", "台湾通行证", "护照" };
                alertDialog = new AlertDialog.Builder(RetrievePasswordActivity.this);
                alertDialog.setTitle("请选择证件类型：");
                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textCertificateType.setTextColor(0xff000000);
                        textCertificateType.setText(items[which]);
                    }
                });
                alertDialog.show();
                break;
            case R.id.btn_retrieve:
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    editName.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "请输入用户名",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editName.getText().toString().length() > 20) {
                    editName.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "用户名的长度不能大于20位",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(editRealName.getText().toString())) {
                    editRealName.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "请输入真实姓名",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editRealName.getText().toString().length() > 20) {
                    editRealName.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "真实姓名的长度不能大于20位",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (textCertificateType.getText().equals("证件类型")) {
                    Toast.makeText(RetrievePasswordActivity.this, "请选择证件类型",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(editNum.getText().toString())) {
                    editNum.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "请输入证件号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editNum.getText().toString().length() > 20) {
                    editNum.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "请输入有效的证件号码",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(editVerification.getText().toString())) {
                    editVerification.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "请输入预留验证信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editVerification.getText().toString().length() > 30) {
                    editVerification.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "预留验证信息不能超过30个字符",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = ProgressDialog.show(RetrievePasswordActivity.this, "",
                        "正在处理...", false, false);

                url = GlobalVariable.HOST + "/UserPasswordServlet";

                stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("code").equals("0")) {
                                        alertDialog = new AlertDialog.Builder(
                                                RetrievePasswordActivity.this);
                                        alertDialog.setTitle("验证成功");
                                        alertDialog.setMessage(jsonObject.getString("message"));
                                        alertDialog.setCancelable(false);
                                        alertDialog.setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        alertDialog.show();
                                    } else {
                                        alertDialog = new AlertDialog.Builder(
                                                RetrievePasswordActivity.this);
                                        alertDialog.setTitle("验证失败");
                                        alertDialog.setMessage(jsonObject.getString("message"));
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
                                alertDialog = new AlertDialog.Builder(RetrievePasswordActivity.this);
                                alertDialog.setTitle("验证失败");
                                alertDialog.setMessage("服务器异常！");
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("确定", null);
                                alertDialog.show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("type", "retrieve");
                        map.put("name", editName.getText().toString());
                        map.put("real_name", editRealName.getText().toString());
                        map.put("certificate_type", textCertificateType.getText().toString());
                        map.put("certificate_num", editNum.getText().toString());
                        map.put("verification", editVerification.getText().toString());
                        return map;
                    }
                };
                //将StringRequest对象添加进RequestQueue请求队列中
                VolleySingleton.getVolleySingleton(this.getApplicationContext())
                        .addToRequestQueue(stringRequest);
                break;
            default:
                break;
        }
    }

}
