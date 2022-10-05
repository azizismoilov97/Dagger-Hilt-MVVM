package admiral.group.daggerhiltmvvm.di

import admiral.group.daggerhiltmvvm.database.BlogDao
import admiral.group.daggerhiltmvvm.database.CacheMapper
import admiral.group.daggerhiltmvvm.network.BlogApi
import admiral.group.daggerhiltmvvm.network.BlogMapper
import admiral.group.daggerhiltmvvm.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        blogDao: BlogDao,
        blogApi: BlogApi,
        cacheMapper: CacheMapper,
        blogMapper: BlogMapper
    ): MainRepository {
        return MainRepository(blogDao, blogApi, cacheMapper, blogMapper)
    }
}