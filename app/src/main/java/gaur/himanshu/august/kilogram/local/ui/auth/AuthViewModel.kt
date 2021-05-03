package gaur.himanshu.august.kilogram.local.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.ui.auth.repository.IAuthRepository
import gaur.himanshu.august.kilogram.remote.body.LoginBody
import gaur.himanshu.august.kilogram.remote.response.LoginResponse
import gaur.himanshu.august.kilogram.remote.response.RegisterResponse
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: IAuthRepository): ViewModel() {

    private val _loginBody= MutableLiveData<Events<Result<LoginResponse>>>()
    val loginBody:LiveData<Events<Result<LoginResponse>>> = _loginBody

    private  val _registerBody= MutableLiveData<Events<Result<RegisterResponse>>>()
    val registerBody:LiveData<Events<Result<RegisterResponse>>> = _registerBody

    fun login(loginBody: LoginBody)=viewModelScope.launch {
        _loginBody.postValue(Events(Result(Status.LOADING,null,null)))
        _loginBody.postValue(Events(authRepository.login(loginBody)))
    }

    fun register(part:MultipartBody.Part, userName:RequestBody, email: RequestBody, phone:RequestBody, password:RequestBody,fcmTokens:RequestBody)=viewModelScope.launch {
        _registerBody.postValue(Events(Result(Status.LOADING,null,null)))
        _registerBody.postValue(Events(authRepository.register(part, userName, password, email, phone,fcmTokens)))
    }







}