package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.StatusLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ToDoActivity extends Activity {

    ListView listView;
    ArrayList<String> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        listView = (ListView) findViewById(R.id.todolist);
        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ToDoActivity.this);
                dialog.setTitle("Add To-Do Task");
                dialog.setContentView(R.layout.todoitem_dialog);
                Button button = (Button) dialog.findViewById(R.id.addnewtodo);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = ((TextView)dialog.findViewById(R.id.newtodo)).getText().toString();
                        dialog.dismiss();
                        new AddToDoTask(task).execute(StaticAddress.BASE +"ToDoListServlet");

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchToDoTask().execute(StaticAddress.BASE+"ToDoListServlet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do, menu);
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
    private class ToDoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.todo_item, null);
            final String item = itemList.get(position);
            ((TextView)view.findViewById(R.id.todoItem)).setText(item.split("####")[0]);
            if(item.split("####")[1].equals("1")){
                ((CheckBox)view.findViewById(R.id.checkbox)).setChecked(true);
            }
            else{
                ((CheckBox)view.findViewById(R.id.checkbox)).setChecked(false);
            }
            ((CheckBox)view.findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //change the check
                    String state = "0";
                    if(isChecked)
                        state = "1";

                    new ChangeStateTask(((TextView)view.findViewById(R.id.todoItem)).getText().toString(), state).execute(StaticAddress.BASE + "ToDoListItemServlet");
                }
            });
            final TextView textView = (TextView) view.findViewById(R.id.todoItem);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(ToDoActivity.this);
                    dialog.setTitle("Task Completed By");
                    dialog.setContentView(R.layout.todomemberlist_dialog);
                    TextView memberListView = (TextView) dialog.findViewById(R.id.completed);
                    new FetchMembersTask(textView.getText().toString(),memberListView).execute(StaticAddress.BASE+"ToDoListItemServlet");
                    dialog.show();
                }
            });
            return view;
        }
    }
    private class FetchToDoTask extends AsyncTask<String, String, String>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ToDoActivity.this, "Fetching List...", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName", CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String output = null;
            itemList.clear();
            try {
                while((output=br.readLine())!=null){
                    itemList.add(output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            listView.setAdapter(new ToDoAdapter());
        }
    }
    private class AddToDoTask extends AsyncTask<String, String, String>{

        private ProgressDialog progressDialog;

        private String todoItem;
        AddToDoTask(String todoItem){
            this.todoItem = todoItem;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ToDoActivity.this, "Adding Item...", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName", CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("todoItem", todoItem);
            BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            String output = null;
            try {
                while((output=br.readLine())!=null){
                    return output;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(getBaseContext(),"Added!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getBaseContext(),"Failed!",Toast.LENGTH_SHORT).show();
            }
            new FetchToDoTask().execute(StaticAddress.BASE+ "ToDoListServlet");
        }
    }
    private class ChangeStateTask extends AsyncTask<String, String, String>{

        private String todoItem;
        private String state;
        ChangeStateTask(String todoItem, String state){
            this.state = state;
            this.todoItem = todoItem;
        }
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName",CurrentData.USER);
            map.put("JName",CurrentData.JOURNEY);
            map.put("todoItem",todoItem);
            map.put("state", state);
            BufferedReader br = HttpUtility.openPostConnection(params[0],map);
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private class FetchMembersTask extends AsyncTask<String, String, String>{

        private String todoItem;
        private TextView memberListView;
        FetchMembersTask(String todoItem, TextView textView){
            this.todoItem = todoItem;
            memberListView = textView;
        }
        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName", CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("todoItem", todoItem);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            StringBuilder builder = new StringBuilder("");
            String line = null;
            try {
                while((line = br.readLine())!=null){
                    if(line.split("####")[1].equals("1")){
                        builder.append(line.split("####")[0]+"\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                memberListView.setText("No One has completed yet!");
                return;
            }
           memberListView.setText(s);
        }
    }
}
