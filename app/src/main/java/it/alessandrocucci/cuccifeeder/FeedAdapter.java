package it.alessandrocucci.cuccifeeder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter<Feed> {

        private int resource;
        private LayoutInflater inflater;
        DatabaseHandler db;

        public FeedAdapter(Context context, int resourceId, List<Feed> objects) {
                super(context, resourceId, objects);
                resource = resourceId;
                inflater = LayoutInflater.from(context);
                db = new DatabaseHandler(context);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {


                final Feed feed = getItem(position);

                ViewHolder holder;

                if (v == null) {
                        v = inflater.inflate(resource, parent, false);
                        holder = new ViewHolder();
                        holder.nameTextView = (TextView) v.findViewById(R.id.feedName);
                        holder.deleteFeed = (ImageButton) v.findViewById(R.id.deleteFeedButton);
                        holder.viewFeed = (ImageButton) v.findViewById(R.id.viewFeedButton);
                        
                        
                        
                        v.setTag(holder);
                } else {
                        holder = (ViewHolder) v.getTag();
                }

                
                holder.nameTextView.setText(feed.getName());
                holder.viewFeed.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                    	int feedId = feed.getID();
                    	String feedName = feed.getName();
                        String feedUrl = feed.getUrl();
                    	Intent intent = new Intent(view.getContext(), MainActivity.class);
                    	intent.putExtra("GAME_ID", feedId);
                    	intent.putExtra("GAME_NAME", feedName);
                        intent.putExtra("GAME_URL", feedUrl);
                    	view.getContext().startActivity(intent);
                    }
                });
                
                holder.deleteFeed.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(getContext())
                                .setTitle("Elimina")
                                .setMessage("Sei sicuro di voler eliminare " + feed.getName() + "?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        db.deleteFeed(feed);
                                        remove(feed);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .show();


                    }
                });
                         

                return v;
        }

        private static class ViewHolder {
                TextView nameTextView;
                ImageButton viewFeed;
                ImageButton deleteFeed;
                
        }
}