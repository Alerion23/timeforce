package com.natife.timeforce.ui.common

import android.widget.Toast
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.natife.timeforce.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    TopAppBar(
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { expanded.value = !expanded.value }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.mainscreen_menu)
                )
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.settings), fontSize = 16.sp) },
                    onClick = {
                        Toast.makeText(
                            context,
                            "Settings",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.private_policy),
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        Toast.makeText(
                            context,
                            "Private Policy",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
        }
    )
}