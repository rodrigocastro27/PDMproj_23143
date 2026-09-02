package ipca.example.bookbox.ui.Profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ipca.example.bookbox.models.User
import ipca.example.bookbox.repository.AuthenticationRepository
import ipca.example.bookbox.repository.ProfileRepository
import ipca.example.bookbox.repository.ResultWrapper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountSettingsViewState(
    val userProfile: User = User(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(AccountSettingsViewState())
    val uiState: State<AccountSettingsViewState> = _uiState

    private val currentUid = authRepository.getCurrentUid()


    init {
        currentUid?.let { uid ->
            repository.fetchProfile(uid).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value =
                    _uiState.value.copy(userProfile = result.data!!)
            }.launchIn(viewModelScope)
        }
    }

    fun updateAccountSettings(newUsername: String, newEmail: String){
        currentUid?.let { uid ->
            repository.updateAccountSettings(uid, newUsername, newEmail).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(isSuccess = true )
            }.launchIn(viewModelScope)

        }
    }

    fun updatePassword(newPass: String) {
        if (newPass.length < 6) {
            _uiState.value =
                _uiState.value.copy(error = "Password must be at least 6 characters long")
            return
        }
        repository.updatePassword(newPass).onEach { result ->
            when (result) {
                is ResultWrapper.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                is ResultWrapper.Success -> _uiState.value =
                    _uiState.value.copy(isLoading = false, isSuccess = true)

                is ResultWrapper.Error -> _uiState.value =
                    _uiState.value.copy(isLoading = false, error = result.message)
            }
        }.launchIn(viewModelScope)
    }

    fun deleteUserAccount(onDeleted: () -> Unit){
        currentUid?.let { uid ->
            repository.deleteUserAccount(uid).onEach { result ->
                if (result is ResultWrapper.Success) onDeleted()
            }.launchIn(viewModelScope)
        }
    }

    fun signOut(onSignedOut: () -> Unit){
        viewModelScope.launch {
            authRepository.signOut()
            onSignedOut()
        }
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}