import processing.net.*;

BreakinGame GAME;
GameServer gameserver;
GameClient gameclient;

final int CREATE_SERVERSIDE = -1;
int uidcount;

void setupVars() {
  GAME = this;
  uidcount = 0;
}