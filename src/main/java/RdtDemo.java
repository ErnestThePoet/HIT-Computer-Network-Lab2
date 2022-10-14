import controllers.ReceiverController;
import controllers.TransmitterController;

public class RdtDemo {
    public static void main(String[] args) {
        TransmitterController transmitterController=new TransmitterController(8897,8895);
        ReceiverController receiverController=new ReceiverController(5965);

//        transmitterController.sendWithRdt("Je Suis Ernest", 0.3,32,2000);
//        transmitterController.sendWithGbn("Je Suis Ernest",0.2,5,30,2000);
//        transmitterController.sendWithGbn("Je Suis Ernest",0.2,5,30,2000);
//        transmitterController.sendFileWithSr("D:/jane-eyre.txt",50,20,10000);
//        receiverController.receiveFileWithSr("D:/recvSRA.txt",23);
//        transmitterController.sendFileWithGbn("D:/jane-eyre.txt",50,20,10000);

    }
}
