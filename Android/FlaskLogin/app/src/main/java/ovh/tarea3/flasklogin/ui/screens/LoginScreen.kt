package ovh.tarea3.flasklogin.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ovh.tarea3.flasklogin.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    // Iniciamos la verificación de API al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.checkApiStatus(context)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar estado de la API desde el ViewModel
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (viewModel.isApiOnline) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Text(
                text = viewModel.apiStatusMessage,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (viewModel.isApiOnline) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }

        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(text = viewModel.errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.login(context, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.isApiOnline && !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Entrar")
            }
        }

        TextButton(onClick = onNavigateToRegister, enabled = !viewModel.isLoading) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}
