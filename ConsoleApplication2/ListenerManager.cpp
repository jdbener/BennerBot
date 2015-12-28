#include "stdafx.h"
#include "ListenerManager.h"

using namespace std;

void ListenerManager::addListener(PluginBase listener) {
	ListenerManager::listeners.insert(listeners.end, listener);
	listener.trueLoad();
}

void ListenerManager::removeListener(std::string lIdent) {
	for (int i = 0; i < listeners.size(); i++) {
		if (listeners.at(i).getPluginIdentifier().compare(lIdent) == 0) {
			listeners.at(i).trueUnload();
			listeners.erase(listeners.begin + i);
		}
	}
}

void ListenerManager::removeListener(int lIdent) {
	if (lIdent > listeners.size())
		return;
	if (lIdent < 0)
		return;

	listeners.at(lIdent).trueUnload();
	listeners.erase(listeners.begin + lIdent);
}

bool ListenerManager::hasListener(std::string lIdent) {
	for (int i = 0; i < listeners.size(); i++) 
		if (listeners.at(i).getPluginIdentifier().compare(lIdent) == 0)
			return true;
	return false;
}

int ListenerManager::getListenerID(std::string lIdent) {
	for (int i = 0; i < listeners.size(); i++) 
		if (listeners.at(i).getPluginIdentifier().find(lIdent))
			return i;
	return -1;
}

std::string ListenerManager::getListenerName(int lIdent) {
	if (lIdent > listeners.size())
		return "";
	if (lIdent < 0)
		return "";

	return listeners.at(lIdent).getPluginName();
}

void ListenerManager::sendMessage(std::string lIdent, MessageReference reference) {
	for (int i = 0; i < listeners.size(); i++) 
		if (listeners.at(i).getPluginIdentifier().compare(lIdent) == 0) 
			listeners.at(i).sendMessage(reference);
}

void ListenerManager::sendMessage(int lIdent, MessageReference reference) {
	if (lIdent > listeners.size())
		return;
	if (lIdent < 0)
		return;

	return listeners.at(lIdent).sendMessage(reference);
}

void ListenerManager::sendMessage(MessageReference reference) {
	for (int i = 0; i < listeners.size(); i++)
		sendMessage(i, reference);
}

void ListenerManager::receiveMessage(std::string lIdent, MessageReference reference) {
	//These two blocks of code check if the Listener Identifier passed represents a listener in the system or not.
	//If not then it will stop the function and return an error
	bool has = false;
	for (PluginBase l : listeners) {
		if (l.getPluginIdentifier().compare(lIdent) == 0) {
			has = true;
			break;
		}
	}
	if (has == false)
		return;

	//This block of code sends a copy of the message to every other listener/plugin so that it can be processed by all of them accordingly
	for (int i = 0; i < listeners.size(); i++)
		if (!listeners.at(i).getPluginIdentifier().compare(lIdent) == 0)
			sendMessage(i, reference);
}

void ListenerManager::receiveTaggedMessage(std::vector<PluginReference::PluginTags> tags, std::string lIdent, MessageReference reference) {
	//These two blocks of code check if the Listener Identifier passed represents a listener in the system or not.
	//If not then it will stop the function and return an error
	bool has = false;
	for (PluginBase l : listeners) {
		if (l.getPluginIdentifier().compare(lIdent) == 0) {
			has = true;
			break;
		}
	}
	if (has == false)
		return;

	//This block of code sends a copy of the message to every other listener/plugin so that it can be processed by all of them accordingly
	for (int i = 0; i < listeners.size(); i++)
		if (!listeners.at(i).getPluginIdentifier().compare(lIdent) == 0)
			for (PluginReference::PluginTags tag : listeners.at(i).getPluginTags())
				for (PluginReference::PluginTags tar : tags)
					if (tag == tar || tar == PluginReference::PluginTags::ALL)
						sendMessage(i, reference);
}