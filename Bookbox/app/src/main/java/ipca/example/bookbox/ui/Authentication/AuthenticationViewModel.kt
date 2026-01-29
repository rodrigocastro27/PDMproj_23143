package ipca.example.bookbox.ui.Authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.user.User
import ipca.example.bookbox.repository.AuthenticationRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class AuthenticationViewState(
    var identifier: String = "",
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var birthDate: Long? = null,
    var photoUrl: String? = null,
    var isLoading: Boolean = false,
    var error: String? = null,
    var isSuccess: Boolean = false
)

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {

    var uiState = mutableStateOf(AuthenticationViewState())
        private set

    fun updateIdentifier(v: String) {
        uiState.value = uiState.value.copy(
            identifier = v) }
    fun updateUsername(v: String) {
        uiState.value = uiState.value.copy(
            username = v) }
    fun updateFirstName(v: String) {
        uiState.value = uiState.value.copy(
            firstName = v) }
    fun updateLastName(v: String) {
        uiState.value = uiState.value.copy(
            lastName = v) }
    fun updateEmail(v: String) {
        uiState.value = uiState.value.copy(
            email = v) }
    fun updatePassword(v: String) {
        uiState.value = uiState.value.copy(
            password = v) }
    fun updateConfirmPassword(v: String) {
        uiState.value = uiState.value.copy(
            confirmPassword = v) }
    fun updateBirthDate(v: Long?) {
        uiState.value = uiState.value.copy(
            birthDate = v) }

    fun login(onSuccess: () -> Unit) {
        repository.login(
            uiState.value.identifier, uiState.value.password).onEach { result ->
            handleResult(result) { onSuccess() }
        }.launchIn(viewModelScope)
    }

    fun register() {
        if (uiState.value.password != uiState.value.confirmPassword) {
            uiState.value = uiState.value.copy(error = "Passwords do not match")
            return
        }
        val user = User(
            username = uiState.value.username,
            firstName = uiState.value.firstName,
            lastName = uiState.value.lastName,
            email = uiState.value.email,
            birthDate = uiState.value.birthDate
        )
        repository.register(
            user, uiState.value.password).onEach { result ->
            handleResult(result)
        }.launchIn(viewModelScope)
    }

    fun forgotPassword() {
        repository.resetPassword(
            uiState.value.email).onEach { result ->
            handleResult(result) {
                uiState.value = uiState.value.copy(
                    error = "Link sent!", isSuccess = true)
            }
        }.launchIn(viewModelScope)
    }

    private fun handleResult(result: ResultWrapper<Unit>, onSuccess: (() -> Unit)? = null) {
        when(result) {
            is ResultWrapper.Success -> {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    isSuccess = true)
                onSuccess?.invoke()
            }
            is ResultWrapper.Loading ->
                uiState.value = uiState.value.copy(
                    isLoading = true,
                    error = null)
            is ResultWrapper.Error ->
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    error = result.message)
        }
    }

    fun resetState() { uiState.value = AuthenticationViewState() }
}