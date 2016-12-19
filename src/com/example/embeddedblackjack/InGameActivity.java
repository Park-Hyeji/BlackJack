package com.example.embeddedblackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.example.embeddedblackjack.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InGameActivity extends Activity{
	/////서버-클라이언트 통신 관련 변수들/////
	int p_joined = 1;
	static final int Port = 5001; //5001번 포트 사용
	ServerSocket serversocket = null; //서버 소켓
	Socket socket = null; //소켓
	Send_game_info info = new Send_game_info();
	boolean AllConnected = true;
	////////////////////////////////
	/////UI 관련 변수들////////////////
	TextView total_money, bet_money, cardAndItem;
	Button chip_1, chip_5, chip_20 ,chip_100, chip_500;
	Button gameStartBtn, hitBtn, stayBtn, doubleBtn, splitBtn;
	/////플레이에 관련 변수들/////////////
	int playerCnt = 0; //플레이어 숫자
	int aiCnt = 0; //Ai 숫자
	int levelCnt = 0; //level
	int nth_shuffle = 0;
	int total_player = 0; //전체 참가자 수
	int pretotal; //게임 이전 Total
	int n_c_ready = 0; //준비된 클라이언트 수
	int num_game = 0; //게임 횟수
	
	ArrayList<Ai> Ai_list = new ArrayList<Ai>(); //Ai 배열
	ArrayList<Player> c_list = new ArrayList<Player>(); //클라이언트 배열
	
	ArrayList<Player> dead_p_list = new ArrayList<Player>(); //재산이 0인 플레이어들
	ArrayList<Ai> dead_Ai_list = new ArrayList<Ai>(); //재산이 0인 Ai들
	
	Player Me,p1,p2,p3; //플레이어 
	Ai ai1, ai2, ai3, ai4; //Ai
	final Dealer dealer = new Dealer(); //딜러 생성
	Deck deck; //덱 
	ArrayList<ListView> spot_list = new ArrayList<ListView>(); //게임 자리
    
	AiPlay ap = new AiPlay(); //Ai 플레이 Thread
    DealerPlay dp = new DealerPlay(); //딜러 플레이 Thread
    ClientReady cr = new ClientReady(); //클라이언트 준비 Thread
    
	boolean Game_on = true; //게임중
	boolean ai_turn = false; //ai 차례
	boolean d_turn = false; //Dealer 차례
	
	Item item; //Item
	String itemcard;
	String itemeffect;
	//////////////////////////////////////////
	
	///////내장 기기 관련 변수들////////////////////
	Vibrator mVibrator;
	
	segthread SegThread = new segthread(); //세그먼트 Thread
	public native int SegmentControl(int value);
	public native int SegmentIOControl(int value);
	
	//TextLCD
	public native int TextLCDOut(String str, String str2); 
	public native int IOCtlClear();
	String Text1="      RSP    ";
	String Text2="game start";
	
	//Piezo
	PiezoThread piezo = new PiezoThread();
	int PiezoData;
	int music = 0;
	int index = 0;
	int musicOne = 0;
	
	//슈퍼마리오 GameOver = 게임 짐
	int music1[] = {5,18,0,18,18,17,7,5,3,0,3,1,0,0,17,0,0,5,0,3,6,7,6,52,53,52,5,5};
	int duration1[] = {8,8,8,8,8,8,8,8,8,8,8,8,4,8,8,8,8,8,4,4,8,8,8,4,4,4,4,2};
	
	//슈퍼마리오 Level Complete = 게임 이김
	int music2[] = {1,3,5,17,19,21,19,1,50,52,17,66,68,19,2,4,53,18,69,69,69,69,33};
	int duration2[] = {8,8,8,8,8,4,4,8,8,8,8,8,4,4,8,8,8,8,8,4,8,8,8,2};

	//슈퍼마리오 Coin Sound = 아이템 사용
	int music3[] = {7,19,7,19,0,7,19,7,19};
	int duration3[] = {8,8,8,8,4,8,8,8,8};
		
	//슈퍼라미오 1UP = 아이템 얻음
	int music4[] = {19,21,35,33,34,36};
	int duration4[] = {8,8,8,8,8,8};
	
	//말할 수 없는 비밀 OST 배틀2 =  내 턴 or 게임 초기 화면 (파#,도#솔#,레#)
	int music5[] = {5,52,65,5,52,65,5,52,19,5,52,65,5,52,19,65,
	6,65,67,6,65,19,6,65,66,6,65,19,6,65,66,65,
	17,65,22,17,65,68,17,65,67,17,65,19,17,65,66,19,
	65,0,81,0,0,0};
	int duration5[] = {16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,
	16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,
	16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,
	4,4,4,4,4,4};
	
	//슈퍼마리오 Castle Complete = 블랙잭으로 이김
	int music6[] = {17,5,3,17,5,3,17,65,52,4,65,52,4,65,66,53,5,66,53,5,66,20,20,20,21,0};
	int duration6[] = {8,8,8,8,8,8,2,8,8,8,8,8,8,2,8,8,8,8,8,8,4,8,8,8,2,2};
	
	public native int PiezoControl(int value);	// JNI Interface

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);
        System.loadLibrary("blackjack");
    	//설정값 받아오기
        Intent intent = getIntent();
        playerCnt = intent.getIntExtra("playerCnt",0); //필요 참가자 수
        aiCnt = intent.getIntExtra("aiCnt",0); //Ai 숫자
        levelCnt = intent.getIntExtra("levelCnt",0); //덱 수
        
    	//TextView(텍스트 뷰)
    	final TextView deckNum = (TextView)findViewById(R.id.deckNum); //Deck의 갯수 표시 Text View
        total_money = (TextView)findViewById(R.id.cashNum); //전체 보유 금액 Text View
        bet_money = (TextView)findViewById(R.id.betNum); //배팅금액 Text View
        cardAndItem = (TextView)findViewById(R.id.cardAndItem); //카드랑 아이템 보여주는 Text View
        
        final TextView Split_Card = (TextView)findViewById(R.id.splitCardSaveArea); //Split되어 플레이 대기 중인 카드        
        final TextView Score_board1 = (TextView)findViewById(R.id.splitView1); //플레이어1 점수 판
        final TextView Score_board2 = (TextView)findViewById(R.id.splitView2); //플레이어2 점수 판
        final TextView Score_board3 = (TextView)findViewById(R.id.splitView3); //플레이어3 점수 판
        final TextView Score_board4 = (TextView)findViewById(R.id.splitView4); //플레이어4 점수 판
        final TextView Score_board5 = (TextView)findViewById(R.id.splitView5); //플레이어5 점수 판
        final TextView Score_board6 = (TextView)findViewById(R.id.splitView6); //플레이어6 점수 판
        final TextView Score_board_dealer = (TextView)findViewById(R.id.splitViewdealer); //딜러 점수 판
        
        //플레이 Button(버튼)
    	hitBtn = (Button)findViewById(R.id.hit);
    	stayBtn = (Button)findViewById(R.id.Stay);
    	splitBtn = (Button)findViewById(R.id.Split);
    	doubleBtn = (Button)findViewById(R.id.Double);
    	
    	//배팅 Button(버튼)
    	chip_1 = (Button)findViewById(R.id.chip_1);
    	chip_5 = (Button)findViewById(R.id.chip_5);
    	chip_20 = (Button)findViewById(R.id.chip_20);
    	chip_100 = (Button)findViewById(R.id.chip_100);
    	chip_500 = (Button)findViewById(R.id.chip_500);
    	
    	//시작 Button(버튼)
    	gameStartBtn = (Button)findViewById(R.id.gameStartBtn);     
                            
    	//버튼 비활성화
    	hitBtn.setEnabled(false);
    	stayBtn.setEnabled(false);
    	splitBtn.setEnabled(false);
    	doubleBtn.setEnabled(false);
    	
        total_player = playerCnt + aiCnt;
        //ListView자리 배분
        ListView spot1 = (ListView)findViewById(R.id.player1); spot_list.add(spot1);
        ListView spot2 = (ListView)findViewById(R.id.player2); spot_list.add(spot2);
        ListView spot3 = (ListView)findViewById(R.id.player3); spot_list.add(spot3);
        ListView spot4 = (ListView)findViewById(R.id.player4); spot_list.add(spot4);
        ListView spot5 = (ListView)findViewById(R.id.player5); spot_list.add(spot5);
        ListView spot6 = (ListView)findViewById(R.id.player6); spot_list.add(spot6);
        
        ListView spot7 = (ListView)findViewById(R.id.dealer); //딜러 spot

        if(total_player < 6){spot6.setVisibility(View.GONE); Score_board6.setVisibility(View.GONE);}
        if(total_player < 5){spot5.setVisibility(View.GONE);Score_board5.setVisibility(View.GONE);}
        if(total_player < 4){spot4.setVisibility(View.GONE);Score_board4.setVisibility(View.GONE);}
        if(total_player < 3){spot3.setVisibility(View.GONE); Score_board3.setVisibility(View.GONE);}
        
        //서버 플레이어 생성
        p1 = new Player();//플레이어1(서버 플레이어) 생성
    	
        //1인 이상 플레이 할시 필요한 게임 서버 생성 
        if(playerCnt > 1)
        {
        	Create_Server();
        	cr.setDaemon(true);
        	cr.start();
        	gameStartBtn.setEnabled(false);
        }
        info.start();
        //Ai 숫자 선택에 따른 Ai생성
        Create_Ai();
        
        if(levelCnt == 1){deckNum.setText("1 DECK"); deck = new Deck(levelCnt,4);}
        else if(levelCnt == 2){deckNum.setText("2 DECK");deck = new Deck(levelCnt,2);}
        else if(levelCnt == 4){deckNum.setText("4 DECK");deck = new Deck(levelCnt,1);}
        else if(levelCnt == 6){deckNum.setText("6 DECK");deck = new Deck(levelCnt,1);}
              
    	deck.Shuffle();	
    	deck.nth_shuffle++;
    	deck.Card_Shuffled[2] = "♠A";
    	deck.Card_Shuffled[5] = "♠10";
    	
    	Me = p1;//본인
    	pretotal = Me.total;
     	Me.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, Me.Card);
        spot1.setAdapter(Me.adapter);
     	total_money.setText(Integer.toString(Me.total)); //플레이어가 소지한 총 금액
     	bet_money.setText(Integer.toString(Me.bet)); //플레이어가 배팅한 금액
       
        //딜러 생성
        dealer.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, dealer.Card);
        ListView dealer_spot = (ListView)findViewById(R.id.dealer);
        dealer_spot.setAdapter(dealer.adapter);
        //내장 기기 부분
        //Vibrator (진동)
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); //진동
        
        //Segment (세그먼트)
        SegThread.setDaemon(true);
       // SegThread.start();
        
        //Piezo
    	piezo.setDaemon(true);
		PiezoData = 0;
		PiezoControl(PiezoData);
		//piezo.start();
		
		ap.setDaemon(true);
        dp.setDaemon(true);                
        ap.start();
        dp.start();

     	//배팅 버튼  부분
     	chip_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+1 <= Me.total){
					Me.bet = Me.bet +1;
					bet_money.setText(Integer.toString(Me.bet));
					SendtoAll("hello");
				}
				else Toast.makeText(getApplicationContext(), "보유하신 금액을 초과하였습니다.", Toast.LENGTH_LONG).show();}
		});
     	chip_5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+5 <= Me.total){
					Me.bet = Me.bet + 5;
					bet_money.setText(Integer.toString(Me.bet));}
				else
					Toast.makeText(getApplicationContext(), "보유하신 금액을 초과하였습니다.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_20.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+20 <= Me.total){
					Me.bet = Me.bet + 20;
					bet_money.setText(Integer.toString(Me.bet));}
				else
					Toast.makeText(getApplicationContext(), "보유하신 금액을 초과하였습니다.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_100.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+100 <= Me.total){
					Me.bet = Me.bet + 100; 
					bet_money.setText(Integer.toString(Me.bet));
					}
				else
					Toast.makeText(getApplicationContext(), "보유하신 금액을 초과하였습니다.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_500.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+500 <= Me.total){
					Me.bet = Me.bet + 500;
					bet_money.setText(Integer.toString(Me.bet));
				}
				else
					Toast.makeText(getApplicationContext(), "보유하신 금액을 초과하였습니다.", Toast.LENGTH_LONG).show();
			}
		});
     	
     	//게임조작 버튼 부분
        gameStartBtn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				//말할 수 없는 비밀
				//music = 5;
				//index = 0;
				//musicOne = 0;
				if(Me.bet != 0)
				{
					chip_1.setEnabled(false);
					chip_5.setEnabled(false);
					chip_20.setEnabled(false);
					chip_100.setEnabled(false);
					chip_500.setEnabled(false);
					gameStartBtn.setEnabled(false);
					
			    	hitBtn.setEnabled(true);
			    	stayBtn.setEnabled(true);
			    	splitBtn.setEnabled(true);
			    	doubleBtn.setEnabled(true);
			    	init();	
			    	
			    	//이번 게임에서 이 카드 얻으면 이 아이템 얻음 표시
			    }
				else{Toast.makeText(getApplicationContext(), "배팅을 해야 게임이 가능합니다.", Toast.LENGTH_LONG).show();}}
		});
 		hitBtn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Me.Hit(deck.distribute_card());				
				Me.adapter.notifyDataSetChanged();		
				GetItem(Me,itemcard,item);
				if(Me.score == 21) Me.BlackJack = true;
				if(Me.bust){
    					long[] pattern = {500, 200, 500, 200};
    					mVibrator.vibrate(pattern, -1);
						ai_turn = true;
						music = 1;
						musicOne = 0;
						index = 0;
					}
				TextLcd();
				}
	
		});
		stayBtn.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View arg0) 
        	{
        		if(!Me.Splited.isEmpty())
        		{
        			Me.Continue();
        			Me.adapter.notifyDataSetChanged();
        			
        			String score_temp = (String)Score_board1.getText();
        			score_temp = score_temp.replaceAll("ScoreBoard","");
        			String score_temp2 = Me.score_board.get(Me.score_board.size()-1).toString();
        			Score_board1.setText(score_temp + " " + score_temp2);
        			
        			String temp = (String) Split_Card.getText();
        			temp = temp.replaceAll((String)Me.Card.get(0), "");
        			temp = temp.replaceAll(" ", "");
        			Split_Card.setText(temp);
        		}
        		else
        		{
        			Me.Stay();
        			ai_turn = true;
        		}}});
        splitBtn.setOnClickListener(new Button.OnClickListener()
        { 
        	public void onClick(View arg0) {
        	// TODO Auto-generated method stub
        		if(Me.Card.size() == 2)
        		{Me.Split();
        		Me.adapter.notifyDataSetChanged();
        		
        		String temp = (String) Split_Card.getText();
        		String temp2 = (String) Me.Splited.get(Me.Splited.size()-1);
        		Split_Card.setText(temp + ",  " + temp2);}
			}
        });
        doubleBtn.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Me.Double_down[Me.score_board.size()] = true;
				Me.Hit(deck.distribute_card());
				Me.adapter.notifyDataSetChanged();
				
				if(!Me.Splited.isEmpty())
        		{
        			Me.Continue();
        			Me.adapter.notifyDataSetChanged();
        			
        			String score_temp = (String)Score_board1.getText();
        			score_temp = score_temp.replaceAll("ScoreBoard","");
        			String score_temp2 = Me.score_board.get(Me.score_board.size()-1).toString();
        			Score_board1.setText(score_temp + " " + score_temp2);
        			
        			String temp = (String) Split_Card.getText();
        			temp = temp.replaceAll((String)Me.Card.get(0), "");
        			temp = temp.replaceAll(" ", "");
        			Split_Card.setText(temp);
        		}
        		else
        		{
        			Me.Stay();
        			String score_temp = (String)Score_board1.getText();
        			score_temp = score_temp.replaceAll("ScoreBoard","");
        			String score_temp2 = Me.score_board.get(Me.score_board.size()-1).toString();
        			Score_board1.setText(score_temp + " " + score_temp2);
        			total_money.setText(Integer.toString(Me.total));
        			bet_money.setText(Integer.toString(Me.bet));
        	 	
        			chip_1.setEnabled(true);
        			chip_5.setEnabled(true);
        			chip_20.setEnabled(true);
        			chip_100.setEnabled(true);
        			chip_500.setEnabled(true);
        			gameStartBtn.setEnabled(true);
				
        			hitBtn.setEnabled(false);
        			stayBtn.setEnabled(false);
        			splitBtn.setEnabled(false);
        			doubleBtn.setEnabled(false);
        		}
			}	
        });
    }
    
    public void onDestory(){
    	try {
    		socket.close();
			serversocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
     
    void init(){
			Toast.makeText(getApplicationContext(), "게임이 시작됩니다.", Toast.LENGTH_LONG).show();
			num_game++;//게임 횟수 증가
			//모든 플레이어 및 Ai 준비
			Me.Ready(); //본인
			dealer.Ready(); //딜러
			if(!c_list.isEmpty())
			{for(int i=0; i< c_list.size(); i++) //클라이언트 존재시 클라이언트
			 c_list.get(i).Ready();}
			for(int i=0; i<Ai_list.size(); i++) //Ai
			{Ai_list.get(i).Ready();
			 Ai_list.get(i).bet();}
			
			item = new Item();
			item.make_item();
	    	itemcard = item.nowCard();
	    	itemeffect = item.effect;
	    	cardAndItem.setText("'"+itemcard+"' 카드 획득시 \n '"+itemeffect+"' 효과 얻음");		
			
			//첫 카드 받기
			Me.Hit(deck.distribute_card());
			Me.adapter.notifyDataSetChanged();
			if(!c_list.isEmpty())
			{for(int i=0; i< c_list.size(); i++)
				c_list.get(i).Hit(deck.distribute_card());}
			for(int i=0; i< Ai_list.size(); i++)
			{Ai_list.get(i).Hit(deck.distribute_card());
			 Ai_list.get(i).adapter.notifyDataSetChanged();}
	    	
	    	dealer.recieve(deck.distribute_card());
	    	dealer.adapter.notifyDataSetChanged();
	    	///////////////////////////////////////////////
	    	
	    	//두 번째 카드 받기
	    	Me.Hit(deck.distribute_card());
	    	Me.adapter.notifyDataSetChanged();
			
	    	if(!c_list.isEmpty())
			{for(int i=0; i< c_list.size(); i++)
				c_list.get(i).Hit(deck.distribute_card());}
	    	
			for(int i=0; i< Ai_list.size(); i++)			
			{Ai_list.get(i).Hit(deck.distribute_card());
			 Ai_list.get(i).adapter.notifyDataSetChanged();}
	    	
			dealer.Unknown = deck.distribute_card();
	    	dealer.Card.add(1,"unknown");
	    	dealer.adapter.notifyDataSetChanged();
	    	////////////////////////////////////////////////
	    	GetItem(Me,itemcard,item);
	    	//BlackJack 확인/////////////////////////////////
	    	if(Me.score == 21) {Me.BlackJack = true; music = 6; musicOne = 0; index = 0;}
	    	if(!c_list.isEmpty())
			{for(int i=0; i< c_list.size(); i++)
	    		{if(c_list.get(i).score == 21)
	    			c_list.get(i).BlackJack = true;}}
			for(int i=0; i< Ai_list.size(); i++)			
			{
				if(Ai_list.get(i).score == 21)
					Ai_list.get(i).BlackJack = true;}
			////////////////////////////////////////////////
			
			///////////////Dealer Black Jack, Insurance, EvenMoney 확인////////////
	    	char dfc = ((String) dealer.Card.get(0)).charAt(1);
	    	if(dfc == 'A' || dfc == '1'|| dfc == 'J'||dfc == 'Q'||dfc == 'K')
	    	{
	    		//Insuracne, EvenMoney
	    		dealer.Card.remove(1);
	         	dealer.recieve(dealer.Unknown);
	         	dealer.adapter.notifyDataSetChanged();
	         	
	         	if(dealer.score == 21)
	         	{
	         		dealer.BlackJack = true;
	         		music = 1;
	         		musicOne = 0;
	         		index = 0;
	         		Toast.makeText(getApplicationContext(), Integer.toString(dealer.score), Toast.LENGTH_LONG);
	         	}
	         }
	    	///////////////////////////////////////////////
	    	
	    	//////////////////비교/////////////////////////
	    	if(dealer.BlackJack)
	    	{
	    		if(!Me.BlackJack){Me.total -= Me.bet;}
	    		Me.bet = 0;
    			total_money.setText(Integer.toString(Me.total));
    			bet_money.setText(Integer.toString(Me.bet));
    			
		    	if(!c_list.isEmpty())
				{
		    		for(int i=0; i< c_list.size(); i++)
		    		{
		    			if(!c_list.get(i).BlackJack)
		    				c_list.get(i).total -= c_list.get(i).bet;
		    			c_list.get(i).bet = 0;
		    		}
		    	}
		    	
				for(int i=0; i< Ai_list.size(); i++)			
				{
					if(!Ai_list.get(i).BlackJack)
						Ai_list.get(i).total -= Ai_list.get(i).bet;
				}
				
				chip_1.setEnabled(true);
				chip_5.setEnabled(true);
				chip_20.setEnabled(true);
				chip_100.setEnabled(true);
				chip_500.setEnabled(true);
				gameStartBtn.setEnabled(true);
				
		    	hitBtn.setEnabled(false);
		    	stayBtn.setEnabled(false);
		    	splitBtn.setEnabled(false);
		    	doubleBtn.setEnabled(false);
	    	}
	    	/////////////////////////////////////////////////////
	    	TextLcd();
	}
    void Set_View(TextView v)
    {v.setText("ScoreBoard");}
    void Create_Server(){
    	new Thread(new Runnable(){
    		public void run() {
    			try {
    				serversocket = new ServerSocket(Port); //서버 소켓 생성
					while(true)
					{
						socket = serversocket.accept();//서버에 접속하는 클라이언트 소켓 얻어오기
					 	ServerReceiver sr_thread = new ServerReceiver(socket);
					 	sr_thread.run();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}      			
    			// TODO Auto-generated method stub
    		}}).start();
    }
    void Create_Ai()
    {
    	int spot_n = playerCnt;
        if(aiCnt >= 1){
        	ai1 = new Ai(2000, 0.9, 0.6);
        	ai1.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai1.Card);
            spot_list.get(spot_n++).setAdapter(ai1.adapter);
        	Ai_list.add(ai1);	
        }
        if(aiCnt >= 2){
        	ai2 = new Ai(1500, 0.8, 0.4);
            ai2.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai2.Card);
            spot_list.get(spot_n++).setAdapter(ai2.adapter);
        	Ai_list.add(ai2);}
        if(aiCnt >= 3){
        	ai3 = new Ai(1000, 0.75, 0.25); 
            ai3.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai3.Card);
            spot_list.get(spot_n++).setAdapter(ai3.adapter);
        	Ai_list.add(ai3);}
        if(aiCnt >= 4){
        	ai4 = new Ai(500, 0.65, 0.15); 
            ai4.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai4.Card);
            spot_list.get(spot_n++).setAdapter(ai4.adapter);
        	Ai_list.add(ai4);}
    }
    void SendtoAll(String msg)
    {
    	for(int i=0; i < c_list.size(); i++)
    	{
    		try 
    		{DataOutputStream out = c_list.get(i).OS;
			 out.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    void TextLcd(){
		String num_card = Integer.toString(deck.nth_card);
        String remain_card = Integer.toString(deck.num_of_deck * 52 - deck.nth_card);
        String num_deck = Integer.toString(deck.num_of_deck);   
        String nth_game = Integer.toString(num_game);
        Text2 = (num_card + " / " + remain_card + " / " + num_deck + " / " + nth_game);
        TextLCDOut(Text1, Text2);	
	}
    void GetItem(Player p, String Card, Item it)
    {
    	for(int i=0; i<p.Card.size();i++)
    	{
    		if(p.Card.get(i) == Card)
    		{
    			if(it.item_num == 0) //딜러 카드 훔쳐보기
    			{
    				String dealerTwo = (String) dealer.Unknown; 
    				Toast.makeText(getApplicationContext(), dealerTwo, Toast.LENGTH_LONG);   
    			}
    			else if(it.item_num == 1) //마지막 카드 덱에서 바꾸기
    			{
    				String last_card = (String) p.Card.get(p.Card.size()-1);
    				char var = last_card.charAt(1);
    				if(var == 'J' || var == 'Q' || var =='K'||var == '1'){p.score -= 10;}
    				else if(var =='A'){p.score -= 11;}
    				else if(var == 'a'){p.score--;}
    				else {p.score -= (var - 48);}
    				p.nth_card--;
    				p.Card.remove(p.Card.size()-1);
    				p.Hit(deck.distribute_card());
    				Me.adapter.notifyDataSetChanged();
    			}
    			else if(it.item_num == 2) //이겼을 때 두배 보상
    			{
    				p.DoubleWin = true;
    			}
    			else if(it.item_num == 3) //이겼을 때 세배 보상
    			{
    				p.TripleWin = true;
    			}
    			else if(it.item_num == 4)//BUST됐을 때, 마지막 카드 버리기
    			{
    				p.Bust_change = true;
    			}
    			else if(it.item_num ==5)//패배 시에 배팅 금액을 돌려받기
    			{
    				p.lose_protect = true;
    			}
    		}
    	}
    }

class ServerReceiver extends Thread{
    	//서버의 In/Out 스트림
    	Socket socket = null;
    	boolean Connected = true; 
    	DataInputStream IS = null; //InPut 스트림
    	Player p;
    	ServerReceiver(Socket socket)
    	{
    		p = new Player();
    		this.socket = socket;
    		try {
    			IS = new DataInputStream(socket.getInputStream());
    			p.OS = new DataOutputStream(socket.getOutputStream());
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();}
    	}
    	public void run()
    	{
    		try {
    			if(c_list.size() == 0)
    				p.name = "P2";
    			else
    				p.name = "P3";
    		c_list.add(p);
    		SendtoAll(p.name);
    		while(IS != null)
    	    	{
    				String Check = IS.readUTF();
    				if(Check.equals("Ready"))
    					n_c_ready++;
    			}
    		} 
    		catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
    	}
}
class Send_game_info extends Thread{
	public void run()
	{
		while(Game_on)
		{
			if(playerCnt-1 == c_list.size())
			{
				SendtoAll("playerCnt" + Integer.toString(playerCnt));
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SendtoAll("aiCnt" + Integer.toString(aiCnt));
				break;
			}
		}
	}
}
class ClientReady extends Thread{
	public void run()
	{
		while(Game_on)
		{
			if(n_c_ready == playerCnt - 1)
			{
		    	new Thread(new Runnable() {    
		       	    public void run() {
			  		    runOnUiThread(new Runnable() {    	  		      
			  		       public void run() {
			  		        // TODO Auto-generated method stub
			  		    	 gameStartBtn.setEnabled(true);
			  		       }
			  		      });
		       	   }//run method
		    }).start();
			}
		}
	}
}
class GStart extends Thread{}
class AiPlay extends Thread{
    void Ai_turn(final Ai ai) 
    {
		try {sleep(1000);} catch(InterruptedException e) 
		{// TODO Auto-generated catch block
			e.printStackTrace();}
    	while(!ai.bust && !ai.stay){   		
    		if(ai.score <= 11)
    		{Hit_Card(ai);}
    		else if(ai.score > 11 && ai.score < 16)
    		{
    			double temp;
    			temp = Math.random();
    			if(temp <= ai.prob_hit_12)
    			{Hit_Card(ai);}
    			else
    				ai.stay = true;
    		}
    		else if(ai.score >= 16 && ai.score <= 19)
    		{
    			double temp;
    			temp = Math.random();
    			if(temp <= ai.prob_hit_16)
    			{Hit_Card(ai);}
    			else
    				ai.stay = true;
    		}
    		else 
    			ai.stay = true;
    		try {sleep(1000);} catch(InterruptedException e) 
    		{// TODO Auto-generated catch block
    			e.printStackTrace();}
    	}
    }
    void Hit_Card(final Ai ai)
    {
    	ai.Hit(deck.distribute_card());
    	TextLcd();
    	new Thread(new Runnable() {    
	       	    public void run() {
   	  		    runOnUiThread(new Runnable() {    	  		      
    	  		       public void run() {
    	  		        // TODO Auto-generated method stub
    	  		    	 ai.adapter.notifyDataSetChanged();
    	  		       }
    	  		      });
	       	   }//run method
	    }).start();
    }
    public void run()
    {
    	while(true)
    	{
    		if(ai_turn)
    		{
    			for(int i=0; i<Ai_list.size(); i++)
    				{Ai_turn(Ai_list.get(i));}
        		ai_turn = false;
        		d_turn = true;
    		}
    	}
    }
}
class DealerPlay extends Thread{
	void UI_change(){
    	new Thread(new Runnable() {    
       	    public void run() {
	  		    runOnUiThread(new Runnable() {    	  		      
	  		       public void run() {
	  		        // TODO Auto-generated method stub
	  		    	 dealer.adapter.notifyDataSetChanged();
	  		       }
	  		      });
       	   }//run method
    }).start();
	}
	void Set_Ready(){
		  n_c_ready = 0;
		  new Thread(new Runnable() {    
			 public void run(){
				 runOnUiThread(new Runnable() {    	  		      
				 public void run() {
		     // TODO Auto-generated method stub
		     total_money.setText(Integer.toString(Me.total));
		     bet_money.setText(Integer.toString(Me.bet));
      		 chip_1.setEnabled(true);
      		 chip_5.setEnabled(true);
      		 chip_20.setEnabled(true);
      		 chip_100.setEnabled(true);
      		 chip_500.setEnabled(true);
      		 if(playerCnt == 1)
      			 gameStartBtn.setEnabled(true);
      		 
      		 hitBtn.setEnabled(false);
      		 stayBtn.setEnabled(false);
      		 splitBtn.setEnabled(false);
      		 doubleBtn.setEnabled(false);
		       }
		      });
 	   }//run method
		}).start();
	}
	void Dealer_turn()
    {
		if(dealer.Card.get(1) == "unknown")
		{
			dealer.Card.remove(1);
			dealer.recieve(dealer.Unknown);
			UI_change();
		}
		
    	
    	while(dealer.score <= 16 && !dealer.bust)
    	{
    		dealer.recieve(deck.distribute_card());
        	TextLcd();
        	UI_change();
    		//SendtoAll((String) dealer.Card.get(dealer.nth_card-1));
    		try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(!Me.bust)
    	{
    		for(int i=0; i< Me.score_board.size(); i++)
    		{
    			int score = (Integer) Me.score_board.get(i);  				
    			if(score > dealer.score)
    			{
    				if(Me.DoubleWin == true)
    				{Me.bet *= 2; Me.DoubleWin = false;}
    				if(Me.TripleWin == true)
    				{Me.bet *= 3; Me.TripleWin = false;}
    				if(Me.BlackJack == true)
    				{Me.bet *= 1.5; Me.BlackJack = true;}
    				if(Me.Double_down[i]) 
    					{Me.total = Me.total + (2*Me.bet);}
    				else {Me.total = Me.total + Me.bet;}
    				music = 2;
    				musicOne = 0;
    				index = 0;
    			}
    			if(score < dealer.score)
    			{
    				if(Me.lose_protect == true)
    					continue;
    				if(Me.Double_down[i]){
    					Me.total = Me.total - (2*Me.bet);}
    				else{Me.total = Me.total - Me.bet;}
					music = 1;
					musicOne = 0;
					index = 0;
    				long[] pattern = {500, 200, 500, 200};
                	mVibrator.vibrate(pattern, -1);
    			}
    		}
    	}
    	for(int i=0; i<Ai_list.size(); i++)
    	{
    		if(Ai_list.get(i).score > dealer.score)
    			Ai_list.get(i).total = Ai_list.get(i).total + Ai_list.get(i).bet;
    		if(Ai_list.get(i).score < dealer.score)
    			Ai_list.get(i).total = Ai_list.get(i).total - Ai_list.get(i).bet;
    	}
    	Me.bet = 0;
    	n_c_ready = 0;
    	Set_Ready();
    }
	public void run(){
		while(true)
		{
			if(d_turn)
			{
				Dealer_turn();
				d_turn = false;
			}
		}
	}
}
class segthread extends Thread{
		   public void run(){      
		         while(Game_on) 
		         {
		            SegmentControl(Me.total);
		            int tmp;
		            if(pretotal > Me.total){
		               tmp = Me.total;
		               while(pretotal != tmp) {
		                  SegmentControl(pretotal);
		                  pretotal -= 2;
		               }
		            }else if(pretotal < Me.total){
		               tmp = Me.total;
		               while(pretotal != tmp) {
		                  SegmentControl(pretotal);
		                  pretotal += 2;
		               }
		              }
		            }
		          }
		   }
class PiezoThread extends Thread{
		public void run(){
			while(Game_on){
				//게임이 실행중인 동안
				if(music == 1 && musicOne == 0){
					int noteDuration = 1000/duration1[index];
					PiezoControl(music1[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music1.length){
						musicOne = 1;
					}
				}else if(music == 2 && musicOne == 0){
					int noteDuration = 1000/duration2[index];
					PiezoControl(music2[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music2.length){
						musicOne = 1;
					}
				}else if(music == 3 && musicOne == 0){
					int noteDuration = 1000/duration3[index];
					PiezoControl(music3[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music3.length){
						musicOne = 1;
					}
				}else if(music == 4 && musicOne == 0){
					int noteDuration = 1000/duration4[index];
					PiezoControl(music4[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music4.length){
						musicOne = 1;
					}
				}else if(music == 5 && musicOne == 0){
					int noteDuration = 1000/duration5[index];
					PiezoControl(music5[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music5.length){
						musicOne = 1;
					}
				}else if(music == 6 && musicOne == 0){
					int noteDuration = 1000/duration6[index];
					PiezoControl(music6[index]);
					try{
						sleep(noteDuration);
					}catch(InterruptedException e){
					}
					PiezoControl(0);
					try{
						sleep(100);
					}catch(InterruptedException e){
					}
					index++;
					if(index == music6.length){
						musicOne = 1;
					}
				}
			}
		}
	}
class Item {

	int item_num = 0;
	String Card;
	String effect;
	
	Item()
	{int itemRan = (int) (Math.random() * 6);
	 this.item_num = itemRan;}
	
	String nowCard(){
	   //이번 경기에서 어떤 카드를 받으면 어떤 아이템을 얻는지 보여줌
	   //한 게임당 아이템 1개
	   int cardRan = (int) (Math.random() * 52);
	   String cardName = deck.One_Card_Set[cardRan];
	   return cardName;}
	
   void make_item(){
	   if(item_num == 0){effect = "딜러 카드 훔쳐보기";}
	   else if(item_num == 1){effect = "마지막 카드 덱에서 바꾸기";}
	   else if(item_num == 2){effect = "이겼을 때 보상 두 배";}
	   else if(item_num == 3){effect = "이겼을 때 보상 세 배";}
	   else if(item_num == 4){effect = "BUST됐을 때, 마지막 카드 버리기";}
	   else if(item_num == 5){effect = "패배 시에 배팅 금액을 돌려받기";}}  
}
}




        


	
	