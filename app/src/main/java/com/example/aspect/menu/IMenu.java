package com.example.aspect.menu;

import android.content.Context;
import android.content.Intent;

/**
 * 抽象接口 通过不同的实现类来适配不同手机跳转权限设置页面
 */
public interface IMenu {

    Intent getStartActivity(Context context);

}
