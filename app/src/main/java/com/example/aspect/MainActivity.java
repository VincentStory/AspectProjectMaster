package com.example.aspect;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aspect.annotation.CheckLogin;
import com.example.aspect.annotation.Permission;
import com.example.aspect.annotation.PermissionCancel;
import com.example.aspect.annotation.PermissionDenied;

// TODO 用户使用角度
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void permissionRequestTest(View view) {
        testRequest();
    }

    @Permission(value = Manifest.permission.READ_EXTERNAL_STORAGE, requestCode = 200)
    public void testRequest() {
        Toast.makeText(this, "权限申请成功...", Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void testCancel() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void testDenied() {
        Toast.makeText(this, "权限被拒绝（用户勾选了，不再提醒）", Toast.LENGTH_SHORT).show();
    }


    public void loginClick(View view) {
       checkLogin("参数1","参数2");
    }

    @CheckLogin(title = "登录检查", isSaveRequestData = false)
    public void checkLogin(String str, String str2) {
        Log.i("tag","判断是否登录");
    }
}
