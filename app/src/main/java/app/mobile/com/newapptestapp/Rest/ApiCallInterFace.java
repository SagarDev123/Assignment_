package app.mobile.com.newapptestapp.Rest;

import app.mobile.com.newapptestapp.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallInterFace {
    @GET("/3/search/movie")
    Call<Movie> getMovieList(@Query("api_key") String apiKey,@Query("query") String query);
}
