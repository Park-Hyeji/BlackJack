package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
	static List<GameRoom> roomList; // ���� ����Ʈ
	
	public RoomManager(){
        roomList = new ArrayList<GameRoom>();
    }
}
