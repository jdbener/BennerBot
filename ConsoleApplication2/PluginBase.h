#include "PluginReference.h"
#include "UserReference.h"
#include "MessageReference.h"
#include <string>
#include <vector>

#pragma once
class PluginBase {
public:
	PluginBase(std::string, std::string, std::string, std::vector<PluginReference::PluginTags>);
	std::string getPluginIdentifier();
	std::string getPluginName();
	std::string getPluginDescription();
	std::vector<PluginReference::PluginTags> getPluginTags();
	bool isLoaded();
	std::string getListenerName(std::string);
	std::string getListenerName(int);
	void handleMessage(std::string, UserReference);
	void handleTaggedMessage(std::vector<PluginReference::PluginTags>, std::string, UserReference);
	void handleTaggedMessage(PluginReference::PluginTags target, std::string message, UserReference user);
	virtual void sendMessage(MessageReference message);
	virtual void loadPlugin();
	virtual void unloadPlugin();

private:
	PluginReference ref;
	bool loaded = false;
	void trueLoad();
	void trueUnload();

friend class ListenerManager;
};

