package com.example.consumopublicidad;

import java.util.Random;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;


public class PrincipalActivity extends Activity {
	
	private boolean selector = true;
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	private boolean iniciado = false;
	private Button boton;
	private TextView etiquetaTiempo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		
		TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		
		TabHost.TabSpec spec1 = tabs.newTabSpec("mitab1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("pest1");
		tabs.addTab(spec1);
		
		TabHost.TabSpec spec2 = tabs.newTabSpec("mitab2");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("pest2");
		tabs.addTab(spec2);
		
		TabHost.TabSpec spec3 = tabs.newTabSpec("mitab3");
		spec3.setContent(R.id.tab3);
		spec3.setIndicator("pest3");
		tabs.addTab(spec3);
		
		// Etiqueta con el tiempo en segundos
		this.etiquetaTiempo = (TextView)findViewById(R.id.textView4);
		
		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
			}
		});
		
		this.boton = (Button)findViewById(R.id.button1);
		this.boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					if(!iniciado){
						iniciado = true;
						boton.setText("Detener");
						
						// Se inicia el timer se gun el numero calculado
						customHandler.postDelayed(updateTimerThread, 0);
						
					}else{
						iniciado = false;
						boton.setText("Publicidad");
						
						// Se detiene el timer
						customHandler.removeCallbacks(updateTimerThread);
						
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
	}
	
	private Long numeroAleatorio(){
		Random r = new Random();
		// numero aleatorio entre 60 y 120
		int i1 = r.nextInt(120 - 60) + 60;
		Long numero = (Long)Long.valueOf(i1) * 1000;
		
		// Etiqueta del tiempo calculado
		etiquetaTiempo.setText(numero/1000 + " segundos");
		
		return numero;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
	
	@Override
    protected void onResume(){
		super.onResume();
	}
	
	private synchronized MMAdView cargaPublicidad(){
		int placementWidth = BANNER_AD_WIDTH;
		
		//Finds an ad that best fits a users device.
		if(canFit(IAB_LEADERBOARD_WIDTH)) {
		    placementWidth = IAB_LEADERBOARD_WIDTH;
		}else if(canFit(MED_BANNER_WIDTH)) {
		    placementWidth = MED_BANNER_WIDTH;
		}
		
		MMAdView adView = new MMAdView(this);
		if(selector){
			// publicidad de MPopular
			adView.setApid("154899");
			this.selector = false;
		}else{
			// publicidad de detective
			adView.setApid("148574");
			this.selector = true;
		}
		MMRequest request = new MMRequest();
		adView.setMMRequest(request);
		adView.setId(MMSDK.getDefaultAdId());
		adView.setWidth(placementWidth);
		adView.setHeight(BANNER_AD_HEIGHT);
		
		return adView;
	}
	
	protected boolean canFit(int adWidth) {
		int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, getResources().getDisplayMetrics());
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		return metrics.widthPixels >= adWidthPx;
	}
	
	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			try{
				MMAdView adView1 = cargaPublicidad();
				RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.publicidad1);
				layout1.removeAllViews();
				layout1.addView(adView1);
				adView1.getAd();
				
				MMAdView adView2 = cargaPublicidad();
				RelativeLayout layout2 = (RelativeLayout)findViewById(R.id.publicidad2);
				layout2.removeAllViews();
				layout2.addView(adView2);
				adView2.getAd();
				
				MMAdView adView3 = cargaPublicidad();
				RelativeLayout layout3 = (RelativeLayout)findViewById(R.id.publicidad3);
				layout3.removeAllViews();
				layout3.addView(adView3);
				adView3.getAd();
				
				MMAdView adView4 = cargaPublicidad();
				RelativeLayout layout4 = (RelativeLayout)findViewById(R.id.publicidad4);
				layout4.removeAllViews();
				layout4.addView(adView4);
				adView4.getAd();
				
				//customHandler.postDelayed(this, startTime);
				startTime = numeroAleatorio();
				
				// Se inicia el timer se gun el numero calculado
				customHandler.postDelayed(updateTimerThread, startTime);
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
        }
    };
}
