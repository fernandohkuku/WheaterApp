package com.fernando.wheaterapp.presentation.components

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(
    vararg permissions: String,
    onGranted: @Composable () -> Unit,
    onShouldRational: @Composable () -> Unit,
    onDenied: @Composable () -> Unit,
    onRequestPermission: @Composable (MultiplePermissionsState) -> Unit = {},
    onLaunch: @Composable (Unit) -> Unit,
) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            *permissions
        )
    )

    permissionsState.permissions.forEach { permissionState ->
        val permission = permissions[permissionsState.permissions.indexOf(permissionState)]
        when (permissionState.permission) {
            permission -> {
                when {
                    !permissionsState.allPermissionsGranted && !permissionsState.shouldShowRationale -> onDenied()
                    permissionsState.shouldShowRationale -> onShouldRational()
                    permissionsState.allPermissionsGranted -> onGranted()
                }
            }
        }
    }

    fun launcher() {
        permissionsState.launchMultiplePermissionRequest()
    }

    onLaunch(launcher())


}