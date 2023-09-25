package mohammadreza.ghavidel.noteapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mohammadreza.ghavidel.noteapp.core.AppDatabase
import mohammadreza.ghavidel.noteapp.database.notes.NoteDao

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    fun provideAuthorDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

}