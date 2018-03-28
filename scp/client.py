from **.all import *

A = '192.168.1.149' # spoofed source IP address
B = '13.95.11.192' # destination IP address
C = 10000 # source port
D = 80 # destination port
payload = "yadssssssa yada yada" # packet payload

spoofed_packet = IP(src=A, dst=B) / TCP(sport=C, dport=D) / payload
send(spoofed_packet)
