package ovh.tarea3.flasklogin.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ovh.tarea3.flasklogin.network.ErrorUtils
import ovh.tarea3.flasklogin.network.RetrofitClient
import ovh.tarea3.flasklogin.network.UserRequest

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf("")
    var apiStatusMessage by mutableStateOf("Verificando conexión...")
    var isApiOnline by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onUsernameChange(newValue: String) {
        username = newValue
        errorMessage = ""
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        errorMessage = ""
    }

    fun checkApiStatus(context: Context) {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = RetrofitClient.getClient(context).getHello()
                    if (response.isSuccessful) {
                        apiStatusMessage = response.body()?.message ?: "API Funcionando"
                        isApiOnline = true
                    } else {
                        apiStatusMessage = "Servidor responde con error"
                        isApiOnline = false
                    }
                } catch (e: Exception) {
                    apiStatusMessage = "Servidor Desconectado (Offline)"
                    isApiOnline = false
                }
                delay(5000)
            }
        }
    }

    fun login(context: Context, onLoginSuccess: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.getClient(context).login(UserRequest(username, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        prefs.edit().putString("token", token).apply()
                    }
                    onLoginSuccess(username)
                } else {
                    errorMessage = ErrorUtils.getFriendlyErrorMessage(response = response)
                }
            } catch (e: Exception) {
                errorMessage = ErrorUtils.getFriendlyErrorMessage(throwable = e)
                isApiOnline = false
                apiStatusMessage = "Servidor Desconectado (Offline)"
            } finally {
                isLoading = false
            }
        }
    }
}
