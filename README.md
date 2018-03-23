# Distributed Download Manager
DDM is a command line tool written in Java to distrbute large scale downloads on different machines to utilize high speed interanet connected to low speed internet.

__It is strongly advised not to run this on just a single machine__

### Steps to make it run
1. Decide a server(computer) to make run the tracker. Run the file server.java on that machine. The tracker runs on the port 5000 on that machine.
2. Enter the ip adress of machine on which tracker runs in line 111 in server.java and line 74 in downloader.java in the folder APP
3. Complile all the files in the APP folder and then run the MainApp.
4. Enter the download link in the terminal
5. If some other peers are present, the download gets distributed on them otherwise it is downloaded fully by the app itself.
6. Close the app after one download.
7. Each App instance has a server running on the machine that handles download for other clients and a client that manages download for this machine.
