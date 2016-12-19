package com.example.embeddedblackjack;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class Ai extends Thread{
	int total = 15000;
	int bet = 0;
	int max_bet = 0;
	int nth_card = 0;
	int score = 0;
	double prob_hit_12 = 0;
	double prob_hit_16 = 0;
	boolean bust = false;
	boolean insurance = false;
	boolean Even_money = false;
	boolean done = false;
	boolean BlackJack = false;
	boolean stay = false;
	
	ArrayList Card = new ArrayList<String>(); //받은 카드 저장
	ArrayList Spilted =  new ArrayList<String>(); //Spilted된 카드 저장
	ArrayAdapter<String> adapter;
	
	Ai(int max_bet, double prob_hit_12, double prob_hit_16)
	{
		this.max_bet = max_bet;
		this.prob_hit_12 = prob_hit_12;
		this.prob_hit_16 = prob_hit_16;
	}
	
	void Ready(){
	 	Card.clear();
	 	bet = 0;
	 	nth_card = 0;
	 	score = 0;
	 	bust = false;
	 	stay = false;
		insurance = false;
		Even_money = false;
	}
	void bet(){
		if(total > max_bet) bet = (int)(Math.random()* max_bet);
		else bet = (int)(Math.random() * total);
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
			boolean hasAce = false;
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
	void Bust(){
		
		total = total - bet;
		score = 0;
		if(!Spilted.isEmpty())
		{
			nth_card = 0;
		  	Card.clear();
		  	Hit((String) Spilted.get(0));
		  	Spilted.remove(0);
		}
		else 
			bust = true;
	}
	void Split(){
		if(Card.size() == 2)
		{
			String c1 = (String) Card.get(0);
			String c2 = (String) Card.get(1);
		
			if(c1.charAt(1) == c2.charAt(1) || c1.charAt(1) == 'a' && c2.charAt(1) == 'A')
			{
				Spilted.add(Card.get(1));
				score /= 2;
				Card.remove(1);
				nth_card--;
			}
			if(c1.charAt(1) == 'a' && c2.charAt(1) == 'A')
			{
				Spilted.add(Card.get(1));
			
				String Temp = (String)Card.get(0);
				Temp = Temp.replaceAll("a", "A");
				Card.remove(0);
				Card.add(0,Temp);
			
				score = 11;
				Card.remove(1);
				nth_card--;
			}
		}
	}
	
}
