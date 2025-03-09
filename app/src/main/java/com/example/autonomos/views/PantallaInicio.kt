package com.example.autonomos.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.autonomos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavController) {
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(400.dp)
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo3),
                    contentDescription = "Logo Nebrija Facturas"
                )
                Text(
                    text = "Bienvenido a Nebrija Facturas",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.size(40.dp))

                // Botón para ir a Ver Facturas
                Button(
                    onClick = {
                        navController.navigate("VerFacturasAdmin")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Ver Facturas")
                }

                Spacer(modifier = Modifier.size(20.dp))

                // Botón para ir a Registrar Factura
                Button(
                    onClick = {
                        navController.navigate("RegistrarFacturas")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Registrar Factura")
                }
            }
        }
    )
}
