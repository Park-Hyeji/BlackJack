package com.example.embeddedblackjack;

import java.util.ArrayList;

import android.widget.Toast;

public class Item {
   /*
    * 딜러 카드 훔쳐보기
    * 카드 한 장 덱에서 바꾸기
    * 이겼을 때, 보상 X2
    * 다른 플레이어와 카드 한 장 바꾸기
    * BUST됐을 때, 카드 한 장 버리기
    * 패배 시에 배팅 금액을 돌려받기
    */
   
   String nowCard(){
	   //이번 경기에서 어떤 카드를 받으면 어떤 아이템을 얻는지 보여줌
	   //한 게임당 아이템 1개
	   String One_Card_Set[] = { "♠A", "♠2", "♠3", "♠4", "♠5", "♠6", "♠7", "♠8", "♠9", "♠10", "♠J", "♠Q", "♠K",
				  				"◆A", "◆2", "◆3", "◆4", "◆5", "◆6", "◆7", "◆8", "◆9", "◆10", "◆J", "◆Q", "◆K",
				  				"♥A", "♥2", "♥3", "♥4", "♥5", "♥6", "♥7", "♥8", "♥9", "♥10", "♥J", "♥Q", "♥K",
				  				"♣A", "♣2", "♣3", "♣4", "♣5", "♣6", "♣7", "♣8", "♣9", "♣10", "♣J", "♣Q", "♣K"};
	   int cardRan = (int) (Math.random() * 52);
	   String cardName = One_Card_Set[cardRan];
	   return cardName;
   }
   String nowItem(){
	   //이번 경기에서 어떤 카드를 받으면 어떤 아이템을 얻는지 보여줌
	   //한 게임당 아이템 1개
	   int itemRan = (int) (Math.random() * 6);
	   String itemName;
	   if(itemRan == 0){
		   itemName = "딜러 카드 훔쳐보기";
	   }else if(itemRan == 1){
		   itemName = "카드 한 장 덱에서 바꾸기";
	   }else if(itemRan == 2){
		   itemName = "이겼을 때 보상 두 배";
	   }else if(itemRan == 3){
		   itemName = "다른 플레이어와 카드 한 장 바꾸기";
	   }else if(itemRan == 4){
		   itemName = "BUST됐을 때, 카드 한 장 버리기";
	   }else{
		   itemName = "패배 시에 배팅 금액을 돌려받기";
	   }
	   return itemName;
   }
   
   String peek(Dealer d){
	   //딜러 카드 훔쳐보기
	   String dealerTwo = (String) d.Unknown; 
	   return dealerTwo;
   }
   
   void cardChange(){
	   //카드 한 장 덱에서 바꾸기
	   //선택한 카드 다시 덱에 섞기
	   //빼고 싶은 카드 선택하는 창 띄우면 좋을 듯
	   //해당 인덱스 값 받아와서 그 값 DELETE하고 전체 덱 배열에 넣어줌
	   //! player.Card.remove(i); //i = 인덱스 값
	   //! deck.cardShuffled.add(마지막 인덱스) = player.Card.indexOf(i); //배열이 어떻게 되는건지 모르겠음
   }
   
   void doubleWin(Player p){
	   //이겼을 때, 보상 X2
	   p.total += p.bet * 2;
   }
   
   void otherPlayerCardChange(){
	   //다른 플레이어와 카드 한 장 바꾸기
	   //다른 플레이어 배열생기면 swap 고고
   }
   
   void abandonCard(Player p){
	   //Bust, 카드 한 장 버리기
	   if(p.score > 21 && p.hasAce != true){
		   //! player.Card.remove(i); //i = 인덱스 값
	   }
   }
   
   void getBackBet(Player p){
	   //금액 돌려받기
	   //BUST 혹은 딜러한테 짐
	   p.total += p.bet; //어디에서 BET금액을 빼는지 몰라서 걍 한번 더 더해 마이너스 되는 돈을 0으로 만듦  
   }
}