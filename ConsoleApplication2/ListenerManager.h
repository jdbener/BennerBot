#include "PluginBase.h"
#include "PluginReference.h"
#include "MessageReference.h"
#include <string>
#include <vector>

#pragma once
class ListenerManager {
public:
	void addListener(PluginBase listener);
	void removeListener(std::string);
	void removeListener(int);
	bool hasListener(std::string);
	int getListenerID(std::string);
	std::string getListenerName(int);
	void sendMessage(std::string, MessageReference);
	void sendMessage(int, MessageReference);
	void sendMessage(MessageReference);
	void receiveMessage(std::string, MessageReference);
	void receiveTaggedMessage(std::vector<PluginReference::PluginTags>, std::string, MessageReference);

private:
	std::vector<PluginBase> listeners = std::vector<PluginBase>();
};

