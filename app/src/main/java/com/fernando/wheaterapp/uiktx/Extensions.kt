package com.fernando.wheaterapp.uiktx

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun Any?.isNull(): Boolean = this == null

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }

    return this != null
}