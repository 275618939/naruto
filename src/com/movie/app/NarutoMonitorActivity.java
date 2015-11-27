package com.movie.app;



import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.movie.app.OnNarutoMessageListener;

public  abstract  class NarutoMonitorActivity extends FragmentActivity implements OnNarutoMessageListener{
	
	private ProgressDialog progressDialog;  
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public void finish() {
		super.finish();
		
	}
 
	@Override
	public void onRestart() {
		super.onRestart();
	}
	
	
	public void showProgressDialog(String title,String message)
	{
		if(progressDialog==null)
		{
			
			 progressDialog = ProgressDialog.show(this, title, message, true, true);
		}else if(progressDialog.isShowing())
		{
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}
	
		progressDialog.show();
		
	}
	
	public void hideProgressDialog()
	{
	
		if(progressDialog!=null&&progressDialog.isShowing())
		{
			progressDialog.dismiss();
		}
		
	}
	
	public void showToask(String hint){
		   Toast toast=Toast.makeText(this,hint,Toast.LENGTH_SHORT);
		   toast.show();
	}
	/*
	 * 返回
	 */
	public void doBack(View view) {
		onBackPressed();
	}
	protected MediaPlayer ring() throws Exception, IOException {
		// TODO Auto-generated method stub
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		MediaPlayer player = new MediaPlayer();
		player.setDataSource(this, alert);
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
			//player.setLooping(true);
			player.prepare();
			player.start();
		}
		return player;
	}
	@Override 
	public void onConnectionClosed(){};
	@Override
	public void onConnectionSucceed() {}
	@Override
	public void onConnectionStatus(boolean  isConnected){}
	@Override
	public void onNetworkChanged(NetworkInfo info){};
}
