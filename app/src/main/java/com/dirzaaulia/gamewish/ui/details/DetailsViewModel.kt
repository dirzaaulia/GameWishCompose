package com.dirzaaulia.gamewish.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val rawgRepository: RawgRepository
) : ViewModel() {

    val gameDetails = mutableStateOf<GameDetails?>(null)
    val gameDetailsScreenshots = mutableStateOf<List<Screenshots>>(listOf())

    val loading = mutableStateOf(true)

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage.asStateFlow()

    init {
        getGameDetails(3328)
        getGameDetailsScrenshots(3328)
    }

    fun getGameDetails(gameId: Long) {
        viewModelScope.launch {
            val result = rawgRepository.getGameDetails(gameId)
            if (result is Resource.Success) {
                gameDetails.value = result.data as GameDetails
            }
        }
    }

    fun getGameDetailsScrenshots(gameId: Long) {
        gameDetailsScreenshots.value = emptyList()
        viewModelScope.launch {
            val result = rawgRepository.getGameDetailsScreenshots(gameId)
            if (result is Resource.Success) {
                gameDetailsScreenshots.value = result.data as List<Screenshots>
            }

            loading.value = false
        }
    }
}