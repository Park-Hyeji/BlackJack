package com.example.embeddedblackjack;

import java.net.Socket;

public class GameUser {
	GameRoom room; //������ ���� ��
	Socket sock;
	String nickName;
	int uid;
	
	//���ӿ� ���õ� ���� ����
	PlayerGameInfo.Location playerLocation; // ���� ����
    PlayerGameInfo.Status playerStatus; // ���� ����
	
	public GameUser(){//�������� ���� ����
		
	}
	
	public GameUser(String _nickName){
		this.nickName = _nickName;
	}
	
	public GameUser(int _uid, String _nickName){
		//UID�� �г��� ������ ����
		this.uid = _uid;
		this.nickName = _nickName;
	}
	
	public void EnterRoom(GameRoom _room){
		_room.EnterRoom(this); //�� ����
		this.room = _room; // �� ����
	}
	
	public void SetPlayerLocation(PlayerGameInfo.Location _location) { // ������ ��ġ�� ����
        this.playerLocation = _location;
    }
}
