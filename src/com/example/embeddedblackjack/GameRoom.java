package com.example.embeddedblackjack;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
	
	//Player = GameUser
	List<Player> userList;
	Player roomOwner; // ����
    String roomName; // �� �̸�
    
	public GameRoom() { // �ƹ��� ���� ���� ������ ��
        userList = new ArrayList<Player>();
    }
	
	public GameRoom(Player _user) { // ������ ���� ���鶧
        userList = new ArrayList<Player>();
        userList.add(_user); // ������ �߰���Ų ��
        this.roomOwner = _user; // ������ ������ �����.
    }
	
	public void EnterRoom(Player _user) {
        userList.add(_user);
    }
	
	public void ExitRoom(Player _user) {
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
