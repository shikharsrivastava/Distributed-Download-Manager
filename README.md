# Distributed Download Manager
DDM is a command line tool written in Java to distrbute large scale downloads on different machines to utilize high speed interanet connected to low speed internet.

__It is strongly advised not to run this on just a single machine__

### Steps to make it run
1. Connect all the computers through a network. (Wifi Hotspot works fine)
2. Decide a server(computer) to make run the tracker. Run the file server.java on that machine. The tracker runs on the port 5000 on that machine.
3. Enter the ip adress of machine on which tracker runs in line 111 in server.java and line 74 in downloader.java in the folder APP
4. Copy this code to all the machines who want to participate in the downloading.
5. Complile all the files in the APP folder and then run the MainApp.
6. Enter the download link in the terminal
7. If some other peers are present, the download gets distributed on them otherwise it is downloaded fully by the app itself.
8. Close the app after one download.
9. Each App instance has a server running on the machine that handles download for other clients and a client that manages download for this machine.

### Make it run as a standalone downloader
These are the steps to run this on just a single machine as a downloader.
1. Run the tracker server (server.java) on the machine.
2. Enter the ip adress of machine on which tracker runs in line 111 in server.java and line 74 in downloader.java in the folder APP
3. Complile all the files in the APP folder and then run the MainApp.
4. Enter the download link in the terminal