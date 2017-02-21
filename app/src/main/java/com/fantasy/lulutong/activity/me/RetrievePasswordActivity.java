package com.fantasy.lulutong.activity.me;

import android.app.AlertDialog;
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

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;

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
                    Toast.makeText(RetrievePasswordActivity.this, "请输入验证信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editVerification.getText().toString().length() > 30) {
                    editVerification.requestFocus();
                    Toast.makeText(RetrievePasswordActivity.this, "验证信息不能超过30个字符",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                break;
            default:
                break;
        }
    }

}
