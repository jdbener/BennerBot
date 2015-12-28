#include "stdafx.h"
#include "PluginReference.h"

PluginReference::PluginReference(std::string identifier, std::string name, std::vector<PluginTags> tags) {
	pIdent = identifier;
	this->name = name;
	this->tags = tags;
}

PluginReference::PluginReference(std::string identifier, std::string name, std::string description, std::vector<PluginTags> tags) {
	pIdent = identifier;
	this->name = name;
	this->desc = description;
	this->tags = tags;
}

std::string PluginReference::getIdentifier() {
	return pIdent;
}

std::string PluginReference::getName() {
	return name;
}

std::string PluginReference::getDescription() {
	return desc;
}

std::vector<PluginReference::PluginTags> PluginReference::getTags() {
	return tags;
}

void PluginReference::addUser(UserReference ref) {
	users.insert(users.begin, ref);
}

void PluginReference::removeUser(std::string name) {
	for (int i = 0; i < users.size(); i++) {
		if (users.at(i).getName().equalsIgnoreCase(name)) {
			users.erase(users.begin() + i);
		}
	}
}

void PluginReference::removeUser(int i) {
	users.erase(users.begin() + i);
}

UserReference PluginReference::getUser(std::string name) {
	for (UserReference ref : users)
		if (ref.getName().equalsIgnoreCase(name))
			return ref;
}

UserReference PluginReference::getUser(int i) {
	return users.at(i);
}