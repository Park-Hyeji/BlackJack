package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

public class Player {
	int total = 15000;
	int bet = 0;
	int nth_card = 0;
	int score = 0;
	int insurance_bet = 0;
	boolean bust = false;
	boolean insurance = false;
	boolean Even_money = false;
	boolean hasAce;
	
	ArrayList Card = new ArrayList<String>(); //받은 카드 저장
	ArrayList Splited =  new ArrayList<String>(); //Spilted된 카드 저장
	ArrayList score_board = new ArrayList<Integer>();
	boolean Double_down[] = new boolean[15];
	
	//ArrayList Item_list = new ArrayList<Item>();
	ArrayAdapter<String> adapter;
	
	void Ready(){
	 	Card.clear();
	 	Splited.clear();
	 	score_board.clear();
	 	nth_card = 0;
	 	score = 0;
	 	bust = false;
		insurance = false;
		Even_money = false;
		for(int i=0; i<10; i++)
			Double_down[i] = false;
	}
	void Hit(String card){
		nth_card++;
		Card.add(card);
		char var = card.charAt(1);	
		
		if(var == 'J' || var == 'Q' || var =='K')
		{score = score + 10;}
		else if(var =='A'){score = score + 11;}
		else if(var == '1'){score = score + 10;}
		else {score = score + (var - 48);}
		
		if(score > 21)
		{
			hasAce = false;
			int AceIndex = 0;
			for(int i=0; i < nth_card; i++)
			{
				String c = (String)Card.get(i);
				char check = c.charAt(1);
				if(check == 'A')
				{
					hasAce = true;
					AceIndex = i;
					break;
				}
			}
			if(hasAce)
			{
			  String Temp = (String)Card.get(AceIndex);
			  Temp = Temp.replaceAll("A", "a");
			  Card.remove(AceIndex);
			  Card.add(AceIndex, Temp);
			  score = score - 10;
			}
			else
			{	
				Bust();
			}
		}
	}
	void Stay(){score_board.add(score);}
	void Bust(){
		total = total - bet;
		score = 0;
		if(!Splited.isEmpty())
		{nth_card = 0;
		 Card.clear();
		 Hit((String) Splited.get(0));
		 Splited.remove(0);}
		else 
			bust = true;}
	void Continue(){
			score_board.add(score);
			score = 0;
			Card.clear();
			nth_card = 0;
		  	Card.clear();
		  	Hit((String) Splited.get(0));
		  	Splited.remove(0);}
	void Split(){
			String c1 = (String)Card.get(0);
			String c2 = (String)Card.get(1);
		
			if(c1.charAt(1) == c2.charAt(1))
			{
				Splited.add(Card.get(1));
				score /= 2;
				Card.remove(1);
				nth_card--;
			}
			if(c1.charAt(1) == 'a' && c2.charAt(1) == 'A')
			{
				Splited.add(Card.get(1));
				String Temp = (String)Card.get(0);
				Temp = Temp.replaceAll("a", "A");
				Card.remove(0);
				Card.add(0,Temp);
			
				score = 11;
				Card.remove(1);
				nth_card--;
			}
		}
	void Double_down(){}
}
