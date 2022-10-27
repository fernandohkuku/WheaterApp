package com.fernando.wheaterapp.domain.entities.location

data class LocationEntity(
    val latitude:Double,
    val longitude:Double,
    val accuracy:Float = 0f
){

}
