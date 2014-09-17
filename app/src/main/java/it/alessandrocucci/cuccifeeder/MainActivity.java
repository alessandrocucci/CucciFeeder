package it.alessandrocucci.cuccifeeder;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;




public class MainActivity extends ListActivity {

    public int MAX_POST = 10; //Qui devi inserire il numero massimo di post che vuoi visualizzare
    public String URL_FEED = "URL_DEL_SITO";
    public String NAME_FEED;



    String[] titles = new String[MAX_POST];
    String[] dates = new String[MAX_POST];
    String[] links = new String[MAX_POST];
    String[] images = new String[MAX_POST];



    private ProgressDialog prgDialog;

    public static final int progress_bar_type = 0;
    ListView lv;
    ListAdapter adapter;


    public class MyCustomAdapter extends ArrayAdapter<String> {



        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.list_row, parent, false);

            TextView label=(TextView)row.findViewById(R.id.title);
            TextView date=(TextView)row.findViewById(R.id.date);
            label.setText(titles[position]);
            date.setText(dates[position]);
            ImageView icon=(ImageView)row.findViewById(R.id.featured);
            UrlImageViewHelper.setUrlDrawable(icon, images[position]);

            return row;
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            URL_FEED = extras.getString("GAME_URL");
            NAME_FEED = extras.getString("GAME_NAME");
        }

        this.setTitle(NAME_FEED);

        new ParseData().execute();
        adapter = new MyCustomAdapter(this, R.layout.list_row, links);
        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setDivider(null);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String selection = l.getItemAtPosition(position).toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selection));
        startActivity(browserIntent);
    }


    protected static String getElementValue(Element parent,String label) {
        return getCharacterDataFromElement((Element)parent.getElementsByTagName(label).item(0));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Attendi mentre scarico i dati...");
                prgDialog.setIndeterminate(true);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prgDialog.setCancelable(true);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:

                new ParseData().execute();
                adapter = new MyCustomAdapter(this, R.layout.list_row, links);
                setListAdapter(adapter);
                ListView lv = getListView();
                lv.setDivider(null);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    class ParseData extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showDialog(progress_bar_type);
        }


        @Override
        protected String doInBackground(String... f_url) {
            try{

                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                URL u = new URL(URL_FEED);
                Document doc = builder.parse(u.openStream());




                NodeList nodes = doc.getElementsByTagName("item");

                if (nodes.getLength() < MAX_POST) MAX_POST =  nodes.getLength();

                for(int i=0;i<MAX_POST;i++) {
                    Element element = (Element)nodes.item(i);


                    titles[i] = getElementValue(element,"title");

                    links[i] = getElementValue(element,"link");

                    String format = "EEE, dd MMM yyyy kk:mm:ss Z";
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss z", Locale.US);

                    Date formatedDate = sdf.parse(getElementValue(element,"pubDate"));

                    Calendar c= Calendar.getInstance();
                    c.setTime(formatedDate);

                    dates[i] = ""+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);

                    String s = getElementValue(element, "description");
                    Matcher matcher = urlPattern.matcher(s);
                    while (matcher.find())
                    {
                        String urlimage=matcher.group(1);
                        images[i] = urlimage.toString();
                    }

                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }





        @Override
        protected void onPostExecute(String file_url) {

            dismissDialog(progress_bar_type);
            setListAdapter(adapter);

        }
    }



    private static String getCharacterDataFromElement(Element e) {
        try {
            Node child = e.getFirstChild();
            if(child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
        } catch(Exception ex) {
        }
        return "";
    }


    private static final Pattern urlPattern = Pattern.compile(
            ".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);

}

