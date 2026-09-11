# Modelos desserializados pelo Gson por reflexao: os nomes dos campos precisam sobreviver.
-keep class br.com.fiap.bussola.data.remote.dto.** { *; }
-keep class br.com.fiap.bussola.data.local.dto.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
