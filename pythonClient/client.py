import socket
import json
import time
import os


HOST = "13.69.80.156"
#HOST = "localhost"
PORT = 80

number = int(raw_input('Enter your favorite number:'))

def recv_all(sock, length):
    data = ''
    while len(data) < length:
        read_data = sock.recv(length - len(data))
        if read_data == '':
            #raise EOFError('socket closed')
            return data
        data += read_data
    return data

def cls():
    os.system('cls' if os.name=='nt' else 'clear')

#HELLO
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall("GET /log/chat/hello?numeroPreferito="+`number`+" HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nUser-Agent: chat-client\r\n\r\n".encode("utf8"))
message = recv_all(sock, 1024) 
sock.close()
#print repr(message)
lines = message.splitlines()
jsonMessage = json.loads(lines[9])
#print repr(lines)
nonce = jsonMessage['nonce']






#AUTH
name=raw_input('Enter your name:')
password=raw_input('Enter your password:')
##Eseguo Autenticazione
authBody = "{\"matricola\":\"xx\",\"nickname\":\""+name+"\",\"password\":\""+password+"\",\"nonce\":\""+`nonce`+"\"}"
authCall = "POST /log/chat/auth HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nContent-Type: application/json\r\nContent-Length: "+`len(authBody)`+"\r\nUser-Agent: chat-client\r\n\r\n"+authBody+"\r\n\r\n"
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall(authCall)
message = recv_all(sock,1024)
#print repr(message)
sock.close()
lines = message.splitlines()
#print lines
jsonMessage = json.loads(lines[9])
#print repr(lines)
token = jsonMessage['token']
#print token
##Ho il token :D



##Voglio stampare la lista dei presenti
#sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#sock.connect((HOST,PORT))
#sock.sendall("GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
#message = recv_all(sock,1024)
#print repr(message)
#sock.close()



##Voglio leggere i messaggi per me
#sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#sock.connect((HOST,PORT))
#sock.sendall("GET /log/chat/allMessagesForMe?nickname="+name+" HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
#message = recv_all(sock,1024)
#print repr(message)
#sock.close()

while 1:
    comando = raw_input('inserisci comando')
    if comando == 'list':
	##Voglio stampare la lista dei presenti
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall("GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
	message = recv_all(sock,1024)
	#print repr(message)
	sock.close()
	lines = message.splitlines()
	#print lines
	jsonMessage = json.loads(lines[9])
	#print jsonMessage
	print "Amici Presenti:"
	for amico in jsonMessage:
		print amico


    if comando=="update":
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall("GET /log/chat/allMessagesForMe?nickname="+name+" HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
	message = recv_all(sock,1024)
	sock.close()
	#print repr(message)
	lines = message.splitlines()
	#print lines
	jsonMessage = json.loads(lines[9])
	#print jsonMessage
	for messaggio in jsonMessage:
		print messaggio["src"] + ":\t"+messaggio["text"]
	

    if comando=="send":
	dest = raw_input('inserisci destinatario')
	text = raw_input('inserisci messaggio')
	

	##Invio un messaggio
	sendBody = "{\"nickname\":\""+name+"\",\"dstnickname\":\""+dest+"\",\"text\":\""+text+"\",\"timestamp\":1234}"
        sendCall = "PUT /log/chat/sendMessage HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nContent-Type: application/json\r\nContent-Length: "+`len(sendBody)`+"\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n"+sendBody+"\r\n\r\n"
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall(sendCall)
	message = recv_all(sock,1024)
	print repr(message)
	sock.close()

    if comando == 'list-loop':
	while 1:
		##Voglio stampare la lista dei presenti
		sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		sock.connect((HOST,PORT))
		sock.sendall("GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
		message = recv_all(sock,1024)
		#print repr(message)
		sock.close()
		lines = message.splitlines()
		#print lines
		jsonMessage = json.loads(lines[9])
		#print jsonMessage
		cls()
		print "Amici Presenti:"
		for amico in jsonMessage:
			print amico
		time.sleep(3)

    if comando == 'update-loop':
        cls()
        print "Messaggi in arrivo per "+name
        while 1:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((HOST,PORT))
            sock.sendall("GET /log/chat/allMessagesForMe?nickname="+name+" HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
            message = recv_all(sock,1024)
            sock.close()
            #print repr(message)
            lines = message.splitlines()
            #print lines
            if len(lines)>9:
                jsonMessage = json.loads(lines[9])
                #print jsonMessage
                for messaggio in jsonMessage:
                    print messaggio["src"] + ":\t"+messaggio["text"]
            time.sleep(1)

	
    if comando == "send-loop":
        while 1:
            dest = raw_input('inserisci destinatario')
            text = raw_input('inserisci messaggio')
            sendBody = "{\"nickname\":\""+name+"\",\"dstnickname\":\""+dest+"\",\"text\":\""+text+"\",\"timestamp\":1234}"
            sendCall = "PUT /log/chat/sendMessage HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nContent-Type: application/json\r\nContent-Length: "+`len(sendBody)`+"\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n"+sendBody+"\r\n\r\n"
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.connect((HOST,PORT))
            sock.sendall(sendCall)
            message = recv_all(sock,1024)
            sock.close()
            if message.startswith("HTTP/1.1 201"):
                print "message sent"
            else:
                print "send error... :("
            ##print repr(message)
            

