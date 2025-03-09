package com.example.autonomos.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.autonomos.R
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PantallaIS(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Iniciar sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(20.dp))

        var nif by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var mensajeError by remember { mutableStateOf("") }

        OutlinedTextField(
            value = nif,
            onValueChange = { nif = it },
            label = { Text("NIF") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (mensajeError.isNotEmpty()) {
            Text(
                text = mensajeError,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        val db = FirebaseFirestore.getInstance()
        val coleccion = "usuarios"

        Button(
            onClick = {
                db.collection(coleccion)
                    .document(nif)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val contrasenaUsuario = document.getString("contrasena")
                            val nombreUsuario = document.getString("nombre")
                            val nifUsuario = document.getString("nif")


                            // Verificar si el nombre es "admin"
                            if (contrasenaUsuario == contrasena) {
                                if (nombreUsuario == "admin" && contrasenaUsuario == "admin" && nifUsuario == "05994415") {
                                    navController.navigate("PantallaInicio") {
                                        popUpTo("PantallaIS") { inclusive = true }
                                    }
                                } else {
                                    // Si el nombre no es "admin", navegar a la pantalla normal
                                    navController.navigate("VerFacturasUser") {
                                        popUpTo("PantallaIS") { inclusive = true }
                                    }
                                }
                            } else {
                                mensajeError = "Contraseña incorrecta"
                            }
                        } else {
                            mensajeError = "El NIF no está registrado"
                        }
                    }
                    .addOnFailureListener {
                        mensajeError = "Error al intentar iniciar sesión"
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(text = "Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "¿No tienes cuenta?",
            fontSize = 14.sp,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("pantallaRegistro") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(text = "Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
