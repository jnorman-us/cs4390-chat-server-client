
# CS 4390 Project
The group project for CS 4390 (Computer Networks) @ UTD. The Goal is to create a Server/Client chat utilizing UDP and TCP sockets. 

## Futher Documentation
There are actually two different IntelliJ Projects in this repository, one for the Client program and the other for the Server. Because of this, there are two separate README fields within each folder `server/README.md` and `client/README.md` which will document each program individually.

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
		"CLIENT-ID-A": "joseph-norman-dev"
	}
    
---
### Auth Success Packet
The packet that is sent via UDP from the server to the client once the client is authorized. It instructs the client to reconnect to the provided TCP port and to offer its cookie once connected.
| **Key** | **Type** | **Example Value** | **Description** |
|--|--|--|--|
| `PORT` | Integer | `5000` | The TCP port number that the client should connect to |
| `RAND-COOKIE` | String | `gRx57aF!eE4?` | A completely random string that the client will use once it connects to the TCP port |

#### Example Packet:
	{
		"receiver": "AUTH-SUCCESS",
		"PORT": "5000",
		"RAND-COOKIE": "gRx57aF!eE4?"
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
| `RAND-COOKIE` | String | `gRx57aF!eE4?` | A completely random string that the client uses to authenitcate itself |

#### Example Packet:
	{
		"receiver": "CONNECT",
		"RAND-COOKIE": "gRx57aF!eE4?"
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
