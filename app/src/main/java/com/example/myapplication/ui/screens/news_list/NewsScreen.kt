package com.example.myapplication.ui.screens.news_list

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.myapplication.domain.status.LoadingStatus
import com.example.myapplication.ui.SwipeToRefreshLayout
import com.example.myapplication.ui.screens.ArticleUiModel
import com.example.myapplication.ui.screens.NewsViewModel
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun NewsScreen(newsViewModel: NewsViewModel, navController: NavController){
    when(val state = newsViewModel.newsScreenState.collectAsState().value){
        is NewsScreenState.ScreenWithData -> {
            FilledNewsScreen(state = state,
                onLastIndex = {
                newsViewModel.consumeAction(NewsViewModel.NewsScreenAction.RequestNewPage)
            }, onItemClick = {
                newsViewModel.consumeAction(NewsViewModel.NewsScreenAction.OpenArticle(it))
                navController.navigate("article")
            }, onRefresh = {
                newsViewModel.consumeAction(NewsViewModel.NewsScreenAction.UpdateNews)
            })
        }
        NewsScreenState.EmptyScreen -> {

        }
        is NewsScreenState.ErrorScreen -> {

        }
        NewsScreenState.LoadingScreen -> {
            ProgressScreen()
        }
    }
}

@Composable
fun FilledNewsScreen(state: NewsScreenState.ScreenWithData, onLastIndex: () -> Unit, onRefresh: () -> Unit, onItemClick: (ArticleUiModel) -> Unit){
    val listState = rememberLazyListState()
    SwipeToRefreshLayout(
        refreshingState = state.refreshLoadingStatus == LoadingStatus.LOADING,
        onRefresh = onRefresh,
        refreshIndicator = {
            Surface(elevation = 10.dp, shape = CircleShape) {
                CircularProgressIndicator(

                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp),
                    strokeWidth = 2.dp
                )
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                state =listState
            ) {
                itemsIndexed(state.articles) { idx, item ->
                    NewsItem(article = item) {
                        onItemClick(item)
                    }
                    if (idx == state.articles.lastIndex) onLastIndex()
                }
                if (state.paginationLoadingStatus == LoadingStatus.LOADING) item { ProgressItem() }
            }
        })
}

@Composable
fun ProgressScreen(){
    CircularProgressIndicator(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}

@Composable
fun ProgressItem(){
    CircularProgressIndicator(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.BottomCenter)
        .padding(bottom = 16.dp)
        .size(32.dp),
        strokeWidth = 3.dp)
}

@Composable
fun NewsItem(article: ArticleUiModel, onItemClick: () -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {
            onItemClick()
        }
        .padding(16.dp)
    ){
        Card(modifier = Modifier.size(100.dp, 70.dp),
        shape = MaterialTheme.shapes.medium) {
            CoilImage(data = article.urlToImage,
                contentDescription = null,
                fadeIn = true,
                contentScale = ContentScale.Crop)
        }
        Column(modifier = Modifier.weight(1F)) {
            Text(text = article.title,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .offset(y = (-2).dp))
        }
    }
}