#include "UserReference.h"
#include <string>

#pragma once
class MessageReference {

public:
	MessageReference(string, UserReference, string);
	MessageReference(string, UserReference);
	string getMessage();
	UserReference getUser();
	string getSourcePlugin();

private:
	string message, source="";
	UserReference user;
};

