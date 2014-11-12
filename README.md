Hello, my name is Jdbener and today i am going to give you a basic walkthrough of how to use my twitch/hitbox chatbot, BennerBot! Bennerbot has been designed differently than other chatbots, it is designed by streamers for streamers, it has been designed to manage both twitch and hitbox simoltaniously, it has also been writen in java so it can be used from anywhere! If you have any bug reports or feature requests feal free to email me: jdbener@gmail.com and will try my best to include your input.

There are 4 steps to runing BennerBot:
1. Configuration: in the config folder is a file called config.yml. Open this folder in notepad or whatever you use, and edit it to your liking it is well commented
 and you should have no problem figuring out what things do.
There is also a file called commands.yml. Use this file to decide what commands you want
Dont touch any of the other files unless you know what you are doing

2. Jar: in the mainfolder there will a file, bennerbot.jar, this file will run the bot.

3. WebDisplay: in the main folder is a file called index.html, if you have bennerbot.jar runing you will be able to view the chat in this file instead of the included GUI.
If you want to embead the chat into something you can acsess a raw feed in resource/output-dirty.txt or a cleaned up version at output.txt, for all you people who want to do cool stuff!

3.5. Extensions: there is a folder called plugins, you can add pluggins to this folder if you want to exend bennerbots abilities. 
There is a file called BasicCommands.java that file contains a simple example plugin.

(Post an Issue if i should add anything else to this file)

=================================================================================

*Changelog:*
Alpha Versions:

- Mk1: This is the original version including basic chat commands !hello and !time as well as join messages, a chat relay, and basic chat output. Designed to work with both Twitch.tv and Hitbox.tv.

- Mk2: Compressed the bot into 1 file from two, updated README

- MK3: Cleaner right! Did alot of under the hood work to clean up the file system, make the bot faster, add emoticon support, add a color system. Oh and did i mention the GUI? No? Well we have a GUI now!

- MK4: The Plugin Update! Added the plugin system, as well as alot of underthehood changes.

- MK5: The Realise Update! This is the first update that was officially realised to the public, as well as a custom command system and varibles. I also did a few changes to the GUI to make it more usable and/or chroma-able.

- MK6: (mk5.1) This update simply adds the ability to send messages in chat

- MK7: The GUI Update! This update completely redesigned the GUI that the bot uses, it fixes many issues that the old gui had, as well as allowing the bot to be run through the terminal!

- MK8: (mk7.1) This update simplifies the GUI and fixes some of the bugs from previous version, we are also taking our first steps twards being open source


=================================================================================

*Licence:*
Copyright © 2014 Joshua Dahl (jdbener@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.