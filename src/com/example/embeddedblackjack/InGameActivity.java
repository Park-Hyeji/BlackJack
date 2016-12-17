package com.example.embeddedblackjack;

import java.util.ArrayList;

import com.example.embeddedblackjack.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	/*new*/
	ArrayList<Player> p_list = new ArrayList<Player>(); 
	ArrayList<Ai> Ai_list = new ArrayList<Ai>();
	ArrayList<Player> remain_p_list = new ArrayList<Player>(); 
	ArrayList<Ai> remain_Ai_list = new ArrayList<Ai>();
	
	Item item = new Item();
	
	Player Me,p1,p2,p3; //�÷��̾� 
	Ai ai1, ai2, ai3, ai4; //Ai
	final Dealer dealer = new Dealer(); //���� ����
	
	Deck deck; //�� ����
	
	int levelCnt;
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);
        System.loadLibrary("blackjack");
        
    	//TextView(�ؽ�Ʈ ��)
    	final TextView deckNum = (TextView)findViewById(R.id.deckNum); //Deck�� ���� ǥ�� Text View
        final TextView total_money = (TextView)findViewById(R.id.cashNum); //��ü ���� �ݾ� Text View
        final TextView bet_money = (TextView)findViewById(R.id.betNum); //���ñݾ� Text View
        final TextView cardAndItem = (TextView)findViewById(R.id.cardAndItem); //ī��� ������ �����ִ� Text View
        final TextView Score_board = (TextView)findViewById(R.id.splitView1); //�÷��̾� ���� ��
        final TextView Split_Card = (TextView)findViewById(R.id.splitCardSaveArea); //Split�Ǿ� �÷��� ��� ���� ī��
        
        //Button(��ư)
    	final Button hitBtn = (Button)findViewById(R.id.hit);
    	final Button stayBtn = (Button)findViewById(R.id.Stay);
    	final Button splitBtn = (Button)findViewById(R.id.Split);
    	final Button doubleBtn = (Button)findViewById(R.id.Double);

    	final Button chip_1 = (Button)findViewById(R.id.chip_1);
    	final Button chip_5 = (Button)findViewById(R.id.chip_5);
    	final Button chip_20 = (Button)findViewById(R.id.chip_20);
    	final Button chip_100 = (Button)findViewById(R.id.chip_100);
    	final Button chip_500 = (Button)findViewById(R.id.chip_500);
    	
    	final Button gameStartBtn = (Button)findViewById(R.id.gameStartBtn);
        
    	//���� ��ư ó���� enable, ���� �����ؾ� ������ ��
    	hitBtn.setEnabled(false);
    	stayBtn.setEnabled(false);
    	splitBtn.setEnabled(false);
    	doubleBtn.setEnabled(false);

    	//������ �޾ƿ���
        Intent intent = getIntent();
        final int playerCnt = intent.getIntExtra("playerCnt",0);
        final int aiCnt = intent.getIntExtra("aiCnt",0);
        levelCnt = intent.getIntExtra("levelCnt",0);
       
        int total_player = 0;
        total_player = playerCnt + aiCnt;
        
        ListView spot1 = (ListView)findViewById(R.id.player1);
        ListView spot2 = (ListView)findViewById(R.id.player2);
        ListView spot3 = (ListView)findViewById(R.id.player3);
        ListView spot4 = (ListView)findViewById(R.id.player4);
        ListView spot5 = (ListView)findViewById(R.id.player5);
        ListView spot6 = (ListView)findViewById(R.id.player6);
        
        ListView spot7 = (ListView)findViewById(R.id.dealer);

        if(total_player < 6){spot6.setVisibility(View.GONE);}
        if(total_player < 5){spot5.setVisibility(View.GONE);}
        if(total_player < 4){spot4.setVisibility(View.GONE);}
        if(total_player < 3){spot3.setVisibility(View.GONE);}
        
        //Player ���� ���ÿ� ���� Player ����
        if(playerCnt >= 1){p1 = new Player(); p_list.add(p1);} //�÷��̾�1 ����
        if(playerCnt >= 2){p2 = new Player(); p_list.add(p2);} //�÷��̾�2 ����
        if(playerCnt == 3){p3 = new Player(); p_list.add(p3);} //�÷��̾�3 ����
        
        //Ai ���� ���ÿ� ���� Ai����
        if(aiCnt >= 1){
        	ai1 = new Ai(2000, 0.9, 0.6);
        	ai1.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai1.Card);
            spot2.setAdapter(ai1.adapter);
        	Ai_list.add(ai1);	
        }
        if(aiCnt >= 2){
        	ai2 = new Ai(1500, 0.8, 0.4);
            ai2.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai2.Card);
            spot3.setAdapter(ai2.adapter);
        	Ai_list.add(ai2);
        }
        if(aiCnt >= 3){
        	ai3 = new Ai(1000, 0.75, 0.25); 
            ai3.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai3.Card);
            spot4.setAdapter(ai3.adapter);
        	Ai_list.add(ai3);}
        if(aiCnt >= 4){
        	ai4 = new Ai(500, 0.65, 0.15); 
            ai4.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, ai4.Card);
            spot5.setAdapter(ai4.adapter);
        	Ai_list.add(ai4);
        }
        
        if(levelCnt == 1){deckNum.setText("1 DECK");}
        else if(levelCnt == 2){deckNum.setText("2 DECK");}
        else if(levelCnt == 4){deckNum.setText("4 DECK");}
        else if(levelCnt == 6){deckNum.setText("6 DECK");}
        
        
        deck = new Deck(levelCnt);
    	deck.Shuffle();	
    	deck.Card_Shuffled[0] = "��A";
    	deck.Card_Shuffled[3] = "��A";
    	
    	Me = p1; //���� �Ҵ�
     	Me.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, Me.Card);
        spot1.setAdapter(Me.adapter);
     	total_money.setText(Integer.toString(Me.total)); //�÷��̾ ������ �� �ݾ�
     	bet_money.setText(Integer.toString(Me.bet)); //�÷��̾ ������ �ݾ�
       
        //���� ����
        dealer.adapter = new ArrayAdapter<String>(this,R.layout.simpleitem, dealer.Card);
        ListView dealer_spot = (ListView)findViewById(R.id.dealer);
        dealer_spot.setAdapter(dealer.adapter);
        
     	//���� �κ� ��ư
     	chip_1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+1 <= Me.total){
					Me.bet = Me.bet +1;
					bet_money.setText(Integer.toString(Me.bet));}
				else Toast.makeText(getApplicationContext(), "�����Ͻ� �ݾ��� �ʰ��Ͽ����ϴ�.", Toast.LENGTH_LONG).show();}
		});
     	chip_5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+5 <= Me.total){
					Me.bet = Me.bet + 5;
					bet_money.setText(Integer.toString(Me.bet));}
				else
					Toast.makeText(getApplicationContext(), "�����Ͻ� �ݾ��� �ʰ��Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_20.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+20 <= Me.total){
					Me.bet = Me.bet + 20;
					bet_money.setText(Integer.toString(Me.bet));}
				else
					Toast.makeText(getApplicationContext(), "�����Ͻ� �ݾ��� �ʰ��Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_100.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+100 <= Me.total){
					Me.bet = Me.bet + 100; 
					bet_money.setText(Integer.toString(Me.bet));
					}
				else
					Toast.makeText(getApplicationContext(), "�����Ͻ� �ݾ��� �ʰ��Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
			}
		});
     	chip_500.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(Me.bet+500 <= Me.total){
					Me.bet = Me.bet + 500;
					bet_money.setText(Integer.toString(Me.bet));
				}
				else
					Toast.makeText(getApplicationContext(), "�����Ͻ� �ݾ��� �ʰ��Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
			}
		});
     	
        gameStartBtn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
					
					//�̹� ���ӿ��� �� ī�� ������ �� ������ ���� ǥ��
			    	String cardName = item.nowCard();
			    	String itemName = item.nowItem();
			    	cardAndItem.setText("'"+cardName+"' ī�� ȹ��� \n '"+itemName+"' ����");
			    	
			    	//if(){
			    		//������ ���� �� ���ĺ��� �������� ��� �� �������� ����Ͽ��� ��,
			    		//String dealerTwo = item.peek(dealer);
				        //Toast.makeText(getApplicationContext(), "������ �� ��° ī��� '"+dealerTwo+"' �Դϴ�.", Toast.LENGTH_LONG).show();
			    	//}					
			    }
				else{Toast.makeText(getApplicationContext(), "������ �ؾ� ������ �����մϴ�.", Toast.LENGTH_LONG).show();}}
		});
        
		hitBtn.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Me.Hit(deck.distribute_card());
				Me.adapter.notifyDataSetChanged();
				if(Me.bust){
        			for(int i=0; i<Ai_list.size(); i++)
        			{Ai_turn(Ai_list.get(i));}
					Dealer_turn();
					Me.bet = 0;
					total_money.setText(Integer.toString(Me.total));
					
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
				else
					{
						try {Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();}
						Me.adapter.notifyDataSetChanged();
					}
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
        			
        			String score_temp = (String)Score_board.getText();
        			String score_temp2 = Me.score_board.get(Me.score_board.size()-1).toString();
        			Score_board.setText(score_temp + " " + score_temp2);
        			
        			String temp = (String) Split_Card.getText();
        			temp = temp.replaceAll((String)Me.Card.get(0), "");
        			temp = temp.replaceAll(" ", "");
        			Split_Card.setText(temp);
        		}
        		else
        		{
        			Me.Stay();
        			for(int i=0; i<Ai_list.size(); i++)
        			{Ai_turn(Ai_list.get(i));}
        			Dealer_turn();
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
        splitBtn.setOnClickListener(new Button.OnClickListener()
        { 
        	public void onClick(View arg0) {
        	// TODO Auto-generated method stub
        		Me.Split();
        		Me.adapter.notifyDataSetChanged();
        		String temp = (String) Split_Card.getText();
        		String temp2 = (String) Me.Splited.get(0);
        		Split_Card.setText(temp + " " + temp2);
			}
        });
        doubleBtn.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Me.bet *= 2;
				Me.Hit(deck.distribute_card());
				Me.adapter.notifyDataSetChanged();
			}
        });
    }
    
    void init(){
			Toast.makeText(getApplicationContext(), "������ ���۵˴ϴ�.", Toast.LENGTH_LONG).show();
			//��� �÷��̾� �� Ai �غ�
			Me.Ready();
			dealer.Ready();
			for(int i=0; i<Ai_list.size(); i++)
			{
				Ai_list.get(i).Ready();
				Ai_list.get(i).bet();
			}
			
			
			Me.Hit(deck.distribute_card());
			Me.adapter.notifyDataSetChanged();
	    	
			for(int i=0; i< Ai_list.size(); i++)
			{
				Ai_list.get(i).Hit(deck.distribute_card());
				Ai_list.get(i).adapter.notifyDataSetChanged();
			}
	    	
	    	dealer.recieve(deck.distribute_card());
	    	dealer.adapter.notifyDataSetChanged();
	    	
	    	Me.Hit(deck.distribute_card());
	    	Me.adapter.notifyDataSetChanged();
	    	
			for(int i=0; i< Ai_list.size(); i++)
			{
				Ai_list.get(i).Hit(deck.distribute_card());
				Ai_list.get(i).adapter.notifyDataSetChanged();
			}
	    	
	    	dealer.recieve(deck.distribute_card());
	    	dealer.Unknown = (String) dealer.Card.get(1);
	    	dealer.Card.remove(1);
	    	dealer.Card.add(1,"unknown");
	    	dealer.adapter.notifyDataSetChanged();
	}
    void Ai_turn(Ai ai) 
    {
    	while(!ai.bust){
    		if(ai.score <= 11)
    		{
    			ai.Hit(deck.distribute_card());
    			ai1.adapter.notifyDataSetChanged();
    		}
    		else if(ai.score > 11 && ai.score < 16)
    		{
    			double temp;
    			temp = Math.random();
    			if(temp <= ai.prob_hit_12)
    			{
    				ai.Hit(deck.distribute_card());
    				ai.adapter.notifyDataSetChanged();
    			}
    			else
    				break;
    		}
    		else if(ai.score >= 16)
    		{
    			double temp;
    			temp = Math.random();
    			if(temp <= ai.prob_hit_16)
    			{
    				ai.Hit(deck.distribute_card());
    				ai.adapter.notifyDataSetChanged();
    			}
    			else
    				break;
    		}
    	}
    }
    void Dealer_turn()
    {
    	dealer.Card.remove(1);
    	dealer.Card.add(1,dealer.Unknown);
    	dealer.adapter.notifyDataSetChanged();
    	
    	while(dealer.score < 16 && !dealer.bust)
    	{
    		dealer.recieve(deck.distribute_card());
    		dealer.adapter.notifyDataSetChanged();
    	}
    	
    	if(!Me.bust)
    	{
    		while(!Me.score_board.isEmpty())
    		{
    			int score = (Integer) Me.score_board.get(0);
    			if(score > dealer.score)
    				Me.total = Me.total + Me.bet;
    			if(score < dealer.score)
    				Me.total = Me.total - Me.bet;
    			Me.score_board.remove(0);
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
    }

}
        


	
	