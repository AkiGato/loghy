# R8 rules for Loghy.
#
# The app has a small reflective surface: no serialization library, no dynamic class
# loading. Room and Compose ship their own consumer rules, so little needs keeping by
# hand.

# Keep crash reports readable. Without these, R8 renames everything and a stack trace
# from a user is unusable. The matching mapping.txt under
# build/outputs/mapping/release/ de-obfuscates it — archive that file with every
# release you ship, or you cannot debug that version.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# GeminiService reads BuildConfig.GEMINI_API_KEY by reflection (getField), which R8
# cannot see. Without this the field is stripped and the lookup throws at runtime.
-keepclassmembers class com.example.BuildConfig {
    public static java.lang.String GEMINI_API_KEY;
}

# Room instantiates the generated database implementation by name.
-keep class com.example.data.db.LoghyDatabase_Impl { *; }
