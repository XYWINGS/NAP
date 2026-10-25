package com.example.nap.presentation.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nap.domain.model.ContentItem

@Composable
fun GroupedContent(
    modifier: Modifier = Modifier,
    content: List<ContentItem>,
    onDeleteImageClick: (Int) -> Unit,
    onTextChange: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        content.forEachIndexed { index, item ->
            item(key = index) {
                when (item) {
                    is ContentItem.Image -> {
                        /*Go until find an image, drop what was before, then continue till a text type found,
                        * then display the image types in a group*/
                        val isAlreadyDisplayed: Boolean = index > 0 &&
                                content[index - 1] is ContentItem.Image
                        content.takeIf { !isAlreadyDisplayed }
                            ?.drop(index)
                            ?.takeWhile { it is ContentItem.Image }
                            ?.map { (it as ContentItem.Image).url }
                            ?.let { urls ->
                                ImageGroup(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    imageUrls = urls,
                                    onDeleteImageClick = { imageIndex ->
                                        onDeleteImageClick(index + imageIndex)
                                    }
                                )
                            }
                    }

                    is ContentItem.Text -> {
                        TextContent(
                            text = item.content,
                            onTextChange = {
                                onTextChange(index, it)
                            }
                        )
                    }
                }
            }
        }
    }
}
