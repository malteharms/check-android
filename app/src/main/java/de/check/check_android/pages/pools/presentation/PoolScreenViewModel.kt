package de.check.check_android.pages.pools.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.check.check_android.CheckApp
import de.check.check_android.pages.pools.data.PoolState
import de.check.check_android.pages.pools.domain.PoolEvent
import de.check.core.getTimestampFromDate
import de.check.database.CheckDao
import de.check.database.tables.Pool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PoolScreenViewModel(
    private val dao: CheckDao = CheckApp.appModule.db.getDao()
): ViewModel() {

    companion object {
        private val TAG: String? = PoolScreenViewModel::class.simpleName
    }

    private val _state = MutableStateFlow(
        PoolState(pools = dao.getPools())
    )

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PoolState(pools = dao.getPools())
    )

    fun onEvent(event: PoolEvent) {
        when (event) {

            PoolEvent.AddNewPool -> {
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

            is PoolEvent.OpenPool -> {
                TODO()
            }

            is PoolEvent.RemovePool -> {
                val poolToDelete = _state.value.selectedPool
                if (poolToDelete == null) {
                    Log.e(TAG, "Internal Error: No pool selected, will not remove pool from DB.")
                    return
                }

                Log.i(TAG,"Removing pool ${poolToDelete.title} from database.")
                viewModelScope.launch {
                    dao.removePool(poolToDelete)
                    _state.update { it.copy(
                        pools = dao.getPools(),
                        isOptionsSheetOpen = false,
                        selectedPool = null
                    ) }
                }
            }

            PoolEvent.OpenSheetAddPool -> {
                _state.update { it.copy(
                    isAddingNewPool = true
                ) }
            }

            PoolEvent.CloseSheetAddPool -> {
                _state.update { it.copy(
                    isAddingNewPool = false
                ) }
            }

            is PoolEvent.SetNewPoolTitle -> {
                _state.update { it.copy(
                    newPoolTitle = event.title
                ) }
            }

            is PoolEvent.OpenSheetOptions -> {
                _state.update { it.copy(
                    isOptionsSheetOpen = true,
                    selectedPool = event.pool
                ) }
            }

            PoolEvent.CloseSheetOptions -> {
                _state.update { it.copy(
                    isOptionsSheetOpen = false,
                    selectedPool = null
                ) }
            }

        }   // when statement
    }   // onEvent()

}
