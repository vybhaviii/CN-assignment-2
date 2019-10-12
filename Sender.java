import java.io.*;
import java.net.*;

public class Sender{
Socket sender;
ObjectOutputStream out=null;
ObjectInputStream in=null;
String packet,ack,str, msg;
int n,i=0,sequence=0;
String recseq=null;

Sender(){}

public void run(){
 try{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
System.out.println("Waiting for Connection....");
 sender = new Socket("localhost",20084);
sequence=0;
out=new ObjectOutputStream(sender.getOutputStream());
out.flush();
 in=new ObjectInputStream(sender.getInputStream());
 str=(String)in.readObject();
 System.out.println("reciever     > "+str);
System.out.println("Enter the data to send....");
packet=br.readLine();
n=packet.length();
do{
try{
if(i<n){
msg=String.valueOf(sequence);
msg=msg.concat(packet.substring(i,i+1));
}
else if(i==n){
msg="end";
out.writeObject(msg);
break;
}
out.writeObject(msg);
sequence=(sequence==0)?1:0;
out.flush();
System.out.println("data sent>"+msg);
Thread.sleep(3000);
//System.out.println("Time out resending data....\n\n");
System.out.println("data sent>"+msg);
try{
	sender.setSoTimeout(1000);
ack=(String)in.readObject();
}
catch(Exception e){
	System.out.println("Exception in getting ack");
}
System.out.println("waiting for ack.....\n\n");
if(ack!=null)
	recseq=String.valueOf(sequence);
else
	recseq ="1";
if(ack.equals(recseq)){
i++;
System.out.println("receiver   >  "+" packet recieved\n\n");
}
else{
System.out.println("Time out resending data....\n\n");
sequence=(sequence==0)?1:0;
}
}
catch(Exception e){}
}
while(i<n+1);
System.out.println("All data sent. exiting.");
}
catch(Exception e){}
finally{
try{
in.close();
out.close();
sender.close();
}
catch(Exception e){}
}
}

public static void main(String args[]){
Sender s=new Sender();
s.run();
}
}
