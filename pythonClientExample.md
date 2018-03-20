import socket
import json
import time

current_milli_time = lambda: int(round(time.time() * 1000))


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


sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall("GET /log/chat/hello?numeroPreferito="+`number`+" HTTP/1.1\r\nHost: 13.69.80.156\r\nUser-Agent: chat-client\r\n\r\n".encode("utf8"))
message = recv_all(sock, 1024)
sock.close()
print repr(message)
lines = message.splitlines()
jsonMessage = json.loads(lines[8])
#print repr(lines)
nonce = jsonMessage['nonce']

name=raw_input('Enter your name:')
password=raw_input('Enter your password:')

##Eseguo Autenticazione
authBody = "{\"matricola\":\"xx\",\"nickname\":\""+name+"\",\"password\":\""+password+"\",\"nonce\":\""+`nonce`+"\"}"
authCall = "POST /log/chat/auth HTTP/1.1\r\nHost: 13.69.80.156\r\nContent-Type: application/json\r\nContent-Length: "+`len(authBody)`+"\r\nUser-Agent: chat-client\r\n\r\n"+authBody+"\r\n\r\n"
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall(authCall)
message = recv_all(sock,1024)
#print repr(message)
sock.close()
lines = message.splitlines()
#print lines
jsonMessage = json.loads(lines[8])
#print repr(lines)
token = jsonMessage['token']
#print token
##Ho il token :D



##Voglio stampare la lista dei presenti
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall("GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
message = recv_all(sock,1024)
print repr(message)
sock.close()



##Voglio leggere i messaggi per me
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall("GET /log/chat/allMessagesForMe?nickname="+name+" HTTP/1.1\r\nHost: 13.69.80.156\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
message = recv_all(sock,1024)
print repr(message)
sock.close()

while 1:
    comando = raw_input('inserisci comando')
    if comando == 'list':
	##Voglio stampare la lista dei presenti
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall("GET /log/chat/friends HTTP/1.1\r\nHost: 13.69.80.156\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
	message = recv_all(sock,1024)
	print repr(message)
	sock.close()
    if comando=="update":
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall("GET /log/chat/allMessagesForMe?nickname="+name+" HTTP/1.1\r\nHost: 13.69.80.156\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n")
	message = recv_all(sock,1024)
	print repr(message)
	sock.close()

    if comando=="send":
	dest = raw_input('inserisci destinatario')
	text = raw_input('inserisci messaggio')


	##Invio un messaggio
	sendBody = "{\"nickname\":\""+name+"\",\"dstnickname\":\""+dest+"\",\"text\":\""+text+"\",\"timestamp\":1234}"
        sendCall = "PUT /log/chat/sendMessage HTTP/1.1\r\nHost: 13.69.80.156\r\nContent-Type: application/json\r\nContent-Length: "+`len(sendBody)`+"\r\nAuthorization: "+token+"\r\nUser-Agent: chat-client\r\n\r\n"+sendBody+"\r\n\r\n"
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect((HOST,PORT))
	sock.sendall(sendCall)
	message = recv_all(sock,1024)
	print repr(message)
	sock.close()


