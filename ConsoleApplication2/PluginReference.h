#include "UserReference.h"
#include <string>
#include <vector>	

#pragma once
class PluginReference {
public:
	enum PluginTags { CONNECTIONPROVIDER, FILTER, UI, UTILITY, UNTAGGED, ALL };
	PluginReference(std::string, std::string, std::vector<PluginTags>);
	PluginReference(std::string, std::string, std::string, std::vector<PluginTags>);
	std::string getIdentifier();
	std::string getName();
	std::string getDescription();
	std::vector<PluginTags> getTags();
	void addUser(UserReference);
	void removeUser(std::string);
	void removeUser(int);
	UserReference getUser(int);
	UserReference getUser(std::string);

private: 
	std::string pIdent, name, desc = "";
	std::vector<PluginTags> tags;
	std::vector<UserReference> users;
};

