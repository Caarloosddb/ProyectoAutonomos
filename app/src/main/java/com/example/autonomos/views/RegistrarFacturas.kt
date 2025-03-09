package com.example.autonomos.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarFacturas(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var baseImponible by remember { mutableStateOf("") }
    var iva by remember { mutableStateOf("") }

    var nombreEmisor by remember { mutableStateOf("") }
    var nifEmisor by remember { mutableStateOf("") }
    var direccionEmisor by remember { mutableStateOf("") }

    var nombreReceptor by remember { mutableStateOf("") }
    var nifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }

    // Estado para el tipo de registro (emisor o receptor)
    var tipoRegistro by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var ivaExpanded by remember { mutableStateOf(false) }

    val opciones = listOf("Emisor", "Receptor")
    val ivaOpciones = listOf("21", "10", "4", "0")

    val calendario = Calendar.getInstance()
    val year = calendario.get(Calendar.YEAR)
    val month = calendario.get(Calendar.MONTH)
    val day = calendario.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            fecha = dateFormat.format(selectedDate.time)
        },
        year, month, day
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Factura") },
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Formulario para registrar factura
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = tipoRegistro,
                        onValueChange = {},
                        label = { Text("Registrar como") },
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    tipoRegistro = opcion
                                    expanded = false
                                    if (opcion == "Emisor") {
                                        nombreEmisor = "Carlos"
                                        nifEmisor = "12345678"
                                        direccionEmisor = "Calle Falsa 123"
                                        nombreReceptor = ""
                                        nifReceptor = ""
                                        direccionReceptor = ""
                                    } else {
                                        nombreEmisor = ""
                                        nifEmisor = ""
                                        direccionEmisor = ""
                                        nombreReceptor = "Carlos"
                                        nifReceptor = "12345678"
                                        direccionReceptor = "Calle Falsa 123"
                                    }
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID de Factura") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )

                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { datePickerDialog.show() },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                    trailingIcon = {
                        Text(
                            text = "📅",
                            modifier = Modifier.clickable { datePickerDialog.show() }
                        )
                    },
                )

                OutlinedTextField(
                    value = baseImponible,
                    onValueChange = { baseImponible = it },
                    label = { Text("Base Imponible") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )

                // Dropdown para seleccionar el IVA
                ExposedDropdownMenuBox(
                    expanded = ivaExpanded,
                    onExpandedChange = { ivaExpanded = !ivaExpanded }
                ) {
                    OutlinedTextField(
                        value = iva,
                        onValueChange = {},
                        label = { Text("IVA") },
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        ),
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
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nombreEmisor,
                    onValueChange = { nombreEmisor = it },
                    label = { Text("Nombre Emisor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = nifEmisor,
                    onValueChange = { nifEmisor = it },
                    label = { Text("NIF Emisor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = direccionEmisor,
                    onValueChange = { direccionEmisor = it },
                    label = { Text("Dirección Emisor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = nombreReceptor,
                    onValueChange = { nombreReceptor = it },
                    label = { Text("Nombre Receptor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = nifReceptor,
                    onValueChange = { nifReceptor = it },
                    label = { Text("NIF Receptor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )
                OutlinedTextField(
                    value = direccionReceptor,
                    onValueChange = { direccionReceptor = it },
                    label = { Text("Dirección Receptor") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    ),
                )

                Spacer(modifier = Modifier.size(16.dp))

                val db = FirebaseFirestore.getInstance()
                val coleccion = if (tipoRegistro == "Emisor") "facturas_emisor" else "facturas_receptor"
                var mensajeConfirmacion by remember { mutableStateOf("") }

                Button(
                    onClick = {
                        // Crear una instancia de la data class Factura
                        val factura = hashMapOf(
                            "baseImponible" to (baseImponible.toIntOrNull() ?: 0),
                            "direccionEmisor" to direccionEmisor,
                            "direccionReceptor" to direccionReceptor,
                            "fecha" to fecha,
                            "id" to id,
                            "iva" to iva,
                            "nifEmisor" to nifEmisor,
                            "nifReceptor" to nifReceptor,
                            "nombreEmisor" to nombreEmisor,
                            "nombreReceptor" to nombreReceptor,
                            "total" to String.format("%.2f", (baseImponible.toDoubleOrNull() ?: 0.0) * (1 + (iva.toDoubleOrNull() ?: 0.0) / 100)).toDouble()
                        )

                        db.collection(coleccion)
                            .document(id)
                            .set(factura)
                            .addOnSuccessListener {
                                mensajeConfirmacion = "Factura guardada con ID: $id"

                                // Limpiar los campos
                                id = ""
                                fecha = ""
                                nombreEmisor = ""
                                nifEmisor = ""
                                direccionEmisor = ""
                                nombreReceptor = ""
                                nifReceptor = ""
                                direccionReceptor = ""
                                baseImponible = ""
                                iva = ""
                            }
                            .addOnFailureListener {
                                mensajeConfirmacion = "No se ha podido guardar la factura"
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Guardar Factura")
                }

                Spacer(modifier = Modifier.size(5.dp))
                Text(text = mensajeConfirmacion)
            }
        }
    )
}
