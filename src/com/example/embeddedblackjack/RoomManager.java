package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
	static List<GameRoom> roomList; // 방의 리스트
	
	public RoomManager(){
        roomList = new ArrayList<GameRoom>();
    }
	
	public GameRoom CreateRoom(){ // 룸 새로 생성
		GameRoom room = new GameRoom();
		roomList.add(room); //룸리스트에 룸 추가
		return room;
	}
	
	public GameRoom CreateRoom(GameUser _owner){ //유저가 방을 생성(유저가 방장)
		GameRoom room = new GameRoom(_owner);
		roomList.add(room);
		return room;
	}
	
	public static void RemoveRoom(GameRoom _room){
		roomList.remove(_room); //전달받은 룸 제거
	}
	
	public static int RoomCount(){return roomList.size();}
}
