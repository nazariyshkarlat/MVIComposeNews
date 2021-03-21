package com.example.myapplication

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.myapplication.di.DI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import org.koin.core.module.Module
import org.koin.dsl.ModuleDeclaration
import java.text.SimpleDateFormat
import java.util.*

fun configModule(createdAtStart: Boolean = false, override: Boolean = false, configuration: DI.Config, moduleDeclaration: ModuleDeclaration): Module {
    val module = Module(createdAtStart, override)
    moduleDeclaration(module)
    return module
}

fun <T> SavedStateHandle.getMutableStateOf(
    key: String,
    default: T,
    save: (T) -> Bundle,
    restore: (Bundle) -> T
): MutableState<T> {
    val bundle: Bundle? = get(key)
    val initial = if (bundle == null) { default } else { restore(bundle) }
    val state = mutableStateOf(initial)
    setSavedStateProvider(key) {
        save(state.value)
    }
    return state
}

fun formatDate(date: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT)
    return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(format.parse(date))
}