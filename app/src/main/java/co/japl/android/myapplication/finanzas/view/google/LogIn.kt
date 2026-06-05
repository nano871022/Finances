package co.japl.android.myapplication.finanzas.view.google

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import co.japl.android.myapplication.finanzas.controller.google.GoogleAuthBackupRestoreViewModel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import coil.compose.AsyncImage

@Composable
fun LoginSpace(viewModel: GoogleAuthBackupRestoreViewModel) {
    val stateResultActivity = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.isProcessing.value = true
        viewModel.responseConnection(it)
    }
    val loginValue = remember { viewModel.loginValue }
    val nameValue = remember { viewModel.nameValue }
    val photoUrlValue = remember { viewModel.photoUrlValue }
    val isLogged = remember { viewModel.isLogged }
    val isProcessing = remember { viewModel.isProcessing }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkSmsPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(0.dp)
    ) {
        if (isProcessing.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))

            Text(
                text=stringResource(R.string.loading_data),
                textAlign = TextAlign.Center,
                modifier=Modifier.fillMaxWidth()

            )
        }else {

            if (isLogged.value) {
                ProfileHeader(
                    isLogged.value,
                    loginValue.value,
                    nameValue.value,
                    photoUrlValue.value
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (isLogged.value.not()) {
                CloudConnectionCard(isLogged.value, loginValue.value, onSwitchAccount = {
                    viewModel.login()?.let { stateResultActivity.launch(it) }
                })
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Permissions & Status
            PermissionsAndStatusSection(viewModel)

            Spacer(modifier = Modifier.height(5.dp))

            // Secondary Actions List
            AccountManagementList(isLogged.value, onSignOut = { viewModel.logout() })

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileHeader(isLogged: Boolean, email: String, name:String, photoUrl: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = photoUrl ?: R.drawable.ic_launcher_foreground,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground)
            )
            if (isLogged) {
                Surface(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(32.dp)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isLogged) name
                    else  stringResource(R.string.not_connected),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = if (isLogged) email
                    else stringResource(R.string.sing_in_to_sync_your_data),
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.AccountBalanceWallet,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.cloud_connection),
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
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.Cloud,
                                    contentDescription = stringResource(id = R.string.login),
                                    tint = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                if (isLogged) stringResource(id = R.string.signed_in_google)
                                else stringResource(id = R.string.not_signed_in),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                if (isLogged) "Active session since Oct 12"
                                else stringResource(id = R.string.please_sign_in),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                        Text(
                            if (isLogged) stringResource(id = R.string.switch_account)
                            else stringResource(id = R.string.login),
                            modifier = Modifier
                                .clickable { onSwitchAccount() }
                                .padding(8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                }
            }
        }
    }
}

@Composable
private fun PermissionsAndStatusSection(viewModel: GoogleAuthBackupRestoreViewModel) {
    val isLogged = viewModel.isLogged.value
    val isGoogleDriveGranted = viewModel.isGoogleDriveGranted.value
    val isEmailAccessGranted = viewModel.isEmailAccessGranted.value
    val isSmsAccessGranted = viewModel.isSmsAccessGranted.value

    Column {
        val allGranted = isGoogleDriveGranted && isEmailAccessGranted && isSmsAccessGranted
        val sectionColor = if (allGranted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, sectionColor.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.GppMaybe,
                        contentDescription = null,
                        tint = sectionColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Permissions & Status",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = sectionColor
                    )
                }
                if(isLogged) {
                    Spacer(modifier = Modifier.height(16.dp))

                    StatusItem(
                        icon = Icons.Rounded.AddToDrive,
                        title = stringResource(R.string.google_drive),
                        description = stringResource(R.string.google_drive_detail),
                        isGranted = isGoogleDriveGranted,
                        onGrant = { viewModel.grantGooglePermissions() }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StatusItem(
                        icon = Icons.Rounded.MarkEmailRead,
                        title = stringResource(R.string.email_access),
                        description = stringResource(R.string.email_access_detail),
                        isGranted = isEmailAccessGranted,
                        onGrant = { viewModel.grantGooglePermissions() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                StatusItem(
                    icon = Icons.Rounded.Textsms,
                    title = stringResource(R.string.sms_access),
                    description = stringResource(R.string.sms_access_detail),
                    isGranted = isSmsAccessGranted,
                    onGrant = { viewModel.grantSmsPermission() }
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
    isGranted: Boolean,
    onGrant: () -> Unit
) {
    val tint = if (isGranted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error
    val containerColor = if (isGranted) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
    
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
                if (!isGranted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, 
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable { onGrant() }
                    ) {
                        Text("Garantizar permisos",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold)
                        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountManagementList(isLogged: Boolean,onSignOut: () -> Unit) {
    Column {
        Text(
            stringResource(R.string.account_manager),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                ManagementItem(
                    icon = Icons.Rounded.Security,
                    title = stringResource(R.string.security_privacy),
                    description = stringResource(R.string.security_privacy_description)
                )
                if(isLogged) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    ManagementItem(
                        icon = Icons.Rounded.Logout,
                        title = stringResource(id = R.string.logout),
                        description = stringResource(id = R.string.logout),
                        tint = MaterialTheme.colorScheme.error,
                        onClick = onSignOut
                    )
                }
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
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = tint)
    }
}

@Preview( backgroundColor = 0xFFFFFFFF)
@Composable
fun ProfileLoginPreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI {
        LoginSpace(vm)
    }
}

@Preview(heightDp = 1600, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProfileLogedPreview(){
    val vm = getViewModel()
    vm.isLogged.value = true
    MaterialThemeComposeUI {
        LoginSpace(vm)
    }
}

@Composable
private fun getViewModel(): GoogleAuthBackupRestoreViewModel{
    return GoogleAuthBackupRestoreViewModel(null,null,null)
}