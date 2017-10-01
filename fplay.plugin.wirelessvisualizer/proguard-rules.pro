# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

#
# http://developer.android.com/tools/help/proguard.html
# https://www.guardsquare.com/en/proguard/manual/usage#obfuscationoptions
# https://www.guardsquare.com/en/proguard/manual/optimizations
# https://www.guardsquare.com/en/proguard/manual/examples
# http://proguard.sourceforge.net/index.html#manual/examples.html
#

# **********************************************************************************************************
# I made the following changes to the file sdk/tools/proguard/proguard-android-optimize.txt
#
# removed -optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# added -optimizations !code/simplification/cast,!field/*,!class/merging/*
# **********************************************************************************************************
-printmapping build/outputs/mapping/mapping.txt
-optimizationpasses 9
-useuniqueclassmembernames
-allowaccessmodification
-repackageclasses ''

-keep public interface br.com.carlosrafaelgn.fplay.plugin.Visualizer
-keepclassmembers interface br.com.carlosrafaelgn.fplay.plugin.Visualizer {
    public *;
}

-keep class * implements br.com.carlosrafaelgn.fplay.plugin.Visualizer

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * {
    native <methods>;
}

-keep public interface br.com.carlosrafaelgn.fplay.plugin.FPlay
-keepclassmembers interface br.com.carlosrafaelgn.fplay.plugin.FPlay {
    public *;
}

-keep public interface br.com.carlosrafaelgn.fplay.plugin.FPlayPlugin
-keepclassmembers interface br.com.carlosrafaelgn.fplay.plugin.FPlayPlugin {
    public *;
}

-keep public interface br.com.carlosrafaelgn.fplay.plugin.VisualizerService
-keepclassmembers interface br.com.carlosrafaelgn.fplay.plugin.VisualizerService {
    public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile