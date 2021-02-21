package com.example.ppoya.stream;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import javaPackage.Item;

public class MainActivity extends AppCompatActivity {

    private int selectedposition = 0;
    private String selSong = null;

    SimpleExoPlayer player;
    DataSource.Factory dataSourceFactory;
    MediaSource mediaSource;
    BandwidthMeter bandwidthMeter;
    TrackSelection.Factory trackSelectionFactory;
    TrackSelector trackSelector;
    DefaultBandwidthMeter defaultBandwidthMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionAudio = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        int permissionInternet = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        final ListView listView = (ListView) findViewById(R.id.list_view);

        ArrayList<Item> listArray = new ArrayList<Item>();
        listArray.add(new Item(R.drawable.dive, "dive","Ed Sheeran"));
        listArray.add(new Item(R.drawable.happier, "happier", "Ed Sheeran"));

        customAdapter adapter = new customAdapter(this, R.layout.item, listArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedposition = position;
            }
        });
        //istView.setSelection(1); //initial
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        ImageButton playBtn = (ImageButton) findViewById(R.id.playBtn);
        ImageButton pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);

        playBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectedSong=(Item)listView.getSelectedItem()
                Log.d("here positon", "" + selectedposition);


                 Item temp = (Item) (listView.getAdapter().getItem(selectedposition));
                 String tempSong= temp.getSong();

                if(selSong==null||!selSong.equals(tempSong)) {

                    selSong = temp.getSong();
                    Uri uri = null;

                    uri = Uri.parse("http://xxxxxx.cloudfront.net/Jerry/" + selSong + "/"+selSong+".m3u8");
                    Log.d("URI", "" + uri);
                    mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
                    player.prepare(mediaSource);
                }

                player.setPlayWhenReady(true);

                //   String selectedSong=temp.getSong();
            }
        });

        pauseBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selSong != null)
                    player.setPlayWhenReady(false);
            }
        });
  }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"종료합니다",Toast.LENGTH_SHORT);
        player.release();
        finish();

    }
    @Override
    public void onUserLeaveHint() {
        player.release();
        finish();
    }
}
