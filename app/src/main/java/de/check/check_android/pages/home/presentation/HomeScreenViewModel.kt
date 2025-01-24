package de.check.check_android.pages.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.check.check_android.CheckApp
import de.check.check_android.pages.home.data.HomeState
import de.check.check_android.pages.home.domain.HomeEvent
import de.check.core.getTimestampFromDate
import de.check.database.CheckDao
import de.check.database.tables.Pool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeScreenViewModel(
    private val dao: CheckDao = CheckApp.appModule.db.getDao()
): ViewModel() {

    companion object {
        private val TAG: String? = HomeScreenViewModel::class.simpleName
    }

    private val _state = MutableStateFlow(
        HomeState(pools = dao.getPools())
    )

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState(pools = dao.getPools())
    )

    fun onEvent(event: HomeEvent) {
        when (event) {

            HomeEvent.AddNewPool -> {
                val newPool = Pool(
                    title = _state.value.newPoolTitle,
                    creator = 0,    // TODO replace with account ID
                    updated = getTimestampFromDate(LocalDateTime.now())
                )

                Log.d(TAG, "Adding new pool ${newPool.title} to database")
                viewModelScope.launch {
                    dao.insertPool(newPool)
                    _state.update { it.copy(
                        pools = dao.getPools(),
                        isAddingNewPool = false,
                        newPoolTitle = ""
                    ) }
                }
            }

            is HomeEvent.OpenPool -> {
                TODO()
            }

            is HomeEvent.RemovePool -> {
                Log.i(TAG,"Removing pool with ID ${event.id} from database")
                viewModelScope.launch {
                    dao.removePool(event.id)        // Database update
                    _state.update { it.copy(
                        pools = dao.getPools()      // Update pools, title reset not necessary
                    ) }
                }
            }

            HomeEvent.OpenSheetAddPool -> {
                _state.update { it.copy(
                    isAddingNewPool = true
                ) }
            }

            HomeEvent.CloseSheetAddPool -> {
                _state.update { it.copy(
                    isAddingNewPool = false
                ) }
            }

            is HomeEvent.SetNewPoolTitle -> {
                _state.update { it.copy(
                    newPoolTitle = event.title
                ) }
            }

        }   // when statement
    }   // onEvent()

}
