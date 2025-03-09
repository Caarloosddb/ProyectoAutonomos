package com.example.autonomos.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarFactura(navController: NavController, documentId: String, coleccion: String) {
    val db = FirebaseFirestore.getInstance()
    var factura by remember { mutableStateOf<Map<String, Any>?>(null) }
    var baseImponible by remember { mutableStateOf("") }
    var iva by remember { mutableStateOf("21") }
    var total by remember { mutableStateOf("0.00") }
    var ivaExpanded by remember { mutableStateOf(false) }

    val ivaOpciones = listOf("21", "10", "4", "0")

    // Cargar la factura cuando se recibe el documentId y la colección
    LaunchedEffect(documentId, coleccion) {
        db.collection(coleccion).document(documentId)
            .get()
            .addOnSuccessListener { document ->
                document.data?.let {
                    factura = it
                    baseImponible = it["baseImponible"].toString()
                    iva = it["iva"].toString()
                }
            }
    }

    // Función para actualizar el total
    fun actualizarTotal() {
        val base = baseImponible.toDoubleOrNull() ?: 0.0
        val ivaPorcentaje = iva.toDoubleOrNull() ?: 0.0
        total = String.format("%.2f", base * (1 + ivaPorcentaje / 100))
    }

    // Función para guardar los cambios
    fun guardarCambios() {
        val nuevosDatos = mapOf(
            "baseImponible" to baseImponible,
            "iva" to iva,
            "total" to total
        )

        db.collection(coleccion).document(documentId)
            .update(nuevosDatos)
            .addOnSuccessListener {
                navController.popBackStack()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Factura") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack() // Acción para volver a la pantalla anterior
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver atrás")
                    }
                },
                actions = {
                    // Botón de "Cerrar sesión"
                    IconButton(onClick = {
                        navController.navigate("PantallaIS") {
                            popUpTo("PantallaUsuario") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Red)
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                OutlinedTextField(
                    value = baseImponible,
                    onValueChange = {
                        baseImponible = it
                        actualizarTotal()
                    },
                    label = { Text("Base Imponible (€)") }
                )

                // Dropdown para seleccionar el IVA
                ExposedDropdownMenuBox(
                    expanded = ivaExpanded,
                    onExpandedChange = { ivaExpanded = !ivaExpanded }
                ) {
                    OutlinedTextField(
                        value = iva,
                        onValueChange = {},
                        label = { Text("IVA (%)") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .clickable { ivaExpanded = true }
                    )
                    DropdownMenu(
                        expanded = ivaExpanded,
                        onDismissRequest = { ivaExpanded = false }
                    ) {
                        ivaOpciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    iva = opcion
                                    ivaExpanded = false
                                    actualizarTotal()
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = total,
                    onValueChange = {},
                    label = { Text("Total (€)") },
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { guardarCambios() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Guardar Cambios")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}
