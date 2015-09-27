package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends Activity {

    private ListView listView;
    private EditText editText;
    private boolean paused = false;
    ArrayList<String> chat = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listView = (ListView) findViewById(R.id.listView);
        listView.setClickable(false);
        editText = (EditText) findViewById(R.id.message);
        Button send = (Button) findViewById(R.id.send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendChatTask(editText.getText().toString()).execute(StaticAddress.BASE+"ChatServlet");
                editText.setText("");
                editText.setHint("Type Here");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        new LoadChatTask().execute(StaticAddress.BASE+"ChatServlet");
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.summary_opt) {
            final Dialog dialog = new Dialog(ChatActivity.this);
            dialog.setTitle("Summary");
            dialog.setContentView(R.layout.summary_dialog);
            final EditText summary = (EditText) dialog.findViewById(R.id.summary_text);
            new FetchSummaryTask(summary).execute(StaticAddress.BASE+"SummaryServlet");
            editText.setEnabled(false);
            Button button = (Button) dialog.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!summary.isEnabled()){
                        summary.setEnabled(true);
                    }
                }
            });
            button = (Button) dialog.findViewById(R.id.button2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SaveSummaryTask(summary).execute(StaticAddress.BASE+"SummaryServlet");
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class LoadChatTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<>();
            map.put("Creator", CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("TName", CurrentData.TOPIC);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String msg = null;
            chat.clear();
            try {
                while((msg = br.readLine())!=null){
                    chat.add(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(new ChatAdapter());
            listView.setSelection(chat.size() - 1);
            if(!paused){
                new LoadChatTask().execute(StaticAddress.BASE+"ChatServlet");
            }
        }
    }
    private class SendChatTask extends AsyncTask<String, String, String>{

        String msg;
        SendChatTask(String msg){
            this.msg = msg;
        }
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("Creator", CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("TName", CurrentData.TOPIC);
            map.put("msg", msg);
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
    private class FetchSummaryTask extends AsyncTask<String, String, String>{

        EditText editText;
        FetchSummaryTask(EditText editText){
            this.editText = editText;
        }
        @Override
        protected String doInBackground(String... params) {
            Map<String, String > map = new HashMap<>();
            map.put("JName", CurrentData.JOURNEY);
            map.put("UName", CurrentData.USER);
            map.put("TName", CurrentData.TOPIC);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String line = null, msg = "";
            try {
                while((line = br.readLine())!=null){
                    msg += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
           editText.setText(s);
        }
    }
    private class SaveSummaryTask extends AsyncTask<String, String, String>{

        EditText msg;
        SaveSummaryTask(EditText msg){
            this.msg = msg;
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("JName",CurrentData.JOURNEY);
            map.put("UName", CurrentData.USER);
            map.put("TName", CurrentData.TOPIC);
            map.put("summary", msg.getText().toString());
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            String output = null;
            try {
                while((output = br.readLine())!=null ){
                    return output;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("true")){
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class ChatAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return chat.size();
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
            textView.setText(chat.get(position));
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            return textView;
        }
    }
}
