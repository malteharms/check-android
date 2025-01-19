package de.check.check_android.di

import android.content.Context

import de.check.database.CheckDatabase


interface AppModule{
    val db: CheckDatabase
}

class AppModuleImpl(
    context: Context
) : AppModule {
    override val db by lazy {
        CheckDatabase.getDatabase(context)
    }

    // TODO define other app-wide service provider
}
