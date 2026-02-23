package org.example.views

import org.example.views.StorageUi

sealed class StorageListState {
    object Empty : StorageListState()
    data class Success(val storages: List<StorageUi>) : StorageListState()
}

sealed class StorageState {
    object Success : StorageState()
    data class Error(val message: String) : StorageState()
}