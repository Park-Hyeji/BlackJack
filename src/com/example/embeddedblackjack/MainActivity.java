package com.example.embeddedblackjack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCheckedChangeListener{
	
	int n_player = 1;
	int n_ai = 1;
	int n_deck = 1;
	RadioGroup playerCnt;
	RadioGroup aiCnt;
	RadioGroup levelCnt;
	Button gameStartBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	playerCnt = (RadioGroup)findViewById(R.id.playerCnt);
    	playerCnt.setOnCheckedChangeListener(this);
    	aiCnt = (RadioGroup)findViewById(R.id.aiCnt);
    	aiCnt.setOnCheckedChangeListener(this);
    	levelCnt = (RadioGroup)findViewById(R.id.levelCnt);
    	levelCnt.setOnCheckedChangeListener(this);
    	gameStartBtn = (Button)findViewById(R.id.gameStartBtn);
        
        //게임 시작 누르면 InGame으로 바뀜

        gameStartBtn.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 if(n_player + n_ai > 6)
		               Toast.makeText(getApplicationContext(), "참가자는 6인을 초과할 수 없습니다", Toast.LENGTH_LONG).show();
				 else{
					//화면 전환
					Intent intent = new Intent(MainActivity.this,InGameActivity.class);
					
					intent.putExtra("playerCnt", n_player); //플레이어 수 전달			
					intent.putExtra("aiCnt", n_ai); //ai수 전달
					intent.putExtra("levelCnt", n_deck); //레벨 값 전달
					startActivity(intent);
					finish();
				}
			}
        });
    }
    
    public void onCheckedChanged(RadioGroup arg0, int arg1){
    	if(playerCnt.getCheckedRadioButtonId() == R.id.onePlayer)
    		n_player = 1;
    	if(playerCnt.getCheckedRadioButtonId() == R.id.twoPlayer)
    		n_player = 2;
    	if(playerCnt.getCheckedRadioButtonId() == R.id.threePlayer)   
    		n_player = 3;       
       
    	if(aiCnt.getCheckedRadioButtonId() == R.id.oneAI)
    		n_ai = 1;
    	if(aiCnt.getCheckedRadioButtonId() == R.id.twoAI)
    		n_ai = 2;
    	if(aiCnt.getCheckedRadioButtonId() == R.id.threeAI)   
    		n_ai = 3;    
    	if(aiCnt.getCheckedRadioButtonId() == R.id.fourAI)   
    		n_ai = 4;  
       
    	if(levelCnt.getCheckedRadioButtonId() == R.id.oneDeck)
    		n_deck = 1;
    	if(levelCnt.getCheckedRadioButtonId() == R.id.twoDeck)   
    		n_deck = 2;    
    	if(levelCnt.getCheckedRadioButtonId() == R.id.fourDeck)   
    		n_deck = 4;  
    	if(levelCnt.getCheckedRadioButtonId() == R.id.sixDeck)   
    		n_deck = 6;  
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
