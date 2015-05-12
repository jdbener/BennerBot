Hello, my name is Jdbener and today I am going to give you a basic walk through of how to use my twitch/hitbox chatbot, BennerBot! Bennerbot has been designed differently than other chatbots, it is designed by streamers for streamers, it has been designed to manage both twitch and hitbox simultaneously, it has also been written in java so it can be used from anywhere! If you have any bug reports or feature requests feel free to post an issue on the GitHub page and will try my best to include your input.

There are 4 steps to running BennerBot:

1. Configuration: in the config folder is a file called config.yml. Open this folder in notepad or whatever you use, and edit it to your liking it is well commented
 and you should have no problem figuring out what things do.
There is also a file called commands.yml. Use this file to decide what commands you want
(Dont touch any of the other files unless you know what you are doing)

2. Jar: in the main folder there will a file, bennerbot.jar, this file will run the bot.

3. WebDisplay: in the main folder is a file called index.html, if you have bennerbot.jar running you will be able to view the chat in this file instead of the included GUI.
If you want to embead the chat into something you can acsess a raw feed in resource/output-dirty.txt or a cleaned up version at output.txt, for all you people who want to do cool stuff!

4. Extensions: there is a folder called plugins, you can add plugins to this folder if you want to exend bennerbots abilities. 
There is a file called BasicCommands.java that file contains a simple example plugin.

**If you ever find a bug or have and idea for a feature please post a comment [here](https://github.com/jdbener/BennerBot/issues)**

**Auto Installer can be found [here](https://github.com/jdbener/BennerBot/releases/tag/autoupdater) or as a:**

1. **[Windows Executable](https://github.com/jdbener/BennerBot/releases/download/autoupdater/BennerBot.exe)**

2. **[Cross Platform JAR](https://github.com/jdbener/BennerBot/releases/download/autoupdater/BennerBot.jar)**

**[Check out our Website!!](http://jdbener.github.io/BennerBot)**

**If you like what im doing feel free to: [![alt text](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif "Title")](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=XF5ZN3N5ULFAE)**

(Post an Issue if i should add anything else to this file)

##Features:##

1. Accessible, Bennerbot is written in java so it can be used anywhere. We also dont have limitations who can use it.

2. Extendible, Bennerbot has been designed to be very extendible with a plugin system with access to all parts of the bot

3. Community Drive, Every step I take in design BennerBot is inspired, suggested, written, or requested by the community.

4. API Integration, Bennerbot has been designed to integrate closely with twitch, hitbox, and many more to come.

5. Server Mindful, Bennerbot's design is server friendly, just pop it in, start it up and be free!

##Changelog:##
**Alpha Versions:**

- Mk1: This is the original version including basic chat commands !hello and !time as well as join messages, a chat relay, and basic chat output. Designed to work with both Twitch.tv and Hitbox.tv.

- Mk2: Compressed the bot into 1 file from two, updated README

- MK3: Cleaner right! Did alot of under the hood work to clean up the file system, make the bot faster, add emoticon support, add a color system. Oh and did i mention the GUI? No? Well we have a GUI now!

- MK4: The Plugin Update! Added the plugin system, as well as alot of underthehood changes.

- MK5: The Realise Update! This is the first update that was officially realised to the public, as well as a custom command system and variables. I also did a few changes to the GUI to make it more usable and/or chroma-able.

- MK6: (mk5.1) This update simply adds the ability to send messages in chat

- MK7: The GUI Update! This update completely redesigned the GUI that the bot uses, it fixes many issues that the old gui had, as well as allowing the bot to be run through the terminal!

- MK8: (mk7.1) This update simplifies the GUI and fixes some of the bugs from previous version, we are also taking our first steps towards being open source

- MK9: This update involves alot of new changes, mainly: GUI Updates, Twitch Emoticon Support, More API Work, and source!!! Yes you can finally look at my ugly source.

- MK10: (mk9.1) This update fixes the huge amount of memory used by the emoticon system in the last update

- MK11: The Quality of Life Update! A bunch of general fixes to improve the quality of life for you the user!

- MK12: The Automatic Update! Added an auto updater and some more quality of life stuff

- MK13: The Bountiful Update! Added Automatic messages, Better Custom messages and A countdown. As well as more quality of life stuff. Removed herobrine

- MK14: The Remote Update! This update changed the database system around a little, so that it is based off of a remote database, or a local database depending on the operator's preference!

- MK15: The GUI Update (MK2)! This update replaces the old gui with a better more butifull one!

- MK16: The Socket.IO Update! This update includes some basic support for socket.io chat servers (aka hitbox)

##Libraries Used:##

- [PIRCBotX](http://code.google.com/p/pircbotx/) ~ License: This software product is OSI Certified Open Source Software, available under the GNU General Public License (GPL). Since the GPL may be too restrictive for use in proprietary applications, a commercial license is also provided.

- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)/[Apache Commons Codec](https://commons.apache.org/proper/commons-codec/) ~ [License](http://www.apache.org/licenses/)

- [Google Guava](https://github.com/google/guava) ~ [License](https://github.com/google/guava/blob/master/COPYING)

- [slf4j](http://www.slf4j.org/) ~ [License](http://www.slf4j.org/license.html)

- [JSON-Simple](http://code.google.com/p/json-simple/) ~ [License](http://www.apache.org/licenses/LICENSE-2.0)

- [Java Simple Plugin System](http://code.google.com/p/jspf/) ~ [License](http://opensource.org/licenses/BSD-3-Clause)

- [JYAML](http://jyaml.sourceforge.net/) ~ [License: JYaml has a BSD style open-source license.](http://opensource.org/licenses/BSD-3-Clause)

- [LogBack](http://logback.qos.ch/) ~ [License](http://logback.qos.ch/license.html)

- [SQLite-JDBC](https://bitbucket.org/xerial/sqlite-jdbc) ~ [License](http://www.apache.org/licenses/)

##Licence:##
Copyright © 2014 Joshua Dahl (jdbener@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish and/or distribute the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

2. The Software is never to be sold or other wise distributed for commercial gain.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.