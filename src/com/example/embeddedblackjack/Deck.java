package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.Arrays;

public class Deck {
	  final String One_Card_Set[] = { "¢¼A", "¢¼2", "¢¼3", "¢¼4", "¢¼5", "¢¼6", "¢¼7", "¢¼8", "¢¼9", "¢¼10", "¢¼J", "¢¼Q", "¢¼K",
			  						  "¡ßA", "¡ß2", "¡ß3", "¡ß4", "¡ß5", "¡ß6", "¡ß7", "¡ß8","¡ß9","¡ß10","¡ßJ","¡ßQ","¡ßK",
			  						  "¢¾A", "¢¾2", "¢¾3", "¢¾4", "¢¾5", "¢¾6", "¢¾7", "¢¾8", "¢¾9", "¢¾10", "¢¾J", "¢¾Q", "¢¾K",
			  						  "¢ÀA", "¢À2", "¢À3", "¢À4", "¢À5", "¢À6", "¢À7", "¢À8", "¢À9", "¢À10", "¢ÀJ", "¢ÀQ", "¢ÀK"};
	  int num_of_deck;
	  int num_of_shuffle;
	  int nth_card;
	  int nth_shuffle;
	  
	  ArrayList <String>Total_Card_Set;
	  String[] Card_Shuffled;
	
	  Deck(int num_of_deck, int num_of_shuf){
		  this.num_of_deck = num_of_deck;
		  this.num_of_shuffle = num_of_shuf;
		  this.nth_card = 0;
		  this.nth_shuffle = 0;
		  
		  Total_Card_Set = new ArrayList<String>();
		  Card_Shuffled = new String[52 * num_of_deck];
		  for(int i=0; i<num_of_deck; i++)
		  {Total_Card_Set.addAll(Arrays.asList(One_Card_Set));}}
	  
	  void Shuffle()
	  {
		  for(int i =0; i<num_of_deck * 52; i++)
		  {
	            int r = (int) (Math.random() * i);
	            String swap = Total_Card_Set.get(i);
	            Card_Shuffled[i] = Total_Card_Set.get(r);
	            Card_Shuffled[r] = swap;
		  }
	  }
	  
	  String distribute_card()
	  {if(nth_card < num_of_deck * 52) 
		  return Card_Shuffled[nth_card++];
	  else 
	  {
		  if(nth_shuffle != num_of_shuffle)
		  {
			  nth_card = 0;
			  Shuffle();
			  return distribute_card();
		  }
		  else
			  return "GameOver";
	  }
	  }
}
