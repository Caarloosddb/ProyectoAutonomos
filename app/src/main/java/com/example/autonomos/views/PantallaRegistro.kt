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
fun PantallaRegistro(navController: NavController){
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
            text = "Registrarse",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(20.dp))

        var nif by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var mensajeConfirmacion by remember { mutableStateOf("") }

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

        val db = FirebaseFirestore.getInstance()
        val coleccion = "usuarios"

        val dato = hashMapOf(
            "nif" to nif,
            "nombre" to nombre,
            "contrasena" to contrasena
        )

        Button(
            onClick = {
                db.collection(coleccion)
                    .document(nif)
                    .set(dato)
                    .addOnSuccessListener {
                        mensajeConfirmacion = "Datos guardados correctamente"
                        nif = ""
                        nombre = ""
                        contrasena = ""
                        navController.navigate("VerFacturasUser")
                    }
                    .addOnFailureListener {
                        mensajeConfirmacion = "No se ha podido guardar"
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
            Text(text = "Guardar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (mensajeConfirmacion.isNotEmpty()) {
            Text(
                text = mensajeConfirmacion,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
