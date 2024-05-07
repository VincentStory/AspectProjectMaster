package com.example.aspect.aspect;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.aspect.LoginActivity;
import com.example.aspect.MyApp;
import com.example.aspect.annotation.CheckLogin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @Description
 * @Author wangwenbo
 * @CreateTime 2024年05月07日 20:01:38
 */
@Aspect
public class CheckLoginAspectJ {
    private static final String TAG = "CheckLogin";
    /**
     * 找到处理的切点
     * * *(..)  可以处理CheckLogin这个类所有的方法
     */
    @Pointcut("execution(@com.example.aspect.annotation.CheckLogin * *(..))")
    public void executionCheckLogin() {
    }
    /**
     * 处理切面，executionCheckLogin()是指上面定义的方法名
     *
     * @param joinPoint
     * @return
     */
    @Around("executionCheckLogin()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i(TAG, "checkLogin: ");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        if (checkLogin != null) {
            Context context = (Context) joinPoint.getThis();
            if (MyApp.isLogin()) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                return joinPoint.proceed();
            } else {
                Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, LoginActivity.class));
                return null;
            }
        }
        return joinPoint.proceed();
    }
}
