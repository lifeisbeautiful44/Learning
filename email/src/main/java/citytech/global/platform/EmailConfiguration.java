package citytech.global.platform;

import citytech.global.resource.payload.EmailDetailsPayload;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailConfiguration {
    private EmailConfiguration(){}

    public static void sendMail(EmailDetailsPayload emailDetailsPayload){
        try{
            MimeMessage mimeMessage = new MimeMessage(getSession());
            mimeMessage.setFrom(emailDetailsPayload.getFrom());
            mimeMessage.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(emailDetailsPayload.getTo())});
            mimeMessage.setSubject(emailDetailsPayload.getSubject());
            mimeMessage.setContent(emailDetailsPayload.getHtmlContent(),"text/html");
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    private static Session getSession(){

        //Creating session
        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("srijansil.bohara444.lhng@gmail.com","heal nghm chup qiwk");
            }
        });
        session.setDebug(true);
        return session;
    }
    private static Properties getProperties(){
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        //setting host properties
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");
        return properties;
    }
}
