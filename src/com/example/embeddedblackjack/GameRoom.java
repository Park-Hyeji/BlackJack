package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
	List<GameUser> userList;
	GameUser roomOwner; // ����
    String roomName; // �� �̸�
    
	public GameRoom() { // �ƹ��� ���� ���� ������ ��
        userList = new ArrayList<GameUser>();
    }
	
	public GameRoom(GameUser _user) { // ������ ���� ���鶧
        userList = new ArrayList<GameUser>();
        userList.add(_user); // ������ �߰���Ų ��
        this.roomOwner = _user; // ������ ������ �����.
    }
	
	public void EnterRoom(GameUser _user) {
        userList.add(_user);
    }
	
	public void ExitRoom(GameUser _user) {
        userList.remove(_user);
 
        if (userList.size() < 1) { // ��� �ο��� �� ���� �����ٸ�
            //RoomManager.RemoveRoom(this); // �� ���� �����Ѵ�.
            return;
        }
 
        if (userList.size() < 2) { // �濡 ���� �ο��� 1�� ���϶�
            this.roomOwner = userList.get(0); // ����Ʈ�� ù��° ������ ������ �ȴ�.
            return;
        }
    }
	
	public void SetRoomName(String _name) { // �� �̸��� ����
        this.roomName = _name;
    }
	
	public String GetRoomName() { // �� �̸��� ������
        return roomName;
    }
}
