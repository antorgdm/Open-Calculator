package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.geometry.GeometryEngine
import com.example.domain.geometry.ShapeType
import com.example.ui.theme.FlatAlizarin
import com.example.ui.theme.LocalFlatThemeColors
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun GeometryToolsScreen() {
    val colors = LocalFlatThemeColors.current
    var selectedShape by remember { mutableStateOf(ShapeType.TRIANGLE) }
    val df = remember { DecimalFormat("#,##0.####", DecimalFormatSymbols(Locale.US)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShapeType.values().forEach { shape ->
                FilterChip(
                    selected = selectedShape == shape,
                    onClick = { selectedShape = shape },
                    label = { Text(shape.displayName, fontSize = 13.sp) },
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(4.dp),
            color = colors.surface,
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${selectedShape.displayName} (${if (selectedShape.is3D) "3D Shape" else "2D Shape"})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )

                Spacer(modifier = Modifier.height(16.dp))

                var param1 by remember(selectedShape) { mutableStateOf("6") }
                var param2 by remember(selectedShape) { mutableStateOf("4") }
                var param3 by remember(selectedShape) { mutableStateOf("5") }
                var param4 by remember(selectedShape) { mutableStateOf("5") }
                var param5 by remember(selectedShape) { mutableStateOf("8") }

                val p1 = param1.toDoubleOrNull() ?: 0.0
                val p2 = param2.toDoubleOrNull() ?: 0.0
                val p3 = param3.toDoubleOrNull() ?: 0.0
                val p4 = param4.toDoubleOrNull() ?: 0.0
                val p5 = param5.toDoubleOrNull() ?: 0.0

                when (selectedShape) {
                    ShapeType.SQUARE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Side length (a)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.RECTANGLE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Length (l)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Width (w)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.TRIANGLE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Base (b)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = param3, onValueChange = { param3 = it }, label = { Text("Side a") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = param4, onValueChange = { param4 = it }, label = { Text("Side c") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        }
                    }
                    ShapeType.CIRCLE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Radius (r)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.TRAPEZOID -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Base a") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Base b") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param3, onValueChange = { param3 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = param4, onValueChange = { param4 = it }, label = { Text("Side c") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = param5, onValueChange = { param5 = it }, label = { Text("Side d") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        }
                    }
                    ShapeType.PARALLELOGRAM -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Base (b)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param3, onValueChange = { param3 = it }, label = { Text("Side (a)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.CUBE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Side length (a)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.CUBOID -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Length (l)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Width (w)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param3, onValueChange = { param3 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.CYLINDER -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Radius (r)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.CONE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Radius (r)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = param2, onValueChange = { param2 = it }, label = { Text("Height (h)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                    ShapeType.SPHERE -> {
                        OutlinedTextField(value = param1, onValueChange = { param1 = it }, label = { Text("Radius (r)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val resultAttempt = runCatching {
                    when (selectedShape) {
                        ShapeType.SQUARE -> GeometryEngine.calculateSquare(p1)
                        ShapeType.RECTANGLE -> GeometryEngine.calculateRectangle(p1, p2)
                        ShapeType.TRIANGLE -> GeometryEngine.calculateTriangle(p1, p2, p3, p4)
                        ShapeType.CIRCLE -> GeometryEngine.calculateCircle(p1)
                        ShapeType.TRAPEZOID -> GeometryEngine.calculateTrapezoid(p1, p2, p3, p4, p5)
                        ShapeType.PARALLELOGRAM -> GeometryEngine.calculateParallelogram(p1, p2, p3)
                        ShapeType.CUBE -> GeometryEngine.calculateCube(p1)
                        ShapeType.CUBOID -> GeometryEngine.calculateCuboid(p1, p2, p3)
                        ShapeType.CYLINDER -> GeometryEngine.calculateCylinder(p1, p2)
                        ShapeType.CONE -> GeometryEngine.calculateCone(p1, p2)
                        ShapeType.SPHERE -> GeometryEngine.calculateSphere(p1)
                    }
                }

                resultAttempt.fold(
                    onSuccess = { result ->
                        ResultBox(label = result.primaryLabel, value = df.format(result.primaryValue))
                        Spacer(modifier = Modifier.height(12.dp))
                        ResultSubBox(modifier = Modifier.fillMaxWidth(), label = result.secondaryLabel, value = df.format(result.secondaryValue))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Formula: ${result.formula}", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                    },
                    onFailure = { err ->
                        Text(
                            text = err.message ?: "Invalid geometric parameters",
                            color = FlatAlizarin,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultBox(label: String, value: String) {
    val colors = LocalFlatThemeColors.current
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = colors.background,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = colors.mutedText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = colors.primaryButton)
        }
    }
}

@Composable
private fun ResultSubBox(modifier: Modifier = Modifier, label: String, value: String) {
    val colors = LocalFlatThemeColors.current
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = colors.background,
        border = BorderStroke(1.dp, colors.border),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = colors.mainText)
        }
    }
}
