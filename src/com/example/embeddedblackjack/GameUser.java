package com.example.embeddedblackjack;

import java.net.Socket;

public class GameUser {
	GameRoom room; //유저가 속한 룸
	Socket sock;
	String nickName;
	int uid;
	
	//게임에 관련된 변수 설정
	PlayerGameInfo.Location playerLocation; // 게임 정보
    PlayerGameInfo.Status playerStatus; // 게임 정보
	
	public GameUser(){//정보없는 유저 생성
		
	}
	
	public GameUser(String _nickName){
		this.nickName = _nickName;
	}
	
	public GameUser(int _uid, String _nickName){
		//UID랑 닉네임 가지고 시작
		this.uid = _uid;
		this.nickName = _nickName;
	}
	
	public void EnterRoom(GameRoom _room){
		_room.EnterRoom(this); //룸 입장
		this.room = _room; // 룸 변경
	}
	
	public void SetPlayerLocation(PlayerGameInfo.Location _location) { // 유저의 위치를 설정
        this.playerLocation = _location;
    }
}
