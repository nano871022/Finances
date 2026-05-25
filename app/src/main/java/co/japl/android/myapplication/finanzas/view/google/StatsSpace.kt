package co.japl.android.myapplication.finanzas.view.google

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.com.japl.ui.components.AlertDialogOkCancel
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.com.japl.ui.theme.values.Dimensions
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.NumbersUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale.getDefault

@Composable
fun StatsSpace(viewModel: GoogleAuthBackupRestoreViewModel) {
    viewModel.onload()
    val isProcessing by remember { viewModel.isProcessing }
    val isLogged by remember { viewModel.isLogged }
    val email = remember { viewModel.loginValue }
    val spaceUsed = remember { viewModel.spaceUsed }
    val spaceMax = remember { viewModel.spaceMax }
    val lastBackup = remember { viewModel.lastBackup }
    val spaceDBKb = remember { viewModel.spaceDBKb }
    val statusRestoreDialog = remember { mutableStateOf(false) }
    val statusBackupDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (isProcessing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            Text(
                text=stringResource(R.string.loading_data),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier=Modifier.fillMaxWidth().padding(Dimensions.PADDING_TOP))
        } else {

            // Status Overview Section (Bento Style)
            CloudSyncStatusCard(isLogged, email.value, spaceUsed.value, spaceMax.value)

            if (isLogged) {
                Spacer(modifier = Modifier.height(16.dp))

                LastBackupCard(lastBackup.value, spaceDBKb.value)
            }
            if (isLogged) {
                Spacer(modifier = Modifier.height(24.dp))

                // Main Actions Section
                Text(
                    stringResource(R.string.data_operations),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(12.dp))

                DataOperationCard(
                    title = stringResource(id = R.string.backup),
                    description = stringResource(id = R.string.backup_description),
                    icon = Icons.Rounded.CloudUpload,
                    iconContainerColor = MaterialTheme.colorScheme.primary,
                    iconContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        statusBackupDialog.value = true
                    }
                )
            }

            if (isLogged) {
                Spacer(modifier = Modifier.height(16.dp))

                DataOperationCard(
                    title = stringResource(id = R.string.restore),
                    description = stringResource(id = R.string.restore_description),
                    icon = Icons.Rounded.CloudDownload,
                    iconContainerColor = MaterialTheme.colorScheme.primary,
                    iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = {
                        statusRestoreDialog.value = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(32.dp))
        }

        AlertRestore(
            status = statusRestoreDialog,
            action = {
                viewModel.restore()
            })

        AlertBackup(
            status = statusBackupDialog,
            action = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.backup()
                }
            })
    }
}

@Composable
private fun CloudSyncStatusCard(isLogged: Boolean, email: String, used:Double,max:Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .drawWithBorder()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if(isLogged) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape,
                    modifier=Modifier.align(Alignment.End)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                stringResource(R.string.up_do_date),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Text(
                    stringResource(R.string.cloud_sync_status),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AddToDrive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        stringResource(R.string.connected_account),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        if (isLogged) email
                        else stringResource(R.string.not_connected),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    stringResource(R.string.store_space,NumbersUtil.bytesConvert(used), NumbersUtil.bytesConvert(max)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    stringResource(R.string.used_percent,NumbersUtil.toString4((used/max)*100)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align( alignment = Alignment.End)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            val usedProgress = (((used * 100) / max)/100).toFloat()
            LinearProgressIndicator(
                progress = { usedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF93C47D),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun LastBackupCard(date: LocalDateTime,spaceKb: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Icon(
                Icons.Rounded.History,
                contentDescription = null,
                modifier = Modifier.size(40.dp).align(Alignment.CenterVertically),
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    stringResource(R.string.last_backup),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")).uppercase(getDefault()),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "${date.format(DateTimeFormatter.ofPattern("HH:mm"))} • ${NumbersUtil.bytesConvert(spaceKb)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun DataOperationCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconContainerColor: Color,
    iconContentColor: Color,
    onClick:()-> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconContainerColor,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconContentColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun Modifier.drawWithBorder() = this.then(
    Modifier.drawWithContent {
        drawContent()
        drawLine(
            color = Color(0xFF2E5D1A),
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = 4.dp.toPx()
        )
    }
)

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

@Composable
private fun AlertBackup(status: MutableState<Boolean>, action: () -> Unit) {
    if (status.value) {
        AlertDialogOkCancel(
            title = R.string.dialog_backup,
            confirmNameButton = R.string.backup,
            onDismiss = { status.value = false },
        ) {
            action.invoke()
            status.value = false
        }
    }
}


@Composable
@Preview(heightDp = 700)
private fun StatsSpacePreview(){
    val vm = getViewModel()
    MaterialThemeComposeUI() {
        StatsSpace(vm)
    }
}

@Composable
@Preview(heightDp = 700)
private fun StatsSpaceLogedPreview(){
    val vm = getViewModel()
    vm.isLogged.value=true
    MaterialThemeComposeUI() {
        StatsSpace(vm)
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    val vm =  GoogleAuthBackupRestoreViewModel(null,null,null,null,null)
    vm.spaceMax.value = 15000000000.0
    vm.spaceUsed.value = 10.0
    vm.spaceDBKb.value = 1000.0
    vm.lastBackup.value = LocalDateTime.now().minusMonths(3)
    return vm
}

