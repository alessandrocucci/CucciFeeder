package it.alessandrocucci.cuccifeeder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class AddFeed extends Activity {
	
	ListView listView;
    FeedAdapter feedAdapter;
    

    ImageButton addNew;
    DatabaseHandler db;

    String feedName, name;

    final private static int DIALOG_ADD = 1;
    final private static int DIALOG_SETTING = 2;

    public int MAX_POST = 10;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        db = new DatabaseHandler(this);
        
        listView = (ListView) findViewById(R.id.feedListView);
        
        feedAdapter = new FeedAdapter(this, R.layout.feedrow,
                new ArrayList<Feed>());
        
        List<Feed> feeds = db.getAllFeeds();
		 
        for (Feed cn : feeds) {

            feedName = cn.getName();
            feedAdapter.add(cn);

        
        }
        
        listView.setAdapter(feedAdapter);
        listView.setDivider(null);
        
        

        addNew = (ImageButton) findViewById(R.id.button1);
        
        addNew.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {

                showDialog(DIALOG_ADD);
 
			}
 
		});
        
        feedAdapter.notifyDataSetChanged();

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                showDialog(DIALOG_SETTING);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog dialogDetails = null;

        switch (id) {
            case DIALOG_ADD:
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogview = inflater.inflate(R.layout.feedialog, null);

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
                dialogbuilder.setTitle("Aggiungi Feed");
                dialogbuilder.setView(dialogview);
                dialogDetails = dialogbuilder.create();

                break;

            case DIALOG_SETTING:
                LayoutInflater inflatersetting = LayoutInflater.from(this);
                View dialogsettingview = inflatersetting.inflate(R.layout.settingdialog, null);

                AlertDialog.Builder dialogbuildersetting = new AlertDialog.Builder(this);
                dialogbuildersetting.setTitle("Numero di Articoli");
                dialogbuildersetting.setView(dialogsettingview);
                dialogDetails = dialogbuildersetting.create();

                break;
        }

        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
            case DIALOG_ADD:
                final AlertDialog alertDialog = (AlertDialog) dialog;
                Button addbutton = (Button) alertDialog
                        .findViewById(R.id.btn_add);
                Button cancelbutton = (Button) alertDialog
                        .findViewById(R.id.btn_cancel);
                final EditText userName = (EditText) alertDialog
                        .findViewById(R.id.txt_name);

                final EditText editurl = (EditText) alertDialog
                        .findViewById(R.id.txt_url);

                addbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        String namedialog = userName.getText().toString();
                        if (namedialog == null) namedialog = name;
                        String url = editurl.getText().toString();

                        feedAdapter.clear();
                        /**
                         * CRUD Operations
                         * */


                        db.addFeed(new Feed(namedialog, url));



                        List<Feed> feeds = db.getAllFeeds();

                        for (Feed cn : feeds) {

                            feedAdapter.add(cn);


                        }

                        feedAdapter.notifyDataSetChanged();

                    }
                });

                cancelbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;

            case DIALOG_SETTING:
                final AlertDialog alertSetting = (AlertDialog) dialog;
                Button okbutton = (Button) alertSetting
                        .findViewById(R.id.btn_ok);

                final EditText postNumber = (EditText) alertSetting
                        .findViewById(R.id.txt_number);

                okbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertSetting.dismiss();
                        MAX_POST = Integer.parseInt(postNumber.getText().toString());

                        if (MAX_POST == 0) MAX_POST = 10;

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddFeed.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("PostMax",MAX_POST);
                        editor.apply();

                    }
                });


                break;
        }
    }

}