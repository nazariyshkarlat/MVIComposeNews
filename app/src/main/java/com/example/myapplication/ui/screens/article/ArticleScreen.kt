package com.example.myapplication.ui.screens.article

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.entity.Article
import com.example.myapplication.ui.screens.ArticleUiModel
import com.example.myapplication.ui.screens.NewsViewModel
import com.example.myapplication.ui.theme.Blue
import com.example.myapplication.ui.theme.BlueLight
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ArticleScreen(newsViewModel: NewsViewModel){
    when(val state = newsViewModel.articleScreenState.collectAsState().value){
        is ArticleScreenState.ScreenWithData -> {
            ArticleScreen(article = state.article)
        }
        is ArticleScreenState.ErrorScreen -> {

        }
        ArticleScreenState.LoadingScreen -> {
            ProgressScreen()
        }
    }
}

@Composable
fun ArticleScreen(article: ArticleUiModel){
    Column(modifier = Modifier.fillMaxHeight().verticalScroll(enabled = true, state = ScrollState(0))) {
        Card(shape = RoundedCornerShape(0.dp)){
            CoilImage(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
                data = article.urlToImage,
                contentDescription = null,
                fadeIn = true,
                contentScale = ContentScale.Crop)
        }
        Text(text = article.title,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        if(article.content != null) {
            Text(
                text = article.content,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        val context = LocalContext.current
        val pressed = remember { mutableStateOf( false) }
        Text(
            text = buildAnnotatedString {
                append(article.url)
                addStyle(SpanStyle(color = Blue, background = if(pressed.value) BlueLight else Color.Transparent), 0, article.url.length)
                addStringAnnotation(
                    tag = "URL",
                    annotation = article.url,
                    start = 0,
                    end = article.url.length
                )
            },
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.CenterHorizontally)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            pressed.value = true
                            return@pointerInteropFilter true
                        }
                        MotionEvent.ACTION_CANCEL  -> {
                            pressed.value = false
                        }
                        MotionEvent.ACTION_UP -> {
                            pressed.value = false
                            openLink(article.url, context)
                        }
                    }
                    false
                })
        Row(modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp)
            .fillMaxWidth()) {
            Text(text = article.publishedAt,
                modifier = Modifier
                    .padding(start = 16.dp),
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(text = article.sourceName,
                modifier = Modifier
                    .padding(end = 16.dp),
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
        if(article.author != null && article.author.toLowerCase() != article.sourceName.toLowerCase()) {
            Text(
                text = article.author,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.End),
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

private fun openLink(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(Intent.createChooser(intent, "Choose browser"))
}

@Composable
fun ProgressScreen(){
    CircularProgressIndicator(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}