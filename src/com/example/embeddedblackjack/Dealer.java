package com.example.embeddedblackjack;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class Dealer {
	int score = 0;
	int nth_card = 0;
	String Unknown;
	boolean bust = false;
	ArrayList Card = new ArrayList<String>(); //받은 카드 저장
	ArrayAdapter<String> adapter;
	
	void Ready(){
	 	Card.clear();
	 	nth_card = 0;
	 	score = 0;
	 	bust = false;
	}
	void recieve(String card){
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
			for(int i=0; i<nth_card; i++)
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
			
			if(hasAce == true)
			{
			  String Temp = (String)Card.get(AceIndex);
			  Temp = Temp.replaceAll("A", "a");
			  Card.remove(AceIndex);
			  Card.add(AceIndex, Temp);
			  score = score - 10;
			 }
			else
				Bust();
		}
	}
	void Bust(){
		bust = true;
		score = 0;
	};
	
	
}
