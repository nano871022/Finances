# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes Signature, RuntimeVisibleAnnotations, AnnotationDefault
-keepattributes EnclosingMethod
-keepattributes InnerClasses
#-dontoptimize

-if class androidx.credentials.CredentialManager {*;}
-keep class androidx.credentials.playservices.** {*;}

-keep public class * implements android.os.Parcelable{*;}
-keep public class * implements com.google.api.client.http.HttpRequestInitializer{}

-keep class * extends com.google.api.client.json.** {*;}
-keep class * extends com.google.api.client.util.** {*;}
-keep class * extends com.google.api.services.drive.** {*;}

-keep class com.google.googlesignin.**{ *;}
-keepnames class com.google.googlesignin.* {*;}

-keep class com.google.firebase.** {*;}
-keepclasseswithmembers class com.google.firebase.FirebaseException{ *; }

-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}

-keep class androidx.security.crypto.** { *; }
-keep class androidx.security.identitycredential.** { *; }
-keepnames class androidx.security.crypto.** {*;}
-keepnames class androidx.security.identitycredential.** {*;}


-keep public class * implements com.google.api.client.http.HttpRequestInitializer { *; }
-keep public class * implements com.google.api.client.json.JsonFactory { *; }
-keep public class * extends com.google.api.client.http.javanet.NetHttpTransport { *; }
-keep public class * implements com.google.api.client.http.LowLevelHttpResponse { *; }
-keep public class * implements com.google.api.client.http.HttpResponseException { *; }

-keep class com.google.android.gms.common.** {*;}
-keep class com.google.android.gms.auth.** {*;}
-keep class com.google.android.gms.internal.** {*;}
-keep class com.google.android.gms.** {*;}

-dontwarn com.google.**
-dontwarn com.google.api.client.extensions.android.**
-dontwarn com.google.api.client.googleapis.extensions.android.**
-dontwarn com.google.android.gms.**
-dontnote java.nio.file.Files, java.nio.file.Path
-dontnote **.ILicensingService
-dontnote sun.misc.Unsafe
-dontwarn sun.misc.Unsafe

-keepclassmembers class **.R$*{
 public static <fields>;
}
-keep class **.R$*

-keep class com.fasterxml.** {*;}

# WorkManager and Room database implementation protection for AGP 9
-keep class androidx.work.impl.** { *; }
-keep class androidx.room.MultiInstanceInvalidationService { *; }
-dontwarn androidx.work.impl.**

# Specific implementation classes and Worker persistence
-keep class androidx.work.impl.WorkDatabase_Impl { *; }
-keep class androidx.work.impl.background.systemjob.SystemJobService { *; }
-keep class * extends androidx.work.Worker { *; }
-keep class * extends androidx.work.ListenableWorker { *; }

# App Startup persistence to prevent obfuscation breaks on start
-keep class androidx.startup.** { *; }
-keep class androidx.startup.InitializationProvider
-keep class * implements androidx.startup.Initializer

# Room/SQLite internal database implementation (Android 15 and AGP 9 compatibility)
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class androidx.work.impl.db.WorkDatabase_Impl { *; }
-keep class androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory