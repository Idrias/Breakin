import processing.net.*;
import java.io.*;
import java.util.Map;

final String NET_SPLITSTRING = "-NEXT-";
final int ACTORTYPE_DUMMY = 3;

int servertime = 0;
int clienttime = 0;

BreakinGame BREAKINGAME;
GameServer gameServer;
GameClient gameClient;

void setup_gvars() {
  BREAKINGAME = this;
  gameServer = new GameServer();
  gameClient = new GameClient();
}