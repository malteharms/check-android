package de.malteharms.check.ui.components

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import de.malteharms.check.ui.theme.blue80
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import de.malteharms.check.data.NavigationItem
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.platform.LocalConfiguration
import de.malteharms.check.data.getBottomNavigationItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController



@Composable
fun FloatingBottomNavigation(
    navController: NavController,
    navigationItems: List<NavigationItem>
) {

    val configuration = LocalConfiguration.current
    val screenWidth: Int = configuration.screenWidthDp
    val navigationWith: Double = screenWidth * 0.7

    var selectedIcon by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.TopCenter)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(blue80)
                .width(navigationWith.dp)
                .height(40.dp)
                .shadow(30.dp, ambientColor = Color.Black)

        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationItems.forEachIndexed { index, item ->

                    val color = if (selectedIcon == index) {
                        MaterialTheme.colorScheme.primary
                    } else Color.White

                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color),
                        modifier = Modifier
                            .clickable {
                                // update the inner state to change icon color
                                selectedIcon = index

                                // navigate to the new selected page
                                navController.navigate(item.route)
                            } // clickable
                    ) // Image
                } // items
            } // Row
        } // Box
    } // Box
} // FBN


@Preview
@Composable
fun BottomNavigationPreview() {
    FloatingBottomNavigation( rememberNavController(), getBottomNavigationItems() )
}