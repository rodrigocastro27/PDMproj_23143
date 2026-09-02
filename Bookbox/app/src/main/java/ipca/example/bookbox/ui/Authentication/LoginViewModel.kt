package ipca.example.bookbox.ui.Authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.repository.AuthenticationRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class LoginViewState(
    var identifier : String = "",
    var password : String = "",
    var isLoading : Boolean = false,
    var error : String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    var uiState = mutableStateOf(LoginViewState())
        private set

    fun updateIdentifier(v:String) { uiState.value = uiState.value.copy(identifier = v)}
    fun updatePassword(v:String) { uiState.value = uiState.value.copy(password = v)}

    fun login(onSucess: () -> Unit){
        repository.login(uiState.value.identifier, uiState.value.password).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true, error = null)
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(isLoading = false)
                    onSucess()
                }
                is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error= result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun resetState() {
        uiState.value = LoginViewState()

    }

}
