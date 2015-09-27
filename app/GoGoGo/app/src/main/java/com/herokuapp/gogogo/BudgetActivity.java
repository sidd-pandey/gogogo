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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BudgetActivity extends Activity {

    ListView budget_list;
    ArrayList<String> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        budget_list = (ListView) findViewById(R.id.budget_list);
        Button button = (Button) findViewById(R.id.new_item);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(BudgetActivity.this);
                dialog.setTitle("New Item");
                dialog.setContentView(R.layout.budget_item_new);
                Button add_item = (Button) dialog.findViewById(R.id.add_item_button);
                add_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) dialog.findViewById(R.id.new_item);
                        String itemName = editText.getText().toString();
                        editText = (EditText) dialog.findViewById(R.id.new_value);
                        Long itemValue = Long.parseLong(editText.getText().toString());
                        new CreateBudgetTask(itemName, itemValue).execute(StaticAddress.BASE+"BudgetServlet");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        new FetchBudgetList().execute(StaticAddress.BASE + "BudgetServlet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
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
    private class BudgetAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
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
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.budeget_item, null);
            String item = items.get(position).split("####")[0];
            String value = items.get(position).split("####")[1];
            ((TextView)view.findViewById(R.id.budget_item)).setText(item);
            ((TextView)view.findViewById(R.id.item_value)).setText(value);
            return view;
        }
    }
    private class FetchBudgetList extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BudgetActivity.this,"Getting Budget...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName",CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            BufferedReader br = HttpUtility.openGetConnection(params[0], map);
            String item = null;
            items.clear();
            try {
                while((item = br.readLine())!=null){
                    items.add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return items.remove(items.size()-1);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            ((TextView)findViewById(R.id.sum_value)).setText(s.split("####")[0]);
            budget_list.setAdapter(new BudgetAdapter());
        }
    }
    private class CreateBudgetTask extends AsyncTask<String, String, String> {

        String itemName;
        Long itemValue;
        CreateBudgetTask(String itemName, Long itemValue){
            this.itemName = itemName;
            this.itemValue = itemValue;
        }
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BudgetActivity.this,"Adding Item...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("UName",CurrentData.USER);
            map.put("JName", CurrentData.JOURNEY);
            map.put("budgetItem", itemName);
            map.put("value", itemValue.toString());
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
            new FetchBudgetList().execute(StaticAddress.BASE + "BudgetServlet");
        }
    }
}
