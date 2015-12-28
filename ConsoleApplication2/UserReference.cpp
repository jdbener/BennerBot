#include "stdafx.h"
#include "UserReference.h"

using namespace std;

static Color defaultColors[15] = { Color("#FF0000"),	//Red
									Color("#0000FF"),	//Blue
									Color("#00FF00"),	//Green
									Color("#B22222"),	//Fire Brick
									Color("#FF7F50"),	//Coral
									Color("#9ACD32"),	//Yellow Green
									Color("#FF4500"),	//Orange Red
									Color("#2E8B57"),	//Sea Green
									Color("#DAA520"),	//Golden Rod
									Color("#D2691E"),	//Chocolate
									Color("#5F9EA0"),	//Cadet Blue
									Color("#1E90FF"),	//Dodger Blue
									Color("#FF69B4"),	//HotPink
									Color("#8A2BE2"),	//Blue Violet
									Color("#00FF7F") };	//Spring Green

UserReference::UserReference(string name, Color color, UserReference::PermissionLevels level, string pIdent) {
	this->name = name;
	IC.insert(IC.end, pair<string,Color>(pIdent, color));
	permLevel = level;
	banned = 0;
}

UserReference::UserReference(string name, UserReference::PermissionLevels level, string pIdent) {
	this->name = name;
	permLevel = level;
	banned = 0;
	IC.insert(IC.end, pair<string, Color>(pIdent, defaultColors[(name[0] + name[name.length() - 1]) % 15]));
}

UserReference::UserReference(string name, PermissionLevels level, map<string, Color> identifierColorPairs) {
	this->name = name;
	permLevel = level;
	IC = identifierColorPairs;
}

string UserReference::getName() {
	return name;
}

vector<string> UserReference::getSourceIdentifiers() {
	vector<string> v;
	for (map<string, Color>::iterator it = IC.begin(); it != IC.end(); ++it) {
		v.push_back(it->first);
	}
	return v;
}

UserReference::PermissionLevels UserReference::getPermissionLevel() {
	return permLevel;
}

string UserReference::getPermissionLevel(bool useless) {
	switch (permLevel) {
	case regular: return "regular";
	case regularPlus: return "regular+";
	case moderator: return "moderator";
	case admin: return "administrator";}
	return "default";
}

vector<Color> UserReference::getChatColors() {
	vector<Color> v;
	for (map<string, Color>::iterator it = IC.begin(); it != IC.end(); ++it) {
		v.push_back(it->second);
	}
	return v;
}

map<string, Color> UserReference::getIdentifierColorPairs() {
	return IC;
}

Color UserReference::getChatColor(string pIdent) {
	return IC.at(pIdent);
}

bool UserReference::isBanned() {
	if (banned > 0)
		return false;
	return true;
}

int UserReference::getBannedTime() {
	return banned;
}

void UserReference::setPermisionLevel(UserReference::PermissionLevels level) {
	permLevel = level;
}

void UserReference::addIdentiferColorPair(string pIdent, Color color) {
	IC.insert(IC.end, pair<string, Color>(pIdent, color));
}

void UserReference::setBanned(int time) {
	banned = time;
}