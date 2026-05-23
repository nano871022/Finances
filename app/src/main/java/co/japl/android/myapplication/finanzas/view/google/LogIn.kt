package co.japl.android.myapplication.finanzas.view.google

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ChevronRight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginSpace(viewModel: GoogleAuthBackupRestoreViewModel) {
    val context = LocalContext.current
    val statusRestoreDialog = remember { mutableStateOf(false) }
    val stateResultActivity = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.isProcessing.value = true
        viewModel.responseConnection(it)
    }
    val loginValue = remember { viewModel.loginValue }
    val isLogged = remember { viewModel.isLogged }
    val isProcessing = remember { viewModel.isProcessing }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (isProcessing.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
        }

        // Profile Header Section
        ProfileHeader(isLogged.value, loginValue.value)

        Spacer(modifier = Modifier.height(24.dp))

        // Bento Layout Grid - Auth & Connection
        CloudConnectionCard(isLogged.value, loginValue.value, onSwitchAccount = {
            viewModel.login()?.let { stateResultActivity.launch(it) }
        })

        Spacer(modifier = Modifier.height(24.dp))

        // Permissions & Status
        PermissionsAndStatusSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Manual Sync Button
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.backup()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Rounded.Sync, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Manual Force Sync", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Secondary Actions List
        AccountManagementList(onSignOut = { viewModel.logout() })

        Spacer(modifier = Modifier.height(32.dp))
    }

    AlertRestore(status = statusRestoreDialog, action = {
        viewModel.restore()
    })
}

@Composable
private fun ProfileHeader(isLogged: Boolean, email: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuB4pBHmtD4vguMkfj9hvQOrCph3A-HOLRkfivwIhkFcq5TKtChn-UDpeqkYUStwRbUh403n9QEqmrl4VAHEXwmfntfxZmwX3fwjvZ48uq0n8xACY8EwrNFz9BUv0ovF7vsTITAUQE0UIgFBqJYf_BNJXBO_mFeNuV48ERmAQ6qbfar4ynXkNBpT5ZT4vlCfxds6XhZ9uL_3BDPUrqe9qXfXZbjmdxbTjWrbqb4zZnFWUmSH2yl7SoUacsJb3KDQMubCfnv1LTPm781u",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(4.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            if (isLogged) {
                Surface(
                    color = Color(0xFF38761D),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(32.dp)
                        .border(2.dp, Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isLogged) "Connected as Alex Rivers" else "Not Connected",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = if (isLogged) email else "Sign in to sync your data",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Badge(
                icon = Icons.Rounded.VerifiedUser,
                text = "Premium Member",
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Badge(
                icon = Icons.Rounded.History,
                text = "Last sync: 2m ago",
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Badge(icon: ImageVector, text: String, containerColor: Color, contentColor: Color) {
    Surface(
        color = containerColor,
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelMedium, color = contentColor)
        }
    }
}

@Composable
private fun CloudConnectionCard(isLogged: Boolean, email: String, onSwitchAccount: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Cloud Connection",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                // Google Icon placeholder
                                Icon(Icons.Rounded.Cloud, contentDescription = null, tint = Color(0xFF4285F4))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                if (isLogged) "Signed in with Google" else "Not Signed In",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                if (isLogged) "Active session since Oct 12" else "Please sign in",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        if (isLogged) "Switch Account" else "Sign In",
                        modifier = Modifier
                            .clickable { onSwitchAccount() }
                            .padding(8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKHz-tU37oEw7k49c9cveuGODmpew8X0S6XFtJNJ6uxV76o1Z99vghQUT8Gvwg1dfjgmUMziHsmWU3DkxRQT8vhUiOxZ1R_x9nPVD1n0xZ7ir8RrLpI2MCUEPpafc3oWHPisyfFJjzkp6VFQKXLq6qXOUlVI42RFGqYbbO-_jKdYO2RRjzQMkoNtyQGnUns3gGZGOiUHLMBL6Dt0eJOloprNzREvFPl0Wh-DYv7k70AtN4TRRB7ep3lFY5icwb_y6LP-no_XHIXYaF",
                    contentDescription = "Secure Cloud",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        "Your data is end-to-end encrypted and synced across all your Android devices automatically.",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionsAndStatusSection() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.GppMaybe,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Permissions & Status",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                StatusItem(
                    icon = Icons.Rounded.CloudOff,
                    title = "Drive Access Required",
                    description = "Automated backups are paused because FinanceFlow doesn't have write access to your Google Drive.",
                    tint = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    actionText = "Grant Permission"
                )

                Spacer(modifier = Modifier.height(12.dp))

                StatusItem(
                    icon = Icons.Rounded.CheckCircle,
                    title = "Biometric Auth",
                    description = "Face Unlock & Fingerprint active.",
                    tint = Color(0xFF38761D),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                StatusItem(
                    icon = Icons.Rounded.NotificationsActive,
                    title = "Smart Alerts",
                    description = "Notify on unusual spending patterns.",
                    tint = Color(0xFF93C47D),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun StatusItem(
    icon: ImageVector,
    title: String,
    description: String,
    tint: Color,
    containerColor: Color,
    actionText: String? = null
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (actionText != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        Text(actionText, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountManagementList(onSignOut: () -> Unit) {
    Column {
        Text(
            "ACCOUNT MANAGEMENT",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                ManagementItem(
                    icon = Icons.Rounded.Security,
                    title = "Security & Privacy",
                    description = "Manage your encryption keys and passwords"
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ManagementItem(
                    icon = Icons.Rounded.Storage,
                    title = "Data Export",
                    description = "Download your transaction history as CSV"
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ManagementItem(
                    icon = Icons.Rounded.Logout,
                    title = "Sign Out",
                    description = "Disconnect your current session",
                    tint = MaterialTheme.colorScheme.error,
                    onClick = onSignOut
                )
            }
        }
    }
}

@Composable
private fun ManagementItem(
    icon: ImageVector,
    title: String,
    description: String,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = tint)
                Text(description, style = MaterialTheme.typography.bodySmall, color = if (tint == MaterialTheme.colorScheme.error) tint.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.AutoMirrored.Rounded.ChevronRight, contentDescription = null, tint = tint)
    }
}

@Composable
private fun AlertRestore(status: MutableState<Boolean>, action: () -> Unit) {
    if (status.value) {
        AlertDialogOkCancel(
            title = R.string.dialog_restore,
            confirmNameButton = R.string.restore,
            onDismiss = { status.value = false },
        ) {
            action.invoke()
            status.value = false
        }
    }
}
