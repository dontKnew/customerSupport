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

# Keep classes and members of the AppCompat library
-keep class androidx.appcompat.** { *; }

# Keep classes and members of the Material Design library
-keep class com.google.android.material.** { *; }

# Keep testing related classes and members
-keep class junit.** { *; }
-keep class androidx.test.** { *; }
-keep class androidx.test.ext.junit.** { *; }
-keep class androidx.test.espresso.core.** { *; }
