package ovh.tarea3.flasklogin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.util.Log

@Composable
fun WelcomeScreen(
    username: String?,
    onLogout: () -> Unit,
    onNavigateToUsers: () -> Unit
) {
    val context = LocalContext.current
    val displayMessage = if (!username.isNullOrEmpty()) {
        "¡Bienvenido, $username!"
    } else {
        "Bienvenido"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = displayMessage, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = onNavigateToUsers,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Usuarios")
        }
        
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                val prefs = context.getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
                //Ver Token
                //val tokenAntes = prefs.getString("token", null)
                //Log.d("AUTH_DEBUG", "Token ANTES de logout: $tokenAntes")

                prefs.edit().remove("token").apply()

                //Ver Token al hacer logout
                //val tokenDespues = prefs.getString("token", null)
                //Log.d("AUTH_DEBUG", "Token DESPUES de logout: $tokenDespues")

                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar Sesión")
        }


    }
}
