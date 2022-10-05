package admiral.group.daggerhiltmvvm.network

import retrofit2.http.GET

interface BlogApi {

    @GET("blogs")
    suspend fun get(): List<BlogObjectResponse>
}