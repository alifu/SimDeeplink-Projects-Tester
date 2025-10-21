package AppWork.SimDeeplink.Android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import AppWork.SimDeeplink.Android.ui.theme.SimDeeplinkTheme
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.UUID

data class AlertData(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val message: String
)

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<DeepLinkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)

        setContent {
            SimDeeplinkTheme {
                DeepLinkScreen(viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val data: Uri? = intent.data
        if (data != null) {
            viewModel.handleDeepLink(data)
        }
    }
}

class DeepLinkViewModel : ViewModel() {
    var alertData by mutableStateOf<AlertData?>(null)
        private set

    fun handleDeepLink(uri: Uri) {
        if (uri.host == "show_alert") {
            val title = uri.getQueryParameter("title")?.decodeOrEmpty() ?: "No Title"
            val message = uri.getQueryParameter("message") ?: "No Message"
            alertData = AlertData(title = title, message = message)
        } else {
            alertData = AlertData(
                title = "Unknown Deeplink",
                message = uri.toString()
            )
        }
    }

    private fun String.decodeOrEmpty(): String = try {
        java.net.URLDecoder.decode(this, java.nio.charset.StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        this
    }
}

@Composable
fun DeepLinkScreen(viewModel: DeepLinkViewModel = viewModel()) {
    val alertData = viewModel.alertData
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(alertData) {
        if (alertData != null) showDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "App is waiting for a deep link to show an alert.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        if (showDialog && alertData != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(alertData.title) },
                text = { Text(alertData.message) },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}