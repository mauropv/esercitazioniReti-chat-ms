import socket
import json
import time
import os


HOST = "13.69.80.156"
#HOST = "localhost"
PORT = 80

number = int(input('Enter your favorite number:'))

def recv_all(sock, length):
    data = ''
    while len(data) < length:
        read_data = sock.recv(length - len(data))
        if read_data.decode() == '':
            #raise EOFError('socket closed')
            return data
        data += read_data.decode()
    return data

def cls():
    os.system('cls' if os.name=='nt' else 'clear')


sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall(("GET /log/chat/hello?numeroPreferito="+str(number)+" HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nUser-Agent: chat-client\r\n\r\n").encode())
message = recv_all(sock, 1024) 
sock.close()
print(repr(message))
lines = message.splitlines()
jsonMessage = json.loads(lines[9])
#print repr(lines)
nonce = jsonMessage['nonce']






name=input('Enter your name:')
password=input('Enter your password:')
##Eseguo Autenticazione
authBody = "{\"matricola\":\"xx\",\"nickname\":\""+name+"\",\"password\":\""+password+"\",\"nonce\":\""+str(nonce)+"\"}"
authCall = "POST /log/chat/auth HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nContent-Type: application/json\r\nContent-Length: "+str(len(authBody))+"\r\nUser-Agent: chat-client\r\n\r\n"+authBody+"\r\n\r\n"
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall(authCall.encode())
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
    comando = input('inserisci comando')
    if comando == 'list':
	##Voglio stampare la lista dei presenti
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((HOST,PORT))
        sock.sendall(b"GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nConnection: close\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
        message = recv_all(sock,1024)
	#print repr(message)
        sock.close()
        lines = message.splitlines()
	#print lines
        jsonMessage = json.loads(lines[9])
	#print jsonMessage
        print("Amici Presenti:")
        for amico in jsonMessage:
                print(amico)

