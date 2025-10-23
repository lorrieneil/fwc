# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep ML Kit classes
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

-dontwarn com.google.mlkit.**
-dontwarn com.google.android.gms.**
