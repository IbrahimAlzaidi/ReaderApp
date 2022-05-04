@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.readerapp.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.readerapp.components.InputField
import com.example.readerapp.components.ReaderAppBar
import com.example.readerapp.model.Item
import com.example.readerapp.navigation.ReaderScreens


@Composable
fun ReaderBookSearchScreen(
    navController: NavController,
    viewModel: BooksSearchViewModel = hiltViewModel(),
) {
    Scaffold(topBar = {
        ReaderAppBar(title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController) {
            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
        }
    }) {

        Column {
            SearchForm(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) { searchQuery ->
                viewModel.searchBooks(query = searchQuery)
            }

            Spacer(modifier = Modifier.height(25.dp))

            BookList(navController, viewModel)
        }

    }
}

@Composable
fun BookList(navController: NavController, viewModel: BooksSearchViewModel = hiltViewModel()) {


    val listOfBooks = viewModel.list

    if (viewModel.isLoading) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            LinearProgressIndicator(modifier = Modifier.padding(start = 4.dp, end = 4.dp))
            Text(text = "Loading...", modifier = Modifier.padding(start = 4.dp, end = 4.dp))
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }
        }
    }


}

@Composable
fun BookRow(book: Item, navController: NavController) {

    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(4.dp),
        shape = RectangleShape,
        elevation = 8.dp) {
        Row(Modifier.padding(4.dp), verticalAlignment = Alignment.Top) {
            val imageUrl = if (book.volumeInfo.readingModes.image) {
                book.volumeInfo.imageLinks.smallThumbnail
            } else {
                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            }
//            val imageUrl = "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))

            Column() {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text =  "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text =  "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

                Text(text =  "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption)

            }
        }
    }

}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {},
) {
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }


        InputField(valueState = searchQueryState, labelId = "Search", enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })

    }
}