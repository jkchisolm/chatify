package newChatProject;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws")
public class ServerSocket {

	private static Vector<Session> sessionVector = new Vector<Session>();
	private static ConcurrentHashMap<String, Session> UserSessionMap = new ConcurrentHashMap<>();
//	This needs to be replaced by Leilani's matching function (it is an example):
	private static ConcurrentHashMap<String, String> matchesExample = new ConcurrentHashMap<>();
//	private static ConcurrentHashMap<String, Vector<String>> unreadMessages = new ConcurrentHashMap<>();

	public ServerSocket() {
//	This needs to be removed (it's just for the example):
		matchesExample.put("Jaiv", "Luke");
		matchesExample.put("Luke", "Jaiv");
		matchesExample.put("Sam", "Clay");
		matchesExample.put("Clay", "Sam");
	}
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Connection made!");
		sessionVector.add(session);
	}
	
//	public boolean isOnline(Session session) {
//		for(Session s : sessionVector) {
//			if(s == session) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	@OnMessage
//	Do not uncomment the unreadMessages functionality (don't deal with it):
	public void onMessage(String message, Session session) {
		// Split String into Components:
		System.out.println(message);
		String[] arrOfStr = message.split("/", 3);
		
		// If Initial Message:
		if(arrOfStr[1].equals("0")) {
			UserSessionMap.put(arrOfStr[0], session);
			String match = matchesExample.get(arrOfStr[0]);
			try {
				session.getBasicRemote().sendText("Your match is: " + match);
				session.getBasicRemote().sendText("Also btw, " + match + "'s favorite song is Heat Waves by Glass Animals and ");
				session.getBasicRemote().sendText("their favorite genre is Indie Pop ;)");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close(session);
			}
//			if(unreadMessages.containsKey(arrOfStr[1]) == false) {
//				Vector<String> tempVec = new Vector<String>();
//				unreadMessages.put(arrOfStr[0], tempVec);
//			}
//			
//			if(unreadMessages.get(arrOfStr[0]).isEmpty() == false) {
//				while(unreadMessages.get(arrOfStr[0]).isEmpty() == false) {
//					String tempMessage = unreadMessages.get(arrOfStr[1]).remove(0);
//					try {
//						session.getBasicRemote().sendText(tempMessage);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						close(session);
//					}
//				}
//			}
		}
		
		// If Other Message:
		else if(arrOfStr[1].equals("1")) {
			System.out.println("Here!");
			String match = matchesExample.get(arrOfStr[0]);
			System.out.println(match);
			Session matchSession = UserSessionMap.get(match);
//			if(isOnline(matchSession)) {
				try {
					matchSession.getBasicRemote().sendText(arrOfStr[0] + ": " + arrOfStr[2]);
					session.getBasicRemote().sendText(arrOfStr[0] + ": " + arrOfStr[2]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					close(session);
				}
//			}
//			else {
//				String match = matchesExample.get(arrOfStr[0]);
//				unreadMessages.get(match).addElement(message);
//			}
		}
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Disconnecting!");
		sessionVector.remove(session);
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error!");
	}
}