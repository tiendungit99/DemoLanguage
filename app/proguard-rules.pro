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

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Annotation, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable, *Annotation*

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions.*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Prevent messing with Model when working with Gson
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation interface com.google.gson.annotations.SerializedName

#Google Ima
-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }

-keep class com.google.ads.interactivemedia.** { *; }
-keep interface com.google.ads.interactivemedia.** { *; }

-keep class com.fptplay.ads.** { *; }

-keep class com.drowsyatmidnight.haint.** { *; }
-keep interface com.drowsyatmidnight.haint.** { *; }


-keep class com.fplay.ads.logo_instream.** { *; }
-keep interface com.fplay.ads.logo_instream.** { *; }


# Crashlytics
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#WorkManager
-dontwarn android.app.job.JobParameters
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.GcmAvailableHelper
-dontwarn com.evernote.android.job.work.**
-dontwarn com.evernote.android.job.WorkManagerAvailableHelper

#Support design library
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

## Android architecture components: Lifecycle
-keepattributes *Annotation*
-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}
-keep class * implements androidx.lifecycle.LifecycleObserver {
}
-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}
-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.ViewModel {
    <init>(androidx.lifecycle.SavedStateHandle);
}
-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application,androidx.lifecycle.SavedStateHandle);
}
# Work Manager
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
# Keep all constructors on ListenableWorker, Worker (also marked with @Keep)
-keep public class * extends androidx.work.ListenableWorker {
    public <init>(...);
}
# We need to keep WorkerParameters for the ListenableWorker constructor
-keep class androidx.work.WorkerParameters

-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }
-keep interface kotlinx.coroutines.** { *; }

-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }

-dontwarn com.google.common.**
-keep class com.google.common.** { *; }
-keep interface com.google.common.** { *; }

-dontwarn com.android.installreferrer

# Google Play Billing
-keep class com.android.vending.billing.**

# Keep class names of Hilt injected ViewModels since their name are used as a multibinding map key.
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

-keep class kotlin.Metadata { *; }

-keepclassmembers class com.xhbadxx.projects.module.domain.entity.fplay.common.Config {
  <init>(...);
  <fields>;
}

-keepclassmembers class org.json.** { *; }


#Sigma
-keep class com.sigma.packer.* { *; }
-dontwarn com.sigma.packer.**
#Connect SDK
-keep class com.connectsdk.**       { * ; }


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn logcat.LogPriority
-dontwarn logcat.LogcatLogger$Companion
-dontwarn logcat.LogcatLogger
-dontwarn org.koin.core.KoinComponent
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn sun.security.x509.X509Key

-keep class com.google.android.exoplayer2.audio.DefaultAudioSink.* { *; }
-keep interface com.google.android.exoplayer2.audio.DefaultAudioSink.* { *; }
-dontwarn com.google.android.exoplayer2.audio.DefaultAudioSink.**
-keep class com.fpl.** { *; }
-keep interface com.fpl.** { *; }
-dontwarn com.fpl.**
-keepclassmembers class * implements com.fpl.* { *; }
-keepclassmembers interface com.fpl.* { *; }

#-keep class com.fpl.base.helpers.LocaleManager { *; }
#-dontwarn com.fpl.base.helpers.LocaleManager

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
