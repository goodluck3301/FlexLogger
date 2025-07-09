# 📝 FlexLogger

<div align="center">

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-29%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=29)
[![JitPack](https://img.shields.io/jitpack/v/goodluck3301/FlexLogger.svg)](https://jitpack.io/#goodluck3301/FlexLogger)

*A flexible, configurable, and powerful logging utility for Android applications*

[Features](#-features) • [Installation](#-installation) • [Quick Start](#-quick-start) • [Documentation](https://goodluck3301.github.io/flexlogger.html) • [Examples](#-examples)
</div>

---

## ✨ Features

<table>
<tr>
<td>

### 🎯 **Flexible Configuration**
- Customizable log levels and filtering
- Global tag prefixes for organized logging
- Thread-safe operations
- Enable/disable logging globally

</td>
<td>

### 📁 **Multiple Destinations**
- **Logcat** - Standard Android logging
- **File Logging** - Persistent file storage
- **Custom Destinations** - Extensible interface

</td>
</tr>
<tr>
<td>

### 🎨 **Pretty Printing**
- Beautiful JSON formatting
- XML pretty printing
- Structured log formatting
- Timestamp and thread info

</td>
<td>

### 🔄 **Smart File Management**
- Automatic log rotation
- Size-based file clearing
- Organized directory structure
- Crash-safe file operations

</td>
</tr>
</table>

---

## 🚀 Installation

### Gradle (Kotlin DSL)
```kotlin
//settings.gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = URI("https://jitpack.io") } // Add this
    }
}

// App module
dependencies {
    implementation("com.github.goodluck3301:FlexLogger:flexlogger-1.1")
}
```

### Gradle (Groovy)
```groovy
//settings.gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' } // Add this
  }
}

// App module
dependencies {
    implementation 'com.github.goodluck3301:FlexLogger:flexlogger-1.1'
}
```

---

## ⚡ Quick Start

### 1. Initialize in your Application class

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        FlexLogger.init {
            enabled = true
            globalTagPrefix = "MyApp"
            minLevel = LogLevel.DEBUG
            showTimestamp = true
            showThreadInfo = true
            
            // Add destinations
            logcat()
            file(
                context = this@MyApplication,
                fileName = "app_log.txt",
                maxFileSizeMb = 5
            )
        }
    }
}
```

### 2. Register in AndroidManifest.xml

```xml
<application
    android:name=".MyApplication"
    ... >
</application>
```

### 3. Start logging!

```kotlin
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Basic logging
        log_i(TAG, "Activity created successfully")
        log_d(TAG, "Debug information: ${someVariable}")
        
        // Error logging with exception
        try {
            riskyOperation()
        } catch (e: Exception) {
            log_e(TAG, e, "Operation failed")
        }
        
        // Pretty print JSON
        flexLogJson(TAG, """{"user": "John", "id": 123}""")
    }
}
```

---

## 📚 Documentation

### Configuration Options

<details>
<summary><strong>📋 Complete Configuration Guide</strong></summary>

```kotlin
FlexLogger.init {
    // Global settings
    enabled = true                              // Enable/disable all logging
    globalTagPrefix = "MyApp"                   // Prefix for all log tags
    minLevel = LogLevel.VERBOSE                 // Minimum log level to process
    
    // Formatting options
    showTimestamp = true                        // Include timestamps
    showThreadInfo = true                       // Include thread information
    timestampFormat = "yyyy-MM-dd HH:mm:ss.SSS" // Custom timestamp format
    
    // Destinations
    logcat()                                    // Android Logcat
    
    file(
        context = applicationContext,
        fileName = "debug.log",                 // Log file name
        directory = "logs",                     // Subdirectory in cache
        maxFileSizeMb = 10                      // Max size before rotation
    )
}
```

</details>

### Log Levels

| Level | Function | Description |
|-------|----------|-------------|
| `VERBOSE` | `log_v()` | Detailed information for debugging |
| `DEBUG` | `log_d()` | Debug information for development |
| `INFO` | `log_i()` | General information messages |
| `WARN` | `log_w()` | Warning messages for potential issues |
| `ERROR` | `log_e()` | Error messages for failures |
| `ASSERT` | `log_wtf()` | Critical errors that should never happen |

### Logging Functions

<details>
<summary><strong>🔧 Available Logging Methods</strong></summary>

#### Basic Logging
```kotlin
// Standard logging
FlexLogger.d("TAG", "Debug message")
FlexLogger.i("TAG", "Info message")
FlexLogger.e("TAG", "Error message", exception)

// Convenience functions with lambda (lazy evaluation)
flexLogD("TAG") { "Expensive string operation: ${computeValue()}" }
flexLogE("TAG", exception) { "Error occurred during ${operation}" }
```

#### Pretty Printing
```kotlin
// JSON formatting
flexLogJson("API") { responseJson }
FlexLogger.json("API", LogLevel.INFO, jsonString)

// XML formatting
flexLogXml("Parser") { xmlContent }
FlexLogger.xml("Parser", LogLevel.DEBUG, xmlString)
```

</details>

---

## 💡 Examples

### Basic Usage

```kotlin
class NetworkManager {
    private val TAG = "NetworkManager"
    
    suspend fun fetchData(): Result<Data> {
        flexLogI(TAG) { "Starting data fetch operation" }
        
        return try {
            val response = apiService.getData()
            log_json(TAG, LogLevel.DEBUG, response.body() )
            Result.success(response.data)
        } catch (e: Exception) {
            log_e(TAG, e, "Failed to fetch data from API")
            Result.failure(e)
        }
    }
}
```

### Custom Log Destination

```kotlin
class RemoteLogDestination(private val apiEndpoint: String) : LogDestination {
    override val id = "remote_logger"
    
    override fun send(logMessage: LogMessage, formattedMessage: String) {
        // Send logs to remote server
        if (logMessage.level >= LogLevel.ERROR) {
            sendToRemoteServer(formattedMessage)
        }
    }
    
    private fun sendToRemoteServer(message: String) {
        // Implementation for remote logging
    }
}

// Usage in configuration
FlexLogger.init {
    addDestination(RemoteLogDestination("https://api.example.com/logs"))
}
```

### Advanced File Configuration

```kotlin
FlexLogger.init {
    globalTagPrefix = "MyApp"
    
    // Different log files for different purposes
    file(
        context = this@MyApplication,
        fileName = "debug.log",
        maxFileSizeMb = 2
    )
    
    file(
        context = this@MyApplication,
        fileName = "errors.log",
        directory = "crash_logs",
        maxFileSizeMb = 10
    )
}
```

---

## 🏗️ Architecture

```mink
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Application   │───▶│   FlexLogger    │───▶│  Destinations   │
│                 │    │                  │    │                 │
│ log_i(...)      │    │ ┌──────────────┐ │    │ ┌─────────────┐ │
│ log_e(...)      │    │ │ LogMessage   │ │    │ │  Logcat     │ │
│ log_json(...)   │    │ │ - level      │ │    │ │  File       │ │
└─────────────────┘    │ │ - tag        │ │    │ │  Custom     │ │
                       │ │ - message    │ │    │ └─────────────┘ │
                       │ │ - throwable  │ │    └─────────────────┘
                       │ │ - timestamp  │ │
                       │ └──────────────┘ │
                       └──────────────────┘
```

---

## 🎯 Best Practices

### ✅ Do's

- **Initialize early**: Set up FlexLogger in your Application's `onCreate()`
- **Use meaningful tags**: Create constants for tags in each class
- **Leverage lazy evaluation**: Use lambda functions for expensive string operations
- **Log at appropriate levels**: Use DEBUG for development, INFO for important events
- **Handle exceptions**: Always log exceptions with context

### ❌ Don'ts

- **Don't log sensitive data**: Avoid logging passwords, tokens, or personal information
- **Don't over-log**: Excessive logging can impact performance
- **Don't forget to disable**: Consider disabling verbose logging in production
- **Don't ignore file sizes**: Monitor log file sizes to prevent storage issues

### 🔒 Production Configuration

```kotlin
FlexLogger.init {
    enabled = BuildConfig.DEBUG   // Disable in release builds
    minLevel = LogLevel.INFO      // Only log important messages
    showThreadInfo = false        // Reduce log verbosity
    
    if (BuildConfig.DEBUG) {
        logcat()
        file(context = this@MyApplication, maxFileSizeMb = 5)
    }
}
```

---

### Development Setup

1. Clone the repository
```bash
git clone https://github.com/goodluck3301/FlexLogger
```

2. Open in Android Studio

3. Run the sample app to test your changes

### Reporting Issues

Please use the [issue tracker](https://github.com/goodluck3301/FlexLogger/issues) to report bugs or request features.

---
## 📄 License

```
MIT License
Copyright (c) 2025 Levon M.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
---

<div align="center">

**⭐ Star this repository if FlexLogger helped you!**

Made with ❤️ for the Android community

[Report Bug](https://github.com/goodluck3301/FlexLogger/issues) • [Request Feature](https://github.com/goodluck3301/FlexLogger/issues) • [Documentation](https://goodluck3301.github.io/flexlogger.html)

</div>
