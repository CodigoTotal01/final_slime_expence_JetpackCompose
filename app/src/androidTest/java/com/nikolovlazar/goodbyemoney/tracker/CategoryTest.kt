package com.nikolovlazar.goodbyemoney.tracker

import androidx.compose.ui.graphics.Color
import com.nikolovlazar.goodbyemoney.features.tracker.models.Category
import org.junit.Assert.assertEquals
import org.junit.Test
class CategoryTest {

        @Test
        fun testcategoryColor() {
            // Arrange
            val expectedColor = Color(255f / 255, 255f / 255, 255f / 255)
            val category = Category("Test Category", expectedColor)

            // Act
            val resultColor = category.color

            // Assert
            assertEquals(expectedColor, resultColor)
        }

        @Test
        fun testCategoryName() {
            // Arrange
            val expectedName = "Test Category"
            val category = Category(expectedName, Color(255f / 255, 255f / 255, 255f / 255))

            // Act
            val resultName = category.name

            // Assert
            assertEquals(expectedName, resultName)
        }

}