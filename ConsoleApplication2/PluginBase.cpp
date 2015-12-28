#include "stdafx.h"
#include "PluginBase.h"

PluginBase::PluginBase(std::string name, std::string identifier, std::string description, std::vector<PluginReference::PluginTags> tags) {
	ref = PluginReference(identifier, name, description, tags);
}

std::string PluginBase::getPluginIdentifier() {
	return ref.getIdentifier();
}

std::string PluginBase::getPluginName() {
	return ref.getName();
}

std::string PluginBase::getPluginDescription() {
	return ref.getDescription();
}

std::vector<PluginReference::PluginTags> PluginBase::getPluginTags() {
	return ref.getTags();
}

bool PluginBase::isLoaded() {
	return loaded;
}

std::string PluginBase::getListenerName(std::string pInit) {
//TODO add code
}

std::string PluginBase::getListenerName(int ID) {
//TODO add code
}

void PluginBase::handleMessage(std::string message, UserReference user) {
//TODO add code
}

void PluginBase::handleTaggedMessage(std::vector<PluginReference::PluginTags>, std::string message, UserReference user) {
	//TODO add code
}

void PluginBase::handleTaggedMessage(PluginReference::PluginTags target, std::string message, UserReference user) {
	//TODO add code
}

void PluginBase::trueLoad() {
	loaded = true;
	loadPlugin();
}

void PluginBase::trueUnload() {
	loaded = false;
	unloadPlugin();
}