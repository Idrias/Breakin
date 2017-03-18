import processing.net.*;
import java.io.*;
import java.util.Map;

final String NET_SPLITSTRING = "-NEXT-";
final int ACTORTYPE_DUMMY = 3;

BreakinGame BREAKINGAME;
GameServer gameServer;
GameClient gameClient;

// Settings //
float NETWORK_UPDATERATE = 20;           // How often per second do we want to send updates from server to client?
boolean CLIENTSIDE_PREDICTIONS = true;   // Should the gameclient predict movements?
//////////////


void setup_gvars() {
  BREAKINGAME = this;
  gameServer = new GameServer();
  gameClient = new GameClient();
}


void printFPS() {
  println("FPS: " + frameRate);
  println();
}