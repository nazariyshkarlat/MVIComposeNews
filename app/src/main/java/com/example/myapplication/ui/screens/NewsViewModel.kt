package com.example.myapplication.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.interactor.NewsInteractor
import com.example.myapplication.domain.interactor.NewsInteractor.Companion.CURRENT_STATE
import com.example.myapplication.domain.status.NewsStatus
import com.example.myapplication.ui.screens.article.ArticleScreenState
import com.example.myapplication.ui.screens.article.toArticleScreenState
import com.example.myapplication.ui.screens.news_list.NewsScreenState
import com.example.myapplication.ui.screens.news_list.toUiNewsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NewsViewModel(private val savedStateHandle: SavedStateHandle, private val newsInteractor: NewsInteractor) : ViewModel() {

    companion object{
        const val ARTICLE_STATE = "ARTICLE_STATE"
    }

    private val _newsScreenState: MutableStateFlow<NewsScreenState> = MutableStateFlow(
        NewsScreenState.LoadingScreen
    )
    val newsScreenState: StateFlow<NewsScreenState> = _newsScreenState

    private val _articleScreenState: MutableStateFlow<ArticleScreenState> = MutableStateFlow(
        ArticleScreenState.LoadingScreen
    )
    val articleScreenState: StateFlow<ArticleScreenState> = _articleScreenState

    var getNewsJob: Job? = null

    private val savedStateNews : NewsStatus?
    get() = savedStateHandle.get<String>(CURRENT_STATE)?.let{ Json.decodeFromString(it) }

    private val savedStateArticle : ArticleUiModel?
        get() = savedStateHandle.get<String>(ARTICLE_STATE)?.let{ Json.decodeFromString(it) }

    init {
        emitNews()
        savedStateArticle?.let{
            emitArticle(it)
        }
    }

    fun consumeAction(action: NewsScreenAction){
        when(action){
            NewsScreenAction.RequestNewPage -> {
                if(newsScreenState.value.isCanLoadNewPage()) updateNews(isNewPage = true)
            }
            is NewsScreenAction.OpenArticle -> {
                emitArticle(action.article)
            }
            else -> {
                updateNews(isNewPage = false)
            }
        }
    }

    private fun emitNews() {
        if(getNewsJob?.isActive != true) {
            getNewsJob = viewModelScope.launch(Dispatchers.Main) {
                savedStateNews?.let{
                    getSavedStateNews(it)
                } ?: run {
                    getNewsFromRepository(false)
                }
            }
        }
    }

    private fun updateNews(isNewPage: Boolean){
        if(getNewsJob?.isActive != true) {
            getNewsJob = viewModelScope.launch(Dispatchers.Main) {
                getNewsFromRepository(isNewPage = isNewPage)
            }
        }
    }

    private suspend fun getNewsFromRepository(isNewPage: Boolean){
        newsInteractor.getNews(isNewPage = isNewPage).collect {
            _newsScreenState.emit(it.toUiNewsScreenState())
            saveNews()
        }
    }

    private suspend fun getSavedStateNews(savedState: NewsStatus){
        newsInteractor.restoreNews(savedState).collect {
            _newsScreenState.emit(it.toUiNewsScreenState())
            saveNews()
        }
    }

    private fun saveNews(){
        savedStateHandle.set(CURRENT_STATE, Json.encodeToString(newsInteractor.currentState))
    }

    private fun saveArticle(articleUiModel: ArticleUiModel){
        savedStateHandle.set(ARTICLE_STATE, Json.encodeToString(articleUiModel))
    }

    private fun emitArticle(article: ArticleUiModel){
        viewModelScope.launch {
            saveArticle(article)
            _articleScreenState.emit(article.toArticleScreenState())
        }
    }

    sealed class NewsScreenAction{
        object RequestNewPage : NewsScreenAction()
        object UpdateNews : NewsScreenAction()
        class OpenArticle(val article: ArticleUiModel) : NewsScreenAction()
    }
}