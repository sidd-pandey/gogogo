package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TopicActivity extends Activity {

    ListView listView;
    ArrayList<String> topicList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new TopicAdapter());
        new TopicListTask().execute(StaticAddress.BASE + "TopicServlet");
        ImageButton create_button = (ImageButton) findViewById(R.id.create_topic_btn);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(TopicActivity.this);
                dialog.setContentView(R.layout.create_topic_dialog);
                dialog.setTitle("New Topic");
                Button create = (Button) dialog.findViewById(R.id.create_topic_btn);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tName = ((TextView)dialog.findViewById(R.id.topic_name)).getText().toString();
                        new CreateTopicTask(tName).execute(StaticAddress.BASE+"TopicServlet");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentData.TOPIC = topicList.get(position);
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
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

    private class TopicListTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopicActivity.this,"Getting Topics...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("Creator",CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String item = null;
            topicList.clear();
            try {
                while((item = br.readLine())!=null){
                    topicList.add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            listView.setAdapter(new TopicAdapter());
        }
    }
    private class CreateTopicTask extends AsyncTask<String, String, String> {

        String tName;
        CreateTopicTask(String tName){
            this.tName = tName;
        }
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopicActivity.this,"Creating Topics...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("Creator",CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("TName", tName);
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            try{
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            new TopicListTask().execute(StaticAddress.BASE+"TopicServlet");
        }
    }

    private class TopicAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return topicList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getBaseContext());
            textView.setText(topicList.get(position));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(28);
            return textView;
        }
    }
}
