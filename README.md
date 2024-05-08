# AspectProjectMaster

- 通过此项目可以熟悉Aspect用法以及注解和反射的使用方式

## 权限申请用法
```

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

```
## 登录检查用法
```
@CheckLogin
public void loginCheck(View view) {
    Log.i("tag","判断是否登录");
}

```
