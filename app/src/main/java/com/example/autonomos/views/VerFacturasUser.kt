package com.example.autonomos.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerFacturasUser(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var facturas by remember { mutableStateOf<List<Pair<String, Map<String, Any>>>>(emptyList()) }
    var coleccionSeleccionada by remember { mutableStateOf("facturas_emisor") }

    fun cargarFacturas() {
        db.collection(coleccionSeleccionada)
            .get()
            .addOnSuccessListener { resultado ->
                facturas = resultado.documents.map { it.id to it.data!! }
            }
            .addOnFailureListener {
                facturas = emptyList()
            }
    }

    LaunchedEffect(coleccionSeleccionada) {
        cargarFacturas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nebrija Facturas") },
                actions = {
                    // Botón de "Cerrar sesión"
                    IconButton(onClick = {
                        navController.navigate("PantallaIS") {
                            popUpTo("PantallaUsuario") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Red)
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            coleccionSeleccionada = "facturas_emisor"
                            cargarFacturas()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text("Facturas Emisor")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            coleccionSeleccionada = "facturas_receptor"
                            cargarFacturas()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text("Facturas Receptor")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(facturas.size) { index ->
                        val (documentId, factura) = facturas[index]
                        FacturaCardUser(
                            baseImponible = factura["baseImponible"].toString(),
                            direccionEmisor = factura["direccionEmisor"].toString(),
                            direccionReceptor = factura["direccionReceptor"].toString(),
                            fecha = factura["fecha"].toString(),
                            id = factura["id"].toString(),
                            iva = factura["iva"].toString(),
                            nifEmisor = factura["nifEmisor"].toString(),
                            nifReceptor = factura["nifReceptor"].toString(),
                            nombreEmisor = factura["nombreEmisor"].toString(),
                            nombreReceptor = factura["nombreReceptor"].toString(),
                            total = factura["total"].toString()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun FacturaCardUser(
    baseImponible: String,
    direccionEmisor: String,
    direccionReceptor: String,
    fecha: String,
    id: String,
    iva: String,
    nifEmisor: String,
    nifReceptor: String,
    nombreEmisor: String,
    nombreReceptor: String,
    total: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Factura #$id", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Fecha: $fecha", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Emisor:", fontWeight = FontWeight.Bold)
            Text(text = "$nombreEmisor ($nifEmisor)")
            Text(text = direccionEmisor)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Receptor:", fontWeight = FontWeight.Bold)
            Text(text = "$nombreReceptor ($nifReceptor)")
            Text(text = direccionReceptor)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Base Imponible: $baseImponible€", fontWeight = FontWeight.Bold)
            Text(text = "IVA: $iva%", fontWeight = FontWeight.Bold)
            Text(text = "Total: $total€", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Red)
        }
    }
}
