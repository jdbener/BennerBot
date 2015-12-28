#include "stdafx.h"
#include "MessageReference.h"

using namespace std;

MessageReference::MessageReference(string message, UserReference user, string source) {
	this->message = message;
	this->user = user;
	this->source = source;
}

MessageReference::MessageReference(string message, UserReference user) {
	this->message = message;
	this->user = user;
}

string MessageReference::getMessage() {
	return message;
}

UserReference MessageReference::getUser() {
	return user;
}

string MessageReference::getSourcePlugin() {
	return source;
}