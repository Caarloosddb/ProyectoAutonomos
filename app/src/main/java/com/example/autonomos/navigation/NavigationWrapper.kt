package com.example.autonomos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.autonomos.views.EditarFactura
import com.example.autonomos.views.PantallaIS
import com.example.autonomos.views.PantallaInicio
import com.example.autonomos.views.PantallaRegistro
import com.example.autonomos.views.RegistrarFacturas
import com.example.autonomos.views.VerFacturasAdmin
import com.example.autonomos.views.VerFacturasUser


@Composable
fun NavigationWrapper (navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "PantallaIS") {

        composable("RegistrarFacturas"){ RegistrarFacturas(navHostController) }
        composable("PantallaInicio"){ PantallaInicio(navHostController) }
        composable("VerFacturasAdmin"){ VerFacturasAdmin(navHostController) }
        composable("PantallaIS"){ PantallaIS(navHostController) }
        composable("PantallaRegistro"){ PantallaRegistro(navHostController) }
        composable("editarFactura/{documentId}/{coleccion}") { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId") ?: ""
            val coleccion = backStackEntry.arguments?.getString("coleccion") ?: ""
            EditarFactura(navHostController, documentId, coleccion)}
        composable("VerFacturasUser"){ VerFacturasUser(navHostController) }

    }
    }
