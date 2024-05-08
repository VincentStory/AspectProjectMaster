package com.example.aspect.aspect;

// 专门处理权限的 Aspect

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;


import com.example.aspect.MyPermissionActivity;
import com.example.aspect.annotation.Permission;
import com.example.aspect.annotation.PermissionCancel;
import com.example.aspect.annotation.PermissionDenied;
import com.example.aspect.core.IPermission;
import com.example.aspect.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Aspect
public class PermissionAspect {

    //这里com.example.myapplication.annotation为项目包名
    @Pointcut
            ("execution(@com.example.aspect.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointActionMethod(Permission permission) {/* 方法内部不做任何事情，只为了@Pointcut服务*/}

    // 对方法环绕监听,pointActionMethod()是指上面定义的方法名，permission是指上面参数名
    @Around("pointActionMethod(permission)")
    public void aProceedingJoinPoint(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        Log.i("TAG", "before->" + point.getTarget().toString());
        // 先定义一个上下文操作环境
        Context context = null;

        final Object thisObject = point.getThis(); // 如果有兼容问题，thisObject == null

        // 给context 初始化
        if (thisObject instanceof Context) {
            context = (Context) thisObject;
        } else if (thisObject instanceof Fragment) {
            context = ((Fragment) thisObject).getActivity();
        }

        // 判断是否为null
        if (null == context || permission == null) {
            throw new IllegalAccessException("null == context || permission == null is null");
        }

        // 执行获取权限的逻辑
        requestPermission(context, permission, point,thisObject);
    }



    private void requestPermission(Context context, Permission permission,ProceedingJoinPoint point, Object thisObject) {
        final Context finalContext = context;

        MyPermissionActivity.requestPermissionAction
                (context, permission.value(), permission.requestCode(), new IPermission() {
                    @Override
                    public void ganted() { // 申请成功 授权成功
                        // 让被 @Permission 的方法 正常的执行下去
                        try {
                            point.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void cancel() { // 被拒绝
                        // 调用到 被 @PermissionCancel 的方法
                        invokeAnnotation(thisObject, PermissionCancel.class);
                    }

                    @Override
                    public void denied() { // 严重拒绝 勾选了 不再提醒
                        // 调用到 被 @PermissionDenied 的方法
                        invokeAnnotation(thisObject, PermissionDenied.class);

                        // 不仅仅要提醒用户，还需要 自动跳转到 手机设置界面
                        PermissionUtils.startAndroidSettings(finalContext);
                    }
                });
    }


    /**
     * 专门去 callback invoke ---》 MainActivity  被注解的方法
     * 通过反射机制调用被注解标记的函数
     *
     * @param object
     * @param annotationClass
     */
    public static void invokeAnnotation(Object object, Class annotationClass) {
        // 获取 object 的 Class对象
        Class<?> objectClass = object.getClass();

        // 遍历 所有的方法
        Method[] methods = objectClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true); // 让虚拟机，不要去检测 private

            // 判断方法 是否有被 annotationClass注解的方法
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);

            if (annotationPresent) {
                // 当前方法 代表包含了 annotationClass注解的
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
