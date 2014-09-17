package it.alessandrocucci.cuccifeeder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    final private static int DIALOG_LOGIN = 1;
    
    
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

                showDialog(DIALOG_LOGIN);
 
			}
 
		});
        
        feedAdapter.notifyDataSetChanged();

        
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog dialogDetails = null;

        switch (id) {
            case DIALOG_LOGIN:
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogview = inflater.inflate(R.layout.feedialog, null);

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
                dialogbuilder.setTitle("Aggiungi Feed");
                dialogbuilder.setView(dialogview);
                dialogDetails = dialogbuilder.create();

                break;
        }

        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
            case DIALOG_LOGIN:
                final AlertDialog alertDialog = (AlertDialog) dialog;
                Button loginbutton = (Button) alertDialog
                        .findViewById(R.id.btn_add);
                Button cancelbutton = (Button) alertDialog
                        .findViewById(R.id.btn_cancel);
                final EditText userName = (EditText) alertDialog
                        .findViewById(R.id.txt_name);

                final EditText editurl = (EditText) alertDialog
                        .findViewById(R.id.txt_url);

                loginbutton.setOnClickListener(new View.OnClickListener() {

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
        }
    }

}