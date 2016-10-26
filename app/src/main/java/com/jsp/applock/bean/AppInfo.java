package com.jsp.applock.bean;

import android.graphics.drawable.Drawable;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

public class AppInfo {
    // 每个对象需要有一个主键
    @PrimaryKey(AssignType.BY_MYSELF)
    public String packagename = "";//包
    public int versionCode = 0; //版本（程序）
    public String appname = "";//名称
    public String versionName = "";//版本（用户可见）
    public Drawable appicon = null;//图标
    public boolean islock = false;
}