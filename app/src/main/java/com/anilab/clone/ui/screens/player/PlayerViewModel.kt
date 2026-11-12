package com.anilab.clone.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anilab.clone.data.model.StreamSource
import com.anilab.clone.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface PlayerUiState {
    object Loading : PlayerUiState
    data class Ready(
        val streamSource: StreamSource,
        val selectedUrl: String,
        val selectedCategory: String
    ) : PlayerUiState
    data class Error(val message: String) : PlayerUiState
}

class PlayerViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadEpisodeStream(episodeId: String, category: String = "sub") {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            repository.getEpisodeSources(episodeId = episodeId, category = category)
                .onSuccess { streamSource ->
                    val defaultUrl = streamSource.sources.firstOrNull()?.url ?: ""
                    _uiState.value = PlayerUiState.Ready(
                        streamSource = streamSource,
                        selectedUrl = defaultUrl,
                        selectedCategory = category
                    )
                }
                .onFailure { error ->
                    _uiState.value = PlayerUiState.Error(
                        error.localizedMessage ?: "Failed to resolve video stream"
                    )
                }
        }
    }

    fun switchCategory(episodeId: String, category: String) {
        loadEpisodeStream(episodeId, category)
    }

    fun selectSource(url: String) {
        val currentState = _uiState.value
        if (currentState is PlayerUiState.Ready) {
            _uiState.value = currentState.copy(selectedUrl = url)
        }
    }
}
