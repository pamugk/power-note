package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.navigation.Location

@Composable
internal fun AppContentNavigator(
    compactUi: Boolean,
    currentLocation: Location,
    destinations: List<AppNavigationDestination>,
    mediumUi: Boolean,
    modifier: Modifier,
    navigate: (Location) -> Unit,
    mainContent: @Composable (Modifier) -> Unit
) {
    if (compactUi) {
        mainContent(modifier)
    } else if(mediumUi) {
        Row(modifier = modifier) {
            NavigationRail {
                destinations.forEach { destination ->
                    NavigationRailItem(
                        selected = currentLocation == destination.location,
                        onClick = { navigate(destination.location) },
                        icon = { Icon(destination.icon, destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
            mainContent(Modifier)
        }
    } else {
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(Modifier.width(IntrinsicSize.Min)) {
                    Spacer(Modifier.height(12.dp))
                    destinations.forEach { destination ->
                        NavigationDrawerItem(
                            label = { Text(destination.label) },
                            selected = currentLocation == destination.location,
                            onClick = { navigate(destination.location) },
                            icon = { Icon(destination.icon, destination.label) },
                        )
                    }
                }
            },
            modifier = modifier,
        ) {
            mainContent(Modifier)
        }
    }
}