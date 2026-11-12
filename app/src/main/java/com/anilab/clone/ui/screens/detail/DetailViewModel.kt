package com.anilab.clone.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anilab.clone.data.model.AnimeDetail
import com.anilab.clone.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val detail: AnimeDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DetailViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadAnimeDetail(animeId: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            repository.getAnimeDetail(animeId)
                .onSuccess { detail ->
                    _uiState.value = DetailUiState.Success(detail)
                }
                .onFailure { error ->
                    _uiState.value = DetailUiState.Error(error.localizedMessage ?: "Failed to load details")
                }
        }
    }
}
