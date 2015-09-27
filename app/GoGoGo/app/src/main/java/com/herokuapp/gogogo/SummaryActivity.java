package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SummaryActivity extends Activity {

    TextView summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        summary = (TextView) findViewById(R.id.details);
        new FetchSummaryTask().execute(StaticAddress.BASE+"JourneySummaryServlet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class FetchSummaryTask extends AsyncTask<String, String, String>{

        ProgressDialog dialog;
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName",CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            BufferedReader br = HttpUtility.openPostConnection(params[0],map);
            String output = null;
            String sum = "";
            try {
                while((output=br.readLine())!=null){
                    sum += output +"\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sum;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SummaryActivity.this);
            dialog.setTitle("Downloading Summary");
            dialog.setMessage("Please wair...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            summary.setText(s);
            dialog.dismiss();
        }
    }
}
