# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pyamsoft/dev/sdk/android/tools/proguard/proguard-android.txt
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

# We are open source, we don't need obfuscation.
# We will still use optimizations though
-dontobfuscate

# Don't obfuscate causes the gradle build to fail after the optimization step
# The addition of !code/allocation/variable is needed to prevent this
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable

# RetroLambda
# Lint will spit out a large amount of errors with parsing. These can safely be ignored.
-dontwarn java.lang.invoke.LambdaForm$Hidden

# Silence some okio warnings
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
