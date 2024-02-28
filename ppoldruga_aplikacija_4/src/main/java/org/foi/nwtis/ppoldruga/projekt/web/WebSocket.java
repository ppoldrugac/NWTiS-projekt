package org.foi.nwtis.ppoldruga.projekt.web;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/info")
public class WebSocket {

  private static ArrayList<Session> sesije = new ArrayList<>();

  @OnOpen
  public void onOpen(Session sesija) {
    sesije.add(sesija);
  }

  @OnClose
  public void onClose(Session sesija) {
    sesije.remove(sesija);
  }

  @OnError
  public void onError(Session session, Throwable throwable) {

  }

  public static void proslijediObavijest(String obavijest) {
    for (Session sesija : sesije) {
      try {
        sesija.getBasicRemote().sendText(obavijest);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}