package com.goodluck3301.flexlogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goodluck3301.flexlogger.log.FlexLogger
import com.goodluck3301.flexlogger.log.LogLevel
import com.goodluck3301.flexlogger.ui.theme.FlexLoggerTheme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivityTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlexLoggerTheme {
                LoggerScreen()
            }
        }
    }

    @Composable
    fun LoggerScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "FlexLogger Compose Demo",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Basic Log Level Buttons
            Button(
                onClick = { FlexLogger.v(TAG, "This is a VERBOSE log message.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Verbose") }

            Button(
                onClick = { FlexLogger.d(TAG, "This is a DEBUG log message.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Debug") }

            Button(
                onClick = { FlexLogger.i(TAG, "This is an INFO log message.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Info") }

            Button(
                onClick = {
                    FlexLogger.w(
                        TAG,
                        "This is a WARN log message.",
                        IllegalStateException("Warning state!")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Warn (with Exception)") }

            Button(
                onClick = {
                    try {
                        val x = 10 / 0
                    } catch (e: Exception) {
                        FlexLogger.e(TAG, "This is an ERROR log message.", e)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Error (with Exception)") }

            Button(
                onClick = { FlexLogger.wtf(TAG, "This is an ASSERT log message - WTF!") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Assert (WTF)") }

            Spacer(modifier = Modifier.height(24.dp))

            // Pretty Printing Buttons
            Button(
                onClick = {
                    val json = """{"user":{"name":"Alice","age":28},"roles":["admin","user"]}"""
                    FlexLogger.json(TAG, LogLevel.DEBUG, json)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Pretty JSON") }

            Button(
                onClick = {
                    val xml = """<data><item id="1">First</item><item id="2">Second</item></data>"""
                    FlexLogger.xml(TAG, LogLevel.INFO, xml)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) { Text("Log Pretty XML") }
        }
    }
}