package com.nikolovlazar.goodbyemoney.tracker
import androidx.compose.ui.graphics.Color
import com.nikolovlazar.goodbyemoney.features.tracker.models.Category
import com.nikolovlazar.goodbyemoney.features.tracker.models.Expense
import com.nikolovlazar.goodbyemoney.features.tracker.models.Recurrence
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class ExpenseTest {

    @Test
    fun testExpenseAmount() {
        // Arrange
        val expectedAmount = 100.0
        val expense = Expense(
            amount = expectedAmount,
            recurrence = Recurrence.Daily,
            date = LocalDateTime.now(),
            note = "Test Note",
            category = Category("Test Category", Color(1f, 1f, 1f))
        )

        // Act
        val resultAmount = expense.amount

        // Assert
        assertEquals(expectedAmount, resultAmount, 0.0)
    }

    @Test
    fun testExpenseRecurrence() {
        // Arrange
        val expectedRecurrence = Recurrence.Daily
        val expense = Expense(
            amount = 100.0,
            recurrence = expectedRecurrence,
            date = LocalDateTime.now(),
            note = "Test Note",
            category = Category("Test Category", Color(1f, 1f, 1f))
        )

        // Act
        val resultRecurrence = expense.recurrence

        // Assert
        assertEquals(expectedRecurrence, resultRecurrence)
    }

}