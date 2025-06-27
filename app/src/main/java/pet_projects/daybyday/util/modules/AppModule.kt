package pet_projects.daybyday.util.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pet_projects.daybyday.database.AppDatabase
import javax.inject.Singleton
import android.content.Context
import pet_projects.daybyday.database.daos.HabitDao
import pet_projects.daybyday.repositories.HabitRepository
import pet_projects.daybyday.repositories.HabitRepositoryImpl
import pet_projects.daybyday.util.schedulers.HabitScheduler
import pet_projects.daybyday.util.schedulers.HabitSchedulerImpl

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository {
        return HabitRepositoryImpl(habitDao)
    }

    @Provides
    @Singleton
    fun provideHabitScheduler(@ApplicationContext context: Context): HabitScheduler {
        return HabitSchedulerImpl(context)
    }
}