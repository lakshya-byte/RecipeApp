package com.example.recipeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _categorieState = MutableStateFlow(RecipeState())
    val categorieState = _categorieState.asStateFlow()


    init {
        fetchCategories()
    }


    private fun fetchCategories() {
        viewModelScope.launch {
            try {

                val response = recipeService.getCategories()
                _categorieState.value = _categorieState.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )


            } catch (e: Exception) {
                _categorieState.value = _categorieState.value.copy(
                    error = "Error fetching Categories ${e.message}",
                    loading = false
                )
            }
        }
    }


    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )

}