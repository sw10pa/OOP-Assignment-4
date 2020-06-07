#Before You Start Testing

###About ```*.txt``` files

I have divided project into four directories.
Text files that you'll use for testing are located in the the directory of its own problem.
So, to use them when passing their names as program arguments you have to append ```problem_directory_name/``` before file name
(e.g. ```Bank/5k.txt```, ```WebLoader/links.txt```, etc.).

###About WebLoader's ```Stop``` button

Because of this piece of code: ```connection.setConnectTimeout(5000);```
which was provided in the instructions, program doesn't stop immediately after pressing ```Stop``` button.

###About fetched bytes' count

For some reasons program fetches identical html codes from the majority of urls, containing 162 bytes each.
In my opinion, given urls do not exist anymore, therefore program gets ```Object not found!``` code.
