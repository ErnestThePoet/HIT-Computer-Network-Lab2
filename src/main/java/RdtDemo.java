import controllers.TransmitterController;

public class RdtDemo {
    public static void main(String[] args) {
        TransmitterController controller=new TransmitterController(8897,8895);

        controller.sendWithGbn("Je Suis Ernest",0.7,5,100,2000);
    }
}
