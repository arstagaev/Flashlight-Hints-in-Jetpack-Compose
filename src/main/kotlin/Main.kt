import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.singleWindowApplication

// ─── DEMO ──────────────────────────────────────────────────────────────

@Composable
@Preview
private fun HintOrFlashlightScreen() {
    // Central state
    var selectedIndex by remember { mutableStateOf(0) }

    // Define your targets in one list with hints
    val targets = listOf(
        TargetItem("You can work with TextField"),
        TargetItem("This a cute button"),
        TargetItem("This is a like button"),
        TargetItem("Amazing red box")
    )

    // One Rect‐state per target
    val boundsList = remember { targets.map { mutableStateOf<Rect?>(null) } }

    Box(Modifier.fillMaxSize()) {
        // 4 Target UI Components:
        TextField(
            value = "textValue",
            onValueChange = {  },
            modifier = Modifier.padding(60.dp).width(200.dp).align(Alignment.TopStart)
                .captureBoundsInRoot { boundsList[0].value = it }
        )
        Button(onClick = { /*…*/ },
            modifier = Modifier.padding(60.dp).size(120.dp, 48.dp).align(Alignment.BottomEnd)
                .captureBoundsInRoot { boundsList[1].value = it }) {
            Text("Button")
        }
        IconButton(onClick = { /*…*/ },
            modifier = Modifier.padding(60.dp).size(48.dp).align(Alignment.CenterEnd)
                .captureBoundsInRoot { boundsList[2].value = it }) {
            Icon(Icons.Default.Favorite, contentDescription = "Favorite")
        }
        Box(
            modifier = Modifier.padding(60.dp)
                .size(120.dp, 48.dp)
                .background(Color.Red)
                .align(Alignment.TopCenter)
                .captureBoundsInRoot { boundsList[3].value = it }
            ,
            contentAlignment = Alignment.Center
        ) {
            Text("Box", color = Color.White)
        }
        // END of 4 Target UI Components


        // Main Overlay
        FocusOverlay(hole = boundsList[selectedIndex].value)

        // Hint + Arrows
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = targets[selectedIndex].hint,
                color = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Button(onClick = {
                    selectedIndex = (selectedIndex + targets.size - 1) % targets.size
                }) { Text("◀") }
                Button(onClick = {
                    selectedIndex = (selectedIndex + 1) % targets.size
                }) { Text("▶") }
            }
        }
    }
}

fun main() = singleWindowApplication(
    title = "Flashlight-like Overlay Hint Demo"
) {
    MaterialTheme {
        HintOrFlashlightScreen()
    }
}
