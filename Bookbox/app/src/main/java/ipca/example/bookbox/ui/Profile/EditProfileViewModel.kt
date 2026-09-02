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
import javax.inject.Inject

data class EditProfileViewState(
    val userProfile: User = User(),
    val isSuccess: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(EditProfileViewState())
    val uiState: State<EditProfileViewState> = _uiState

    private val currentUid = authRepository.getCurrentUid()

    init {
        currentUid?.let { uid ->
            repository.fetchProfile(uid).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(userProfile = result.data!!)
            }.launchIn(viewModelScope)
        }
    }

    fun updateProfile(fName: String, lName: String, bioStr: String, photo: String?) {
        currentUid?.let { uid ->
            val updates = mapOf(
                "firstName" to fName, "lastName" to lName, "bio" to bioStr, "photoUrl" to (photo ?: "")
            )
            repository.updateProfile(uid, updates).onEach { result ->
                if (result is ResultWrapper.Success) _uiState.value = _uiState.value.copy(isSuccess = true)
            }.launchIn(viewModelScope)
        }
    }

}