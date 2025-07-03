package com.example.kspcore

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import wu.seal.jsontokotlin.library.JsonToKotlinBuilder
import wu.seal.jsontokotlin.model.ConfigManager
import wu.seal.jsontokotlin.model.DefaultValueStrategy
import wu.seal.jsontokotlin.model.PropertyTypeStrategy
import wu.seal.jsontokotlin.model.TargetJsonConverter
import wu.seal.jsontokotlin.test.TestConfig.isNestedClassModel
import java.net.HttpURLConnection
import java.net.URL

class TestProcessor(
    private val codeGenerator: CodeGenerator,
    private val kspLogger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        try {
            generateClassFile()
        } catch (e: Exception) {

        }
        return emptyList()
    }

    private fun fetchTodoAsync(): String? {
        return try {
            val urlString = "https://script.googleusercontent.com/macros/echo?user_content_key=AehSKLi72LYE3yvYWHQVRaGH_5NkY74iEpQcAUkJrewK3_9-TwfDlhCiSXYVYV64ARnP8JKG4HnylMsY5LlD2JIuTPW3M992oPkGNdK4NNbOD6Bf_vzQMkGZZNEsIG48QytSY4dihTdf4mD2EeAIInjwliQz5noCBoPuxMWXxme-drFsyWf5D8qs1awVP6OEK0AdRs_u_SLLtnTRoQKcC5Yd72w2tSmBD3oQQsY2a7Wq1VyUR94qUhOuyIKqQfWdRHMMCF1p9EjeqrNfHuhfjBfd0acLNYZUZIdv9RE48CNq&lib=MQfjgH0kBc4n8n3R-PPYvGrTFd0BYWbni"
            val urlGit = "https://tiendungit99.github.io/DemoLanguage/data.json"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.apply {
                requestMethod = "GET"
                connectTimeout = 10000
                readTimeout = 10000
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "Kotlin-App")
                doInput = true
            }

            val responseCode = connection.responseCode
            println("Response Code: $responseCode")

            when (responseCode) {
                HttpURLConnection.HTTP_OK -> {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    connection.disconnect()
                    response
                }

                else -> {
                    val errorResponse =
                        connection.errorStream?.bufferedReader()?.use { it.readText() }
                    connection.disconnect()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun generateClassFile() {
        try {
            val actualOutput = JsonToKotlinBuilder()
                .setPackageName("com.example.myapplication.language")

            ConfigManager.apply {
                // Correct property names for JsonToKotlin library
                targetJsonConverterLib = TargetJsonConverter.MoshiCodeGen
                propertyTypeStrategy = PropertyTypeStrategy.NotNullable
                defaultValueStrategy = DefaultValueStrategy.AvoidNull
                isNestedClassModel = false
                isCommentOff = true
                isOrderByAlphabetical = true
            }

            val json1 = fetchTodoAsync()?.trimIndent() ?: ""

            kspLogger.info("JSON fetched: $json1")

            // Use Moshi to parse JSON into a Map
            val moshi = Moshi.Builder().build()
            val type =
                Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
            val adapter = moshi.adapter<Map<String, Any>>(type)
            val propertyDefaults = adapter.fromJson(json1) ?: emptyMap()

            var output = actualOutput.build(json1, "AppLanguage")

            // Convert snake_case to camelCase for property matching
            fun snakeToCamelCase(snake: String): String {
                return snake.split("_").mapIndexed { index, part ->
                    if (index == 0) part else part.capitalize()
                }.joinToString("")
            }

            // Replace property declarations with default values
            for ((jsonKey, value) in propertyDefaults) {
                val camelCaseProperty = snakeToCamelCase(jsonKey)

                val defaultValue = when (value) {
                    is String -> "\"${value.replace("\"", "\\\"").replace("\n", "\\n")}\""
                    is Number -> {
                        if (value is Double || value.toString().contains(".")) {
                            "$value"
                        } else {
                            value.toString()
                        }
                    }
                    is Boolean -> value.toString()
                    else -> "\"$value\""
                }

                // Updated regex pattern to match the actual generated property names
                // This pattern looks for: val propertyName: Type = someValue or val propertyName: Type,
                val pattern = Regex("""(val\s+$camelCaseProperty:\s+[A-Za-z0-9_<>?]+)(\s*=\s*[^,\n)]+)?([,\n)])""")

                output = output.replace(pattern) { matchResult ->
                    val propertyDeclaration = matchResult.groupValues[1]
                    val endChar = matchResult.groupValues[3]
                    "$propertyDeclaration = $defaultValue$endChar"
                }
            }

            output = "/**\n" +
                    " * Automatically generated file. DO NOT MODIFY\n" +
                    " */ \n\n" + output

            // Create the file using KSP CodeGenerator
            kotlin.runCatching {
                val file = codeGenerator.createNewFile(
                    dependencies = Dependencies(false), // Consider adding actual dependencies if needed
                    packageName = "com.example.myapplication.language",
                    fileName = "AppLanguage",
                    extensionName = "kt" // Explicitly specify the extension
                )

                file.write(output.toByteArray())
                file.close()
            }
        } catch (e: Exception) {
            kspLogger.error("Error generating class file: ${e.message}")
        }
    }
}