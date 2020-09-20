package com.speca.application1;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.speca.application1.R;
public class VideosActivity extends AppCompatActivity {

    private  static final int PERMISSION_STORAGE_CODE =1000;
    DatabaseReference reference,likesreference;
    RecyclerView MrecyclerView;
    FirebaseDatabase database;
    String name,url,downloadurl,desc;
    Boolean likechecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);


        MrecyclerView=findViewById(R.id.recyclerview_video);
        MrecyclerView.setHasFixedSize(true);
        MrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        reference=database.getReference("videos");
        likesreference =database.getReference("likes");

}

    private void firebaseSearch(String searchtext){
        String query=searchtext.toLowerCase();
        Query firebaseQuery =reference.orderByChild("search").startAt(query).endAt(query+"\uf8ff");

        FirebaseRecyclerOptions<Member> options=
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(firebaseQuery,Member.class)
                        .build();

        FirebaseRecyclerAdapter<Member,ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {
                        holder.setVideo(getApplication(),model.getName(),model.getVideourl());

             holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                 @Override
                 public void onItemClick(View view, int position) {

                  name = getItem(position).getName();
                  url =getItem(position).getVideourl();
                   //  desc =getItem(position).getDescription();

                  Intent intent =new Intent(VideosActivity.this,FullscreenActivity.class);
                 intent.putExtra("nam",name);
                 intent.putExtra("ur",url);
                 //intent.putExtra("des",desc);

                 startActivity(intent);
                 }

                 @Override
                 public void onItemLongClick(View view, int position) {

                 }
             });
                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item,parent,false);

                        return new ViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        MrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Member> options=
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(reference,Member.class)
                        .build();

        FirebaseRecyclerAdapter<Member,ViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                         final String currentUserId =user.getUid();
                        final String postkey=getRef(position).getKey();

                        holder.setVideo(getApplication(),model.getName(),model.getVideourl());
                        holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                name = getItem(position).getName();
                                url =getItem(position).getVideourl();
                                //desc =getItem(position).getDescription();

                                Intent intent =new Intent(VideosActivity.this,FullscreenActivity.class);

                                intent.putExtra("nam",name);
                                intent.putExtra("ur",url);
                                //intent.putExtra("des",desc);

                                startActivity(intent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                        /*
                   holder.downloadbtn.setOnClickListener(new View.OnClickListener(){

                       @Override
                       public void onClick(View view) {
                           if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
                               if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                       PackageManager.PERMISSION_DENIED) {
                                   String permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);

                                   requestPermissions(new String[]{permission}, PERMISSION_STORAGE_CODE);
                               } else {
                                   downloadurl = getItem(position).getVideourl();
                                   startDownloading(downloadurl);
                               }
                           }else{
                                   startDownloading(downloadurl);

                               }
                           }
                   });
*/

                    holder.setLikesbuttonStatus(postkey);
                    holder.likesbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            likechecker =true;

                            ValueEventListener valueEventListener = likesreference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (likechecker.equals(true)) {
                                        if (snapshot.child(postkey).hasChild(currentUserId)) {
                                            likesreference.child(postkey).child(currentUserId).removeValue();
                                            likechecker = false;
                                        } else {
                                            likesreference.child(postkey).child(currentUserId).setValue(true);
                                            likechecker = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item,parent,false);

                        return new ViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        MrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

   /* private void startDownloading(String downloadurl) {

        DownloadManager.Request request =new DownloadManager.Request(Uri.parse(downloadurl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);

        request.setTitle("Download");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

    DownloadManager manager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
    manager.enqueue(request);

    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){

            case PERMISSION_STORAGE_CODE:{

                if(grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){

                    startDownloading(downloadurl);
                }
            else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }*/
}
