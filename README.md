# DoubleDB

This is my practice project where I want to use two databases, MySQL and maybe Cassandra or some other nosql db.
It will be console based project and I want to practice some design patterns while writing code.
I will also use encription for user passwords, which will be stored into MySQL db. So I will be making
user registration and login logic with validation.

Later in project I want to store some data in NoSQL db and test its capabilities. I want to be able to decide
from which db I want to pull data. 

So far this is my idea for this project which I'm sure will change in future.

25/04/2018  
So far I did user login and registration, I've added encryption using AES algorithm. At first I was considering 
using Twofish but after some research I've decided to go with AES. 
I've aranged my db for user data to have two tables. In first I store userName and encrypted userPassword
while userID is auto incremented. In second table I'm storing that userID and encrypted secret key.
I'm using that key during loging to reconstruct encrypted userPassword and compare it with stored one.
I've read about some safety concerns about handling passwords as Strings, at that point I was already 
too far into my project so I didn't pay too much attention about that. But I've tried to avoid using String type.
There are some bugs in program atm, but its functionality is behaving as I wanted. I'll try to polish some code 
and fix some bugs but I'm glad that I'm almost finished with user login and registration.

Next thing to focus on will be adding additional database. Which will be NoSQL one as written in prior text.
I think I'll go with Cassandra but after some digging I'll be sure to know what are my next steps to take, and 
where do I want to go with this practice project.

20/05/2018  
I've implemented Cassandra as second database. I've used DataStax Java Driver for Cassandra, so at this point I did simple implementation to learn how things work. I've set up Cassandra, created keyspace, added "table", inserted data into database and selected wanted data. My plan is to learn more about Cassandra, to expand the code surrounding it. Later I'll see how the data is fed to it, maybe I'll do some IoT part of the program.
