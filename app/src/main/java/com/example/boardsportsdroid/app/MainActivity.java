package com.example.boardsportsdroid.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceHoderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface GitHubService {
        @GET("/users/{user}")
        void listRepos(@Path("user") String user, Callback<GithubUser> callback);
    }

    public class GithubUser {
        public String login;
    }

    public static class PlaceHoderFragment extends Fragment implements View.OnClickListener {

        TextView scrapedContent;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            scrapedContent = (TextView) rootView.findViewById(R.id.scraped_content);
            Button scrapeButton = (Button) rootView.findViewById(R.id.button_scrape);

            scrapeButton.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.github.com")
                    .build();

            GitHubService service = restAdapter.create(GitHubService.class);
            service.listRepos("baskint", new Callback<GithubUser>() {
                @Override
                public void success(GithubUser s, Response response) {
                    scrapedContent.setText(s.login);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Retrofit", error.getMessage());
                }
            });

        }
    }
}

