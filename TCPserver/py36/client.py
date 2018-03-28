import socket
#sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  
HOST = "localhost"
PORT = 1060

def recv_all(sock, length):
    data = ''
    while len(data) < length:
        read_data = sock.recv(length - len(data))
        if read_data.decode() == '':
            #raise EOFError('socket closed')
            return data
        data += read_data.decode()
    return data

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST,PORT))
sock.sendall(b"ciaociaociaociao")
message = recv_all(sock,1024)
sock.close()
print(repr(message))
