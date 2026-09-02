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

data class ForgotPasswordViewState(
    var email: String = "",
    var isLoading: Boolean = false,
    var error: String? = null,
    var isSuccess: Boolean = false
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor (
    private val repository: AuthenticationRepository
) : ViewModel() {

    var uiState = mutableStateOf(ForgotPasswordViewState())
        private set

    fun updateEmail(v:String) { uiState.value = uiState.value.copy(email = v) }

    fun forgotPassword() {
        repository.resetPassword(uiState.value.email).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> uiState.value = uiState.value.copy(isLoading = true, error =null)
                is ResultWrapper.Success-> uiState.value = uiState.value.copy(isLoading = false, error ="Link sent!", isSuccess = true)
                is ResultWrapper.Error -> uiState.value = uiState.value.copy(isLoading = false, error =result.message)
            }
        }
    }

    fun resetState() {
         uiState.value = ForgotPasswordViewState()
    }


}