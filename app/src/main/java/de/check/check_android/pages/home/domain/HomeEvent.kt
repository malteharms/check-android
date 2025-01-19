package de.check.check_android.pages.home.domain

sealed interface HomeEvent {

    data object OpenSheetAddPool : HomeEvent
    data object CloseSheetAddPool : HomeEvent

    data class SetNewPoolTitle(val title: String): HomeEvent

    data object AddNewPool : HomeEvent
    data class OpenPool(val id: Int) : HomeEvent
    data class RemovePool(val id: Int) : HomeEvent

}