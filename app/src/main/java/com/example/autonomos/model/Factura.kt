package com.example.autonomos.model

data class Factura(
    val baseImponible: Int,
    val direccionEmisor: String,
    val direccionReceptor: String,
    val fecha: String,
    val id: Int,
    val iva: String,
    val nifEmisor: Int,
    val nifReceptor: Int,
    val nombreEmisor: String,
    val nombreReceptor: String,
    val total: Double
)
