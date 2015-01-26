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



Licence:
Copyright © 2014 Joshua Dahl (jdbener@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish and/or distribute the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

2. The Software is never to be sold or other wise distributed for commercial gain.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.