package com.example.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.ArticleUiModel
import com.example.myapplication.ui.screens.news_list.NewsScreen
import com.example.myapplication.ui.screens.NewsViewModel
import com.example.myapplication.ui.screens.article.ArticleScreen
import com.example.myapplication.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val newsViewModel: NewsViewModel by viewModel(state = { Bundle.EMPTY })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "news") {
                        composable("news") {
                            NewsScreen(newsViewModel, navController)
                        }
                        composable("article") {
                            ArticleScreen(newsViewModel)
                        }
                    }
                }
            }
        }
    }
}