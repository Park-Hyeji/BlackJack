package com.example.embeddedblackjack;

import java.util.ArrayList;

import android.widget.Toast;

public class Item {
   /*
    * ���� ī�� ���ĺ���
    * ī�� �� �� ������ �ٲٱ�
    * �̰��� ��, ���� X2
    * �ٸ� �÷��̾�� ī�� �� �� �ٲٱ�
    * BUST���� ��, ī�� �� �� ������
    * �й� �ÿ� ���� �ݾ��� �����ޱ�
    */
   
   String nowCard(){
	   //�̹� ��⿡�� � ī�带 ������ � �������� ����� ������
	   //�� ���Ӵ� ������ 1��
	   String One_Card_Set[] = { "��A", "��2", "��3", "��4", "��5", "��6", "��7", "��8", "��9", "��10", "��J", "��Q", "��K",
				  				"��A", "��2", "��3", "��4", "��5", "��6", "��7", "��8", "��9", "��10", "��J", "��Q", "��K",
				  				"��A", "��2", "��3", "��4", "��5", "��6", "��7", "��8", "��9", "��10", "��J", "��Q", "��K",
				  				"��A", "��2", "��3", "��4", "��5", "��6", "��7", "��8", "��9", "��10", "��J", "��Q", "��K"};
	   int cardRan = (int) (Math.random() * 52);
	   String cardName = One_Card_Set[cardRan];
	   return cardName;
   }
   String nowItem(){
	   //�̹� ��⿡�� � ī�带 ������ � �������� ����� ������
	   //�� ���Ӵ� ������ 1��
	   int itemRan = (int) (Math.random() * 6);
	   String itemName;
	   if(itemRan == 0){
		   itemName = "���� ī�� ���ĺ���";
	   }else if(itemRan == 1){
		   itemName = "ī�� �� �� ������ �ٲٱ�";
	   }else if(itemRan == 2){
		   itemName = "�̰��� �� ���� �� ��";
	   }else if(itemRan == 3){
		   itemName = "�ٸ� �÷��̾�� ī�� �� �� �ٲٱ�";
	   }else if(itemRan == 4){
		   itemName = "BUST���� ��, ī�� �� �� ������";
	   }else{
		   itemName = "�й� �ÿ� ���� �ݾ��� �����ޱ�";
	   }
	   return itemName;
   }
   
   String peek(Dealer d){
	   //���� ī�� ���ĺ���
	   String dealerTwo = (String) d.Unknown; 
	   return dealerTwo;
   }
   
   void cardChange(){
	   //ī�� �� �� ������ �ٲٱ�
	   //������ ī�� �ٽ� ���� ����
	   //���� ���� ī�� �����ϴ� â ���� ���� ��
	   //�ش� �ε��� �� �޾ƿͼ� �� �� DELETE�ϰ� ��ü �� �迭�� �־���
	   //! player.Card.remove(i); //i = �ε��� ��
	   //! deck.cardShuffled.add(������ �ε���) = player.Card.indexOf(i); //�迭�� ��� �Ǵ°��� �𸣰���
   }
   
   void doubleWin(Player p){
	   //�̰��� ��, ���� X2
	   p.total += p.bet * 2;
   }
   
   void otherPlayerCardChange(){
	   //�ٸ� �÷��̾�� ī�� �� �� �ٲٱ�
	   //�ٸ� �÷��̾� �迭����� swap ���
   }
   
   void abandonCard(Player p){
	   //Bust, ī�� �� �� ������
	   if(p.score > 21 && p.hasAce != true){
		   //! player.Card.remove(i); //i = �ε��� ��
	   }
   }
   
   void getBackBet(Player p){
	   //�ݾ� �����ޱ�
	   //BUST Ȥ�� �������� ��
	   p.total += p.bet; //��𿡼� BET�ݾ��� ������ ���� �� �ѹ� �� ���� ���̳ʽ� �Ǵ� ���� 0���� ����  
   }
}