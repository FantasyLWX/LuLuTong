package com.fantasy.lulutong.activity.me;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
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
import com.fantasy.lulutong.activity.MainActivity;
import com.fantasy.lulutong.util.ActivityCollector;
import com.fantasy.lulutong.util.GlobalVariable;
import com.fantasy.lulutong.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电子邮箱修改模块
 * @author Fantasy
 * @version 1.0, 2017-02-22
 */
public class UserInfoEmailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;
    private EditText editEmail;
    private Button btnSave;

    private StringRequest stringRequest;
    private SharedPreferences prefes;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_info_email);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_alter_email_back);
        editEmail = (EditText) findViewById(R.id.edit_alter_email);
        btnSave = (Button) findViewById(R.id.btn_alter_email);

        prefes = PreferenceManager.getDefaultSharedPreferences(UserInfoEmailActivity.this);
        editor = prefes.edit();
        editEmail.setText(prefes.getString("email", null));
        // 当有修改电子邮箱才可以保存
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editEmail.getText().toString().equals(prefes.getString("email", null))) {
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
            case R.id.relative_alter_email_back:
                finish();
                break;
            case R.id.btn_alter_email:
                String email = editEmail.getText().toString();
                if (!isEmail(email) || email.length() > 30) {
                    editEmail.requestFocus();
                    Toast.makeText(UserInfoEmailActivity.this, "请输入有效的电子邮箱",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                saveEmail();
                break;
            default:
                break;
        }
    }

    /**
     * 判断电子邮箱的格式是否正确
     * @param email 电子邮箱
     * @return 如果正确的话，则返回true，否则返回false
     */
    public boolean isEmail(String email) {
        String str = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 连接服务器，保存修改的电子邮箱
     */
    private void saveEmail() {
        progressDialog = ProgressDialog.show(UserInfoEmailActivity.this, "",
                "正在保存...", false, false);

        String url = GlobalVariable.HOST + "/UserInfoServlet";

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                ActivityCollector.finishPreviousOne();
                                Intent intent = new Intent(UserInfoEmailActivity.this,
                                        UserInfoActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                alertDialog = new AlertDialog.Builder(UserInfoEmailActivity.this);
                                alertDialog.setTitle("保存失败");
                                alertDialog.setMessage(jsonObject.getString("message"));
                                alertDialog.setCancelable(false);
                                alertDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editor.putString("name", null);
                                                editor.putString("real_name", null);
                                                editor.commit();
                                                Intent intent = new Intent(UserInfoEmailActivity.this,
                                                        MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
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
                        alertDialog = new AlertDialog.Builder(UserInfoEmailActivity.this);
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
                map.put("type", "name");
                map.put("name", prefes.getString("name", null));
                return map;
            }
        };
        VolleySingleton.getVolleySingleton(this.getApplicationContext())
                .addToRequestQueue(stringRequest);
    }

}
