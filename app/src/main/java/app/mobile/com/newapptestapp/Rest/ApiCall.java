package app.mobile.com.newapptestapp.Rest;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import app.mobile.com.newapptestapp.ApiClient;
import app.mobile.com.newapptestapp.Utility.Constants;
import app.mobile.com.newapptestapp.model.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {
    private ProgressBar mProgressBar;
    private Delegate delegate;
    private Context context;
    private String query;


    public ApiCall( Delegate delegate, Context context,String query) {
        this.delegate = delegate;
        this.context = context;
        this.query=query;
    }

    public void executeApi(){


        ApiCallInterFace apiCallInterFace = ApiClient.getClient().create(ApiCallInterFace.class);
        Call<Movie> call = apiCallInterFace.getMovieList(Constants.API_KEY,query);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                int statusCode = response.code();

                if(response.isSuccessful()) {
                    delegate.onSuccess(response.body());
                } else {
                    delegate.onFailure("On Failure - " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                delegate.onFailure("On Failure - "+t.getMessage());

            }
        });


    }
    public void executeLoadMoreApi(int pageNumber){


        ApiCallInterFace apiCallInterFace = ApiClient.getClient().create(ApiCallInterFace.class);
        Call<Movie> call = apiCallInterFace.getMovieList(Constants.API_KEY,query,pageNumber);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                int statusCode = response.code();

                if(response.isSuccessful()) {
                    Log.e("onsuccess","SUCCESS");
                    delegate.onSuccess(response.body());
                } else {
                    Log.e("ON ERROR",""+"ON E");
                    delegate.onFailure("On Failure - " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                delegate.onFailure("On Failure - "+t.getMessage());
                Log.e("ON FAILURE","ON E");

            }
        });


    }

    public interface Delegate {
        void onSuccess(Movie movie);
        void onFailure(Object t);
    }
}
