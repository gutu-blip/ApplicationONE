package com.speca.application1;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewHolder extends RecyclerView.ViewHolder {

//View mview;
SimpleExoPlayer exoPlayer;
private PlayerView mExoplayerView;
ImageView likesbutton,downloadbtn;
TextView likesdisplay;
int likescount;
DatabaseReference likesref;

    public ViewHolder(@NonNull View itemView) {

        super(itemView);

  itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          mClickListener.onItemClick(view,getAdapterPosition());
      }
  });

  itemView.setOnLongClickListener(new View.OnLongClickListener(){

      @Override
      public boolean onLongClick(View view) {
      mClickListener.onItemLongClick(view,getAdapterPosition());
          return false;
      }}
  );

    }

    public void setLikesbuttonStatus(final String postkey){
        likesbutton =itemView.findViewById(R.id.like_btn);
        //likesdisplay = itemView.findViewById(R.id.likes_textview);
     //downloadbtn =itemView.findViewById(R.id.download_button_viewholder);
        likesref = FirebaseDatabase.getInstance().getReference("likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId =user.getUid();
        String likes ="likes";

    likesref.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(snapshot.child(postkey).hasChild(userId)){
                likescount =(int) snapshot.child(postkey).getChildrenCount();
                likesbutton.setImageResource(R.drawable.ic_favorite_red);
            //likesdisplay.setText(Integer.toString(likescount)+likes);
            }else{
                likescount =(int) snapshot.child(postkey).getChildrenCount();
                likesbutton.setImageResource(R.drawable.ic_favorite_grey);
               // likesdisplay.setText(Integer.toString(likescount)+likes);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }

    public void setVideo(final Application ctx,String name,final String videourl) {
        TextView mtextview=itemView.findViewById(R.id.Titletv);
        mExoplayerView = itemView.findViewById(R.id.exoplayer_view);

        mtextview.setText(name);
                try{
                    BandwidthMeter bandwidthMeter =new DefaultBandwidthMeter.Builder(ctx).build();
                    TrackSelector trackSelector =new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    exoPlayer=(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(ctx);
                    Uri video=Uri.parse(videourl);
                    DefaultHttpDataSourceFactory dataSourceFactory= new DefaultHttpDataSourceFactory("videos");
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource= new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                    mExoplayerView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(false);

                }catch(Exception e){
                    Log.e("ViewHolder","exoplayer error"+ e.toString());
                }
    }

    private ViewHolder.Clicklistener mClickListener;

    public interface Clicklistener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClicklistener(ViewHolder.Clicklistener clicklistener){
        mClickListener=clicklistener;
    }
}
