import socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  
MAX = 65535
PORT = 1060
HOST = ""
sock.bind((HOST,PORT))
sock.listen(5)  

def recv_all(sock, length):
    data = ''
    while len(data) < length:
        read_data = sock.recv(length - len(data))
        if read_data.decode() == '':
            #raise EOFError('socket closed')
            return data
        data += read_data.decode()
    return data

while 1:
    sock_cli, addr = sock.accept()  
    message = recv_all(sock_cli, 16)
    print('The incoming sixteen-octet message says', repr(message))
    sock_cli.sendall(b'Hello World!')  
    sock_cli.close()
    print('Reply sent, socket closed')

