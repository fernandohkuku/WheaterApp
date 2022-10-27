package com.fernando.wheaterapp.presentation.components

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult

class PermissionRequester(
    private val activity: ComponentActivity,
    private val permission: String,
    onDenied: () -> Unit = {},
    onShowRational: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val permissionContract = ActivityResultContracts.RequestPermission()

    private val launcher = activity.registerForActivityResult(permissionContract) { isGranted ->
        when {
            isGranted -> onGranted()
            activity.shouldShowRequestPermissionRationale(permission) -> onShowRational()
            else -> onDenied()
        }
    }

    fun runWithPermission(onGranted: () -> Unit) {
        this.onGranted = onGranted
        launcher.launch(permission)
    }

    fun runWithPermission(){
        launcher.launch(permission)
    }
}