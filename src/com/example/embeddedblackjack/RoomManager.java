package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
	static List<GameRoom> roomList; // ���� ����Ʈ
	
	public RoomManager(){
        roomList = new ArrayList<GameRoom>();
    }
	
	public GameRoom CreateRoom(){ // �� ���� ����
		GameRoom room = new GameRoom();
		roomList.add(room); //�븮��Ʈ�� �� �߰�
		return room;
	}
	
	public GameRoom CreateRoom(GameUser _owner){ //������ ���� ����(������ ����)
		GameRoom room = new GameRoom(_owner);
		roomList.add(room);
		return room;
	}
	
	public static void RemoveRoom(GameRoom _room){
		roomList.remove(_room); //���޹��� �� ����
	}
	
	public static int RoomCount(){return roomList.size();}
}
