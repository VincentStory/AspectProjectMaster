package com.example.aspect.aspect;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.example.aspect.LoginActivity;
import com.example.aspect.MyApp;
import com.example.aspect.annotation.CheckLogin;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect // 定义切面类
public class LoginCheckAspect {

    private final static String TAG = "aop_tag >>> ";

    // 1、应用中用到了哪些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution，以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.example.aspect.annotation.CheckLogin * *(..))")
    public void pointCut() {
    }

    // 2、处理切面，pointCut()是指上面定义的方法名
    @Around("pointCut()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable {


        //1.获取切入点所在目标对象
        Object targetObj = joinPoint.getTarget();
        Log.e(TAG, "类名：" + targetObj.getClass().getName());
        // 2.获取切入点方法的名字
        String methodName = joinPoint.getSignature().getName();
        Log.e(TAG, "切入方法名字：" + methodName);
        // 3. 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);

        if (checkLogin != null) {
            Log.e(TAG, "切入方法注解的title:" + checkLogin.title());
        }

        //4. 获取方法的参数
        Object[] args = joinPoint.getArgs();
        for (Object o : args) {
            Log.e(TAG, "切入方法的参数：" + o);
        }


        Context context = (Context) joinPoint.getThis();
        if (MyApp.isLogin()) {
            Toast.makeText(context, "已经登录！", Toast.LENGTH_SHORT).show();
            return joinPoint.proceed();
        } else {
            Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return null; // 不再执行方法（切入点）
        }
    }
}
