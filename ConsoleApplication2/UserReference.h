#include "Color.h"
#include <string>
#include <map>
#include <vector>

#pragma once

class UserReference {
	enum PermissionLevels { base, regular, regularPlus, moderator, admin };

	UserReference(string, Color, PermissionLevels, string);
	UserReference(string, PermissionLevels, string);
	UserReference(string, PermissionLevels, map<string, Color>);

public:
	string getName();
	vector<string> getSourceIdentifiers();
	PermissionLevels getPermissionLevel();
	string getPermissionLevel(bool);
	vector<Color> getChatColors();
	Color getChatColor(string);
	map<string, Color> getIdentifierColorPairs();
	bool isBanned();
	int getBannedTime();
	void setPermisionLevel(PermissionLevels);
	void addIdentiferColorPair(string, Color);
	void setBanned(int);
	
private: 
	PermissionLevels permLevel;
	string name;
	int banned;
	map<string, Color> IC;
};

