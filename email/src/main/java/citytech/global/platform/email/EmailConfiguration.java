package citytech.global.platform.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailConfiguration {
    private EmailConfiguration(){}

    public static void sendMail(EmailDetails emailDetails){
        try{
            MimeMessage mimeMessage = new MimeMessage(getSession(emailDetails.from()));
            mimeMessage.setFrom(emailDetails.from());
            mimeMessage.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(emailDetails.to())});
            mimeMessage.setSubject(emailDetails.subject());
            mimeMessage.setContent(emailDetails.htmlContent(),"text/html");
            Transport.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    private static Session getSession(String email){

        //Creating session
        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(email,"heal nghm chup qiwk");
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
