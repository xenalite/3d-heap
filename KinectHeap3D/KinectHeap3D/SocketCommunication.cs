using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace KinectHeap3D
{
    class SocketCommunication : IDisposable
    {

        private readonly Socket clientSocket;

        public SocketCommunication(string ip, int port)
        {
            try
            {
                IPEndPoint serverAddress = new IPEndPoint(IPAddress.Parse(ip), port);
                clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                clientSocket.Connect(serverAddress);
            }
            catch (Exception){}
        }

        public void sendMessage(string toSend)
        {
            int toSendLen = System.Text.Encoding.ASCII.GetByteCount(toSend);
            byte[] toSendBytes = System.Text.Encoding.ASCII.GetBytes(toSend);
            byte[] toSendLenBytes = System.BitConverter.GetBytes(toSendLen);
            clientSocket.Send(toSendLenBytes);
            clientSocket.Send(toSendBytes);
        }

        public string waitToRecieveMessage()
        {
            byte[] rcvLenBytes = new byte[4];
            clientSocket.Receive(rcvLenBytes);
            int rcvLen = System.BitConverter.ToInt32(rcvLenBytes, 0);
            byte[] rcvBytes = new byte[rcvLen];
            clientSocket.Receive(rcvBytes);
            String rcv = System.Text.Encoding.ASCII.GetString(rcvBytes);

            return rcv;
        }

        public void Dispose()
        {
            clientSocket.Close();
        }

        public bool isConnected()
        {
            return clientSocket != null && clientSocket.Connected;
        }
    }
}
