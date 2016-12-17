package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
	List<GameUser> userList;
	GameUser roomOwner; // 방장
    String roomName; // 방 이름
    
	public GameRoom() { // 아무도 없는 방을 생성할 때
        userList = new ArrayList<GameUser>();
    }
	
	public GameRoom(GameUser _user) { // 유저가 방을 만들때
        userList = new ArrayList<GameUser>();
        userList.add(_user); // 유저를 추가시킨 후
        this.roomOwner = _user; // 방장을 유저로 만든다.
    }
	
	public void EnterRoom(GameUser _user) {
        userList.add(_user);
    }
	
	public void ExitRoom(GameUser _user) {
        userList.remove(_user);
 
        if (userList.size() < 1) { // 모든 인원이 다 방을 나갔다면
            //RoomManager.RemoveRoom(this); // 이 방을 제거한다.
            return;
        }
 
        if (userList.size() < 2) { // 방에 남은 인원이 1명 이하라
            this.roomOwner = userList.get(0); // 리스트의 첫번째 유저가 방장이 된다.
            return;
        }
    }
	
	public void SetRoomName(String _name) { // 방 이름을 설정
        this.roomName = _name;
    }
	
	public String GetRoomName() { // 방 이름을 가져옴
        return roomName;
    }
}
