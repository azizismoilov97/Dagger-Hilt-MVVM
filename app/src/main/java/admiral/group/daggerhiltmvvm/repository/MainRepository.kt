package admiral.group.daggerhiltmvvm.repository

import admiral.group.daggerhiltmvvm.database.BlogDao
import admiral.group.daggerhiltmvvm.database.CacheMapper
import admiral.group.daggerhiltmvvm.model.Blog
import admiral.group.daggerhiltmvvm.network.BlogApi
import admiral.group.daggerhiltmvvm.network.BlogMapper
import admiral.group.daggerhiltmvvm.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository
constructor(
    private val blogDao: BlogDao,
    private val blogApi: BlogApi,
    private val cacheMapper: CacheMapper,
    private val blogMapper: BlogMapper
) {
    suspend fun getBlog(): Flow<DataState<List<Blog>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        try {
            val networkBlogs = blogApi.get()
            val blogs = blogMapper.mapFromEntityList(networkBlogs)
            for (blog in blogs) {
                blogDao.insert(cacheMapper.mapToEntity(blog))
            }
            val cachedBlogs = blogDao.get()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBlogs)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}