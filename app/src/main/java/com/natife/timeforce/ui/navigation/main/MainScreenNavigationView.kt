package com.natife.timeforce.ui.navigation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.natife.timeforce.R
import com.natife.timeforce.ui.common.MainScreenBottomNavBar
import com.natife.timeforce.ui.common.MainScreenTopBar
import com.natife.timeforce.ui.navigation.bottom.BottomNavGraph
import com.natife.timeforce.ui.theme.TimeForceTheme

context (NavHostController)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenNavigationView(
    bottomNavController: NavHostController
) {
    with(bottomNavController) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        var topBarTitle by remember { mutableIntStateOf(R.string.alarm_title) }
        TimeForceTheme {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MainScreenTopBar(
                        scrollBehavior = scrollBehavior,
                        title = stringResource(id = topBarTitle)
                    )
                },
                bottomBar = {
                    MainScreenBottomNavBar(
                        modifier = Modifier.fillMaxWidth(),
                        onNavigate = { route, title ->
                            navigate(route)
                            topBarTitle = title
                        }
                    )
                }
            ) { initialPadding ->
                Box(modifier = Modifier.padding(initialPadding)) {
                    BottomNavGraph(navController = this@with)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    TimeForceTheme {
        rememberNavController().run {
            MainScreenNavigationView(rememberNavController())
        }
    }
}