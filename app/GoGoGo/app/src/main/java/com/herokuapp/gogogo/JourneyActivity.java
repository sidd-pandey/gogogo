package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JourneyActivity extends Activity {

    ListView listView;
    ArrayList<String> jList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String jName = jList.get(position);
                CurrentData.JOURNEY = jName;
                Intent intent = new Intent(getBaseContext(), MainScreenActivity.class);
                startActivity(intent);
            }
        });
        new JourneyListTask().execute(StaticAddress.BASE+"CreateJourneyServlet");
        setListeners();
    }

    private void setListeners(){
        ImageButton create = (ImageButton) findViewById(R.id.imageButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(JourneyActivity.this);
                dialog.setContentView(R.layout.create_journey_dialog);
                dialog.setCancelable(false);
                dialog.setTitle("Create Journey");
                Button cancel = (Button) dialog.findViewById(R.id.cancel_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button create = (Button) dialog.findViewById(R.id.create_btn);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String jName = ((TextView)dialog.findViewById(R.id.adminname)).getText().toString();
                        String jPass = ((TextView)dialog.findViewById(R.id.journey_name)).getText().toString();
                        String doj = ((TextView)dialog.findViewById(R.id.journey_pass)).getText().toString();
                        new CreateJourneyTask(jName, jPass, doj).execute(StaticAddress.BASE + "CreateJourneyServlet");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        ImageButton join  = (ImageButton) findViewById(R.id.imageButton2);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(JourneyActivity.this);
                dialog.setContentView(R.layout.join_journey_dialog);
                dialog.setCancelable(true);
                dialog.setTitle("Join Journey");
                Button cancel = (Button) dialog.findViewById(R.id.cancel_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button create = (Button) dialog.findViewById(R.id.join_btn);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aName = ((TextView) dialog.findViewById(R.id.adminname)).getText().toString();
                        String jName = ((TextView) dialog.findViewById(R.id.journey_name)).getText().toString();
                        String jPass = ((TextView) dialog.findViewById(R.id.journey_pass)).getText().toString();
                        new JoinJourneyTask(aName, jName, jPass).execute(StaticAddress.BASE + "MemberServlet");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journey, menu);
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
    private class JourneyListTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(JourneyActivity.this,"Getting journeys...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("creator",CurrentData.USER);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String item = null;
            jList.clear();
            try {
                while((item = br.readLine())!=null){
                    jList.add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            listView.setAdapter(new JourneyAdapter());
        }
    }
    private class JourneyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return jList.size();
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
            textView.setText(jList.get(position));
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(28);
            return textView;
        }
    }
    private class CreateJourneyTask extends AsyncTask<String, String, String> {

        String jName, jPass, doj;
        public CreateJourneyTask(String jName, String jPass, String doj){
            this.jName = jName;
            this.jPass = jPass;
            this.doj = doj;
        }
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(JourneyActivity.this,"Creating journey...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("Creator",CurrentData.USER);
            map.put("JName", jName);
            map.put("JPass", jPass);
            map.put("DOJ", doj);
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(getBaseContext(), "Journey Created", Toast.LENGTH_SHORT).show();
                new JourneyListTask().execute(StaticAddress.BASE+"CreateJourneyServlet");
            }
            else {
                Toast.makeText(getBaseContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class JoinJourneyTask extends AsyncTask<String, String, String> {

        String aName, jName, jPass;
        public JoinJourneyTask(String aName, String jName, String jPass){
            this.jName = jName;
            this.jPass = jPass;
            this.aName = aName;
        }
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(JourneyActivity.this,"Joining journey...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("Name", aName);
            map.put("MName", CurrentData.USER);
            map.put("JName", jName);
            map.put("JPass", jPass);
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(getBaseContext(), "Journey Joined", Toast.LENGTH_SHORT).show();
                new JourneyListTask().execute(StaticAddress.BASE+"CreateJourneyServlet");
            }
            else {
                Toast.makeText(getBaseContext(), "Join Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
