# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# 混淆基本设置
-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose
-dontskipnonpubliclibraryclasses
-dontpreverify

# 优化项
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# android官方推荐系统组件不混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

# v4包引入,v7包里面的recyclerView
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.** { *; }
-dontwarn android.support.*

# 不混淆bean
-keep class com.myticket.model.**{*;}
-keep class com.train.model.**{*;}

# 不混淆枚举
-keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}

# 自定义View不混淆
-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
public void get*(...);
}

# onclick事件不混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 序列化不混淆
-keep public class * implements java.io.Serializable {
public *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# R文件不混淆
-keep class **.R$* { *; }

# 反射不混淆
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

# 本地方法不混淆
-keepclasseswithmembernames class * {
 native <methods>;
}


# 第三方jar包不混淆 

# alipay 
-keep class com.alipay.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-dontwarn com.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn con.ut.device.**

# 百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

# EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# greendao数据库
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# 个推
-dontwarn com.igexin.**
-keep class com.igexin.** {*;}

# apache,httpmime
-keep class org.apache.http.entity.mime.** {*;}

# tencent
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}

# 动画库nineoldandroids
-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}

# QQ支付
-dontwarn com.tenpay.paybyqq.*
-keep class com.tenpay.paybyqq.**{*;}

# sharedsdk,mob
-keep class com.mob.commons.**{*;}
-keep class com.mob.tools.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class m.framework.**{*;}
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class com.sharesdk.share.onekey.theme.classic.OneKeySharePage

# ImageLoader
-keep class com.nostra13.universalimageloader.**{*;}

# 银联支付
-keep class com.unionpay.**{*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.**{*;}

# org.apache.http.legacy
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

#友盟 不混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}