import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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


// Capture a composable’s bounds *relative to the root* of this Compose hierarchy
fun Modifier.captureBoundsInRoot(onBounds: (Rect) -> Unit): Modifier =
    this.onGloballyPositioned { coords ->
        val topLeft = coords.localToRoot(Offset.Zero)
        val size    = coords.size.toSize()
        onBounds(Rect(topLeft, size))
    }

// Draw a full‑screen 50%‑black mask but punch out exactly one hole (with padding)
@Composable
fun FocusOverlay(
    hole: Rect?,
    padding: Dp = 10.dp,
    maskAlpha: Float = 0.5f
) {
    if (hole == null) return

    val density = LocalDensity.current
    Canvas(modifier = Modifier.fillMaxSize()) {
        val padPx = with(density) { padding.toPx() }
        val path = Path().apply {
            addRect(Rect(Offset.Zero, Size(size.width, size.height)))
            addRoundRect(
                RoundRect(
                    left   = hole.left   - padPx,
                    top    = hole.top    - padPx,
                    right  = hole.right  + padPx,
                    bottom = hole.bottom + padPx,
                    topLeftCornerRadius = CornerRadius(20f,20f),
                    topRightCornerRadius = CornerRadius(20f,20f),
                    bottomLeftCornerRadius = CornerRadius(20f,20f),
                    bottomRightCornerRadius = CornerRadius(20f,20f)
                )
            )
            fillType = PathFillType.EvenOdd
        }
        drawPath(path, Color.Black.copy(alpha = maskAlpha))
    }
}

// Data holder for each target: its hint + how to compose it
data class TargetItem(
    val hint: String,
    val rect: Rect? = null
)