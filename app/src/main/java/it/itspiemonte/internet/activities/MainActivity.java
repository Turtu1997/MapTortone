package it.itspiemonte.internet.activities;

import android.content.pm.InstrumentationInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import it.itspiemonte.internet.R;
import utilities.Network;

public class MainActivity extends AppCompatActivity {





    private EditText query;
    private TextView url;
    private TextView response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        query = (EditText) findViewById(R.id.et_query);
        url = (TextView) findViewById(R.id.tv_url);
        response = (TextView) findViewById(R.id.tv_response);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            String githubUrl = createGithubUrl();
            String responseJson = "";
            url.setText(githubUrl);
            GithubAsyncTask task = new GithubAsyncTask();
            try {
                task.execute(new URL(githubUrl));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class GithubAsyncTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... url) {
            String responseJson = null;
            try {
                responseJson = Network.getResponseFromHttpUrl(url[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseJson;
        }

        @Override
        protected void onPostExecute(String responseJson) {
            super.onPostExecute(responseJson);
            try {
                JSONObject githubResult = new JSONObject(responseJson);
                Log.d("INTERNET total count = ", String.valueOf(githubResult.get("total_count")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            response.setText(responseJson);
        }
    }

    private String createGithubUrl() {
        String queryString = query.getText().toString();
        return Network.buildUrl(queryString).toString();
    }
}
