package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
	static List<GameRoom> roomList; // 방의 리스트
	
	public RoomManager(){
        roomList = new ArrayList<GameRoom>();
    }
}
