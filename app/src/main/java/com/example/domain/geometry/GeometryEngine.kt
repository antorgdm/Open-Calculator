package com.example.domain.geometry

import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sqrt

enum class ShapeType(val displayName: String, val is3D: Boolean) {
    SQUARE("Square", false),
    RECTANGLE("Rectangle", false),
    TRIANGLE("Triangle", false),
    CIRCLE("Circle", false),
    TRAPEZOID("Trapezoid", false),
    PARALLELOGRAM("Parallelogram", false),
    CUBE("Cube", true),
    CUBOID("Cuboid", true),
    CYLINDER("Cylinder", true),
    CONE("Cone", true),
    SPHERE("Sphere", true)
}

data class GeometryResult(
    val primaryLabel: String,
    val primaryValue: Double,
    val secondaryLabel: String,
    val secondaryValue: Double,
    val formula: String
)

object GeometryEngine {
    fun calculateSquare(side: Double): GeometryResult {
        if (side <= 0.0) throw IllegalArgumentException("Side must be greater than zero")
        val area = side * side
        val perimeter = 4 * side
        return GeometryResult("Area", area, "Perimeter", perimeter, "Area = a², Perimeter = 4a")
    }

    fun calculateRectangle(length: Double, width: Double): GeometryResult {
        if (length <= 0.0 || width <= 0.0) throw IllegalArgumentException("Dimensions must be greater than zero")
        val area = length * width
        val perimeter = 2 * (length + width)
        return GeometryResult("Area", area, "Perimeter", perimeter, "Area = l × w, Perimeter = 2(l + w)")
    }

    fun calculateTriangle(base: Double, height: Double, sideA: Double, sideB: Double): GeometryResult {
        if (base <= 0.0 || height <= 0.0 || sideA <= 0.0 || sideB <= 0.0) {
            throw IllegalArgumentException("Triangle dimensions must be greater than zero")
        }
        // Triangle inequality theorem on sides: base, sideA, sideB
        if (base + sideA <= sideB || base + sideB <= sideA || sideA + sideB <= base) {
            throw IllegalArgumentException("Triangle inequality violated: sum of any 2 sides must exceed the 3rd side")
        }
        // Altitude cannot exceed adjacent side lengths
        if (height > sideA + 1e-6 || height > sideB + 1e-6) {
            throw IllegalArgumentException("Altitude height cannot exceed adjacent side lengths")
        }

        val area = 0.5 * base * height
        val perimeter = base + sideA + sideB
        return GeometryResult("Area", area, "Perimeter", perimeter, "Area = ½ × b × h, Perimeter = a + b + c")
    }

    fun calculateCircle(radius: Double): GeometryResult {
        if (radius <= 0.0) throw IllegalArgumentException("Radius must be greater than zero")
        val area = PI * radius * radius
        val circumference = 2 * PI * radius
        return GeometryResult("Area", area, "Circumference", circumference, "Area = πr², Circumference = 2πr")
    }

    fun calculateTrapezoid(a: Double, b: Double, height: Double, c: Double, d: Double): GeometryResult {
        if (a <= 0.0 || b <= 0.0 || height <= 0.0 || c <= 0.0 || d <= 0.0) {
            throw IllegalArgumentException("Trapezoid dimensions must be greater than zero")
        }
        val area = 0.5 * (a + b) * height
        val perimeter = a + b + c + d
        return GeometryResult("Area", area, "Perimeter", perimeter, "Area = ½(a + b)h, Perimeter = a + b + c + d")
    }

    fun calculateParallelogram(base: Double, height: Double, side: Double): GeometryResult {
        if (base <= 0.0 || height <= 0.0 || side <= 0.0) {
            throw IllegalArgumentException("Dimensions must be greater than zero")
        }
        val area = base * height
        val perimeter = 2 * (base + side)
        return GeometryResult("Area", area, "Perimeter", perimeter, "Area = b × h, Perimeter = 2(b + a)")
    }

    fun calculateCube(side: Double): GeometryResult {
        if (side <= 0.0) throw IllegalArgumentException("Side must be greater than zero")
        val volume = side * side * side
        val surfaceArea = 6 * side * side
        return GeometryResult("Volume", volume, "Surface Area", surfaceArea, "Volume = a³, Surface Area = 6a²")
    }

    fun calculateCuboid(l: Double, w: Double, h: Double): GeometryResult {
        if (l <= 0.0 || w <= 0.0 || h <= 0.0) throw IllegalArgumentException("Dimensions must be greater than zero")
        val volume = l * w * h
        val surfaceArea = 2 * (l * w + l * h + w * h)
        return GeometryResult("Volume", volume, "Surface Area", surfaceArea, "Volume = lwh, Surface Area = 2(lw + lh + wh)")
    }

    fun calculateCylinder(r: Double, h: Double): GeometryResult {
        if (r <= 0.0 || h <= 0.0) throw IllegalArgumentException("Dimensions must be greater than zero")
        val volume = PI * r * r * h
        val surfaceArea = 2 * PI * r * (r + h)
        return GeometryResult("Volume", volume, "Surface Area", surfaceArea, "Volume = πr²h, Surface Area = 2πr(r + h)")
    }

    fun calculateCone(r: Double, h: Double): GeometryResult {
        if (r <= 0.0 || h <= 0.0) throw IllegalArgumentException("Dimensions must be greater than zero")
        val slant = sqrt(r * r + h * h)
        val volume = (1.0 / 3.0) * PI * r * r * h
        val surfaceArea = PI * r * (r + slant)
        return GeometryResult("Volume", volume, "Surface Area", surfaceArea, "Volume = ⅓πr²h, Surface Area = πr(r + s)")
    }

    fun calculateSphere(r: Double): GeometryResult {
        if (radiusCheck(r)) throw IllegalArgumentException("Radius must be greater than zero")
        val volume = (4.0 / 3.0) * PI * r * r * r
        val surfaceArea = 4 * PI * r * r
        return GeometryResult("Volume", volume, "Surface Area", surfaceArea, "Volume = ⁴⁄₃πr³, Surface Area = 4πr²")
    }

    private fun radiusCheck(r: Double): Boolean = r <= 0.0
}
