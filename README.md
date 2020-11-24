



# CS 4390 Project
The group project for CS 4390 (Computer Networks) @ UTD. The Goal is to create a Server/Client chat utilizing UDP and TCP sockets. 

This project was written using Intellij IDEA 2020.2.3 (Community Edition), using OpenJDK 14. 

## Execution
Execution of the code is fairly simple because we went ahead and bundled the program into an executable `.jar` file. Since this code has two parts, here are two different sets of instructions:

### ... the Server
This should be run first. Open a terminal window and navigate to the root directory of the project: `cs-4390-project/`. Then type in the following commands:

	~/cs-4390-project~$ cd runnables
	~/cs-4390-project/runnables~$ cd server
	~/cs-4390-project/runnables/server~$ java -jar server.jar

And now your server should be running. It won't immediately print anything, but if you just hit the `ENTER` key, you'll see a debug printout. Leave this terminal window open as we open the client.

### ... and the Client
Open another terminal window and navigate to the root directory of the project: `cs-4390-project/`. Then type in the following commands

	~/cs-4390-project~$ cd runnables
	~/cs-4390-project/runnables~$ cd client
	~/cs-4390-project/runnables/client~$ java -jar client.jar

You can do this however many times you want to create several instances of the client. **Make sure the server is open while you use the client**

### Client-ID/Password Combinations
Here is a table of the client-id/password combinations of our subscribers. They are here for you to use to login.
| **Client ID** | **Password** |
|--|--|
| austin-li | `yo123` |
| joseph-norman | `toe123` |
| josh-guzman | `bro123` |
| kevin-salinda | `moe123` |
| user1 | `pass` |
| user2 | `pass` |
| user3 | `pass` |
| user4 | `pass` |
| user5 | `pass` |
| user6 | `pass` |
| user7 | `pass` |
| user8 | `pass` |
| user9 | `pass` |
| user10 | `pass` |

## How we are formatting Packet Data
Because this program requires two different programs to talk to each other via UDP/TCP, this README will document the types of packets that will be sent between the server and the client.

In an effort to standardize the formatting of these packets, we will use JSON, a system that allows an array of `<key, value>` pairs to be formatted into a string, then converted back. The specific details of this will be documented in `server/README.md` and `client/README.md`, but the important part is that the strings can be parsed into `byte[]` streams which can be sent using Java's TCP/UDP libraries. For more information on the JSON format, read [here](https://www.softwaretestinghelp.com/json-tutorial/).


## Packet Documentation
The following is a listing of all of the different packets we could possible send. Each packet has an identifier that is used to identify what the client or the server should do with it. This identifier is called the *receiver* and will look like this in the JSON: 

    {
	    "receiver": "HELLO",
	    ...
    }
When the server receives this packet, it knows that it is the Hello Packet and should look for a `CLIENT-ID-A`

---
### Hello Packet
The initial packet sent from the Client to the Server via UDP.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID-A` | String | `joseph-norman-dev` | The ID of the client that is connecting to the server |

#### Example Packet:
	{
		"receiver": "HELLO",
		"CLIENT-ID-A": "joseph-norman"
	}
    
---
### Challenge Packet
The packet sent via UDP from server to client that challenges the client to authenticate itself.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `RAND` | String | `1938182376` | The random number that the client will use to authenticate itself using the MD5 algorithm |

#### Example Packet:
	{
		"receiver": "CHALLENGE",
		"RAND": "1938182376"
	}
    
---
### Response Packet
The packet sent via UDP from client to server that the server uses to respond to the challenge provided. The value of `RES` is dictated by the formula:
```		
String K_A = "toe123"; //the client's secret key (password) 
String RAND = "1938182376"; //value sent from server in the CHALLENGE Packet 
String RES = MD5(K_A + "," + RAND);
```

| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID` | String | `joseph-norman` | The client id of the Subscriber trying to respond to the challenge |
| `RES` | String | `C2E67DCE66A2651386F6054FA112E61D` | The hash provided by the MD5 algorithm |

#### Example Packet:
	{
		"receiver": "CHALLENGE",
		"CLIENT-ID": "joseph-norman",
		"RES": "961E5D0F404509D43AE2EFFF24489F7D"
	}
    
---
### Auth Success Packet
The packet that is sent via UDP from the server to the client once the client is authenticated (if the proper value of `RES` is found in the Response Packet). It instructs the client to reconnect to the provided TCP port and to offer its cookie once connected.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `PORT` | Integer | `8001` | The TCP port number that the client should connect to |
| `RAND-COOKIE` | String | `1938182376` | The same string sent by the Challenge packet |

#### Example Packet:
	{
		"receiver": "AUTH-SUCCESS",
		"PORT": "8001",
		"RAND-COOKIE": "1938182376"
	}
	
---
### Auth Fail Packet
The packet that is sent via UDP from the server to the client because it failed to authorize. The client will not be instructed to connect to a specific TCP port
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|

#### Example Packet:
	{
		"receiver": "AUTH-FAIL"
	}
---
### Connect Packet
The packet that is sent via TCP from client to server to authenticate the client and authorize further communication over TCP.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `RAND-COOKIE` | String | `1938182376` | A completely random string that the client uses to authenticate itself |

#### Example Packet:
	{
		"receiver": "CONNECT",
		"RAND-COOKIE": "1938182376"
	}
---
### Connected Packet
The packet that is sent via TCP from the server to the client in order to verify that the client was authorized.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|

#### Example Packet:
	{
		"receiver": "CONNECTED"
	}
---
### Chat Request Packet
The packet that is sent via TCP from the client to the server to request a Session be created between the original sender and client B
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID-B` | String | `austin-li` | The Client ID of the other subscriber |
#### Example Packet:
	{
		"receiver": "CHAT-REQUEST",
		"CLIENT-ID-B": "austin-li"
	}
---
### Chat Started Packet
The packet that is sent via TCP from the server to clients A and B that they are to be conjoined in a chat Session
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID-B` | String | `austin-li` | The Client ID of the other subscriber in the Session |
| `SESSION-ID` | String | `1` | The Session ID of Session |
#### Example Packet:
	{
		"receiver": "CHAT-STARTED",
		"CLIENT-ID-B": "austin-li",
		"SESSION-ID": "1"
	}
---
### Chat Message Packet
The packet that is sent via TCP between the clients, relayed by the server. This packet contains the actual chat message and the session id
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `SESSION-ID` | String | `1` | Session ID number |
| `CHAT-MESSAGE` | String | `hello` | The chat message sent |
#### Example Packet:
	{
		"receiver": "CHAT",
		"SESSION-ID: "1",
		"CHAT-MESSAGE": "hello"
	}
---
### History Request Message Packet
The packet that is sent via TCP by the client A to request history of past chat messages with client b.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID-B` | String | `josh-guzman` | Client-ID of the subscriber that client A is requesting chat history with |
#### Example Packet:
	{
		"receiver": "HISTORY_REQ",
		"CLIENT-ID-B": "josh-guzman"		
	}
---
### End Notification Message Packet
The packet that is sent via TCP by the server to the client involved in a session that the session has been terminated by another client.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `SESSION-ID` | String | `1` | The Session-ID of sendind session |
#### Example Packet:
	{
		"receiver": "END-NOTIF",
		"SESSION-ID: "1"		
	}
---
### History Reponse Message Packet
The packet that is sent via TCP by the server to the client who requested the history.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID` | String | `josh-guzman` | Client-ID of the client who sent the chat message |
| `CHAT-MESSAGE` | String | `hello` | The chat message sent |
| `SESSION-ID` | String | `1` | The Session-ID of the chat message |
#### Example Packet:
	{
		"receiver": "CHAT",
		"CLIENT-ID": "josh-guzman", 
		"CHAT-MESSAGE": "hello",
		"SESSION-ID: "1"		
	}
---
### Unreachable Packet
The packet that is sent via TCP from the server to the client to notify client A that client B was unreachable. Either they are not connected or are already in a Session with someone else
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `CLIENT-ID-B` | String | `austin-li` | The Client ID of the unreachable subscriber |
#### Example Packet:
	{
		"receiver": "UNREACHABLE",
		"CLIENT-ID-B": "austin-li"
	}
---

