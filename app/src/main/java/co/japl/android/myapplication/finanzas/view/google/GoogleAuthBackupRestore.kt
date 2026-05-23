package co.japl.android.myapplication.finanzas.view.google

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.com.japl.ui.theme.MaterialThemeComposeUI
import co.japl.android.myapplication.R
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleAuthBackupRestore(viewModel:GoogleAuthBackupRestoreViewModel) {
    val selectedTabIndex = remember { viewModel.tabIndex }

    Scaffold(
        topBar = {
            if (selectedTabIndex.value == 0) {
                ProfileTopBar()
            } else {
                SyncTopBar()
            }
        },
        bottomBar = {
            BottomNav(selectedTabIndex)
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)) {

            when (selectedTabIndex.value) {
                0 -> LoginSpace(viewModel)
                1 -> StatsSpace(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAkaM33g0dBseBvu9JwJtD73QH-mIciYu1by6mIJIse9KQCkU8DdgJYCTndEB_vGwdu5mO9Nr_-KTWSJX0CxN4DCL4FtNr5Jr_z1JcWb_WIc6q23y7kiAlkaA9yMw6Bo9OxPV_YzvagmOVp61c35E6KEDm-rrdJp0wialhu4xZdesXjvMVx7wX9eHshdUcF2QFORklexSORDQWjIHUGGji4_oa4zcFZ9IRn1U2e0EtLycp1IiZWGHw_SyyZal7O3eFeSIlyHnGD-Mo9",
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "FinanceFlow",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* TODO */ }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "FinanceFlow",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCJBeqKUCncizG8DmM1c2ZXcPYnQfUN0Q4f_SqzDgMpOX4YyYvKRadqVJpVd_m9RLrVgku6AKs_JFGaQaL11dbrLMdDyN_96ExJU_IJrxKLbxwGocgLqvd0TZi3Z-ERBIfDHpm7HyRcmuzwSBPAn9pSadJdOfha1OLTYz6U3yHD4mNYn1KBDKusvuLcr9LVsioHMXGg1Boj2z3XaDjhhyvBEGhcwvy4V9vfWdbdkbM4GyWUpW66vsmOZDHXc6d2gmBGg-Gu4OkqKucd",
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun BottomNav(selectedTabIndex: MutableIntState) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTabIndex.value == 1,
            onClick = { selectedTabIndex.value = 1 },
            icon = { Icon(Icons.Rounded.CloudSync, contentDescription = "Sync") },
            label = { Text("Sync") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Rounded.Storage, contentDescription = "Database") },
            label = { Text("Database") }
        )
        NavigationBarItem(
            selected = selectedTabIndex.value == 0,
            onClick = { selectedTabIndex.value = 0 },
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
internal fun GoogleAuthBackupRestorePreview(){
    val viewModel = getViewModel()
    viewModel.tabIndex.value = 0
    MaterialThemeComposeUI {
        GoogleAuthBackupRestore(viewModel)
    }
}

@Composable
private fun getViewModel():GoogleAuthBackupRestoreViewModel{
    return  GoogleAuthBackupRestoreViewModel(null,null,null,null,null)
}
