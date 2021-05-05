package uz.java.hr.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.sql.Timestamp;


@Component
public class EmailSender {
    @Autowired
    JavaMailSender javaMailSender;


    public boolean sendEmail(String sendingEmail, String text) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("moreply@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Yangi xabar");
            mailMessage.setText(text);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public boolean mailText (String sendingEmail, String emailCode, String password)  {
        String link = "http:localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Emailni tasdiqlash</a>\n" +
                        "<br>\n" +
                        "<p>Parolingiz: <b> " + password + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"></p>";

        return sendEmail(sendingEmail, text);
    }


    public boolean givingTaskForEmail(String takerEmail, String taskName, String taskGiver, String taskDescription,
                                          Timestamp deadline)  {
        String link = "http:localhost:8080/api/auth/verifyTask?email=" + takerEmail + "&taskName=" + taskName;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Vazifani boshlaganliginggizni tasdiqlang</a>\n" +
                        "<br>\n" +
                        "<p>Vazifa nomi: <b> " + taskName + "</b></p>\n" +
                        "<p>Vazifa izohi: <b> " + taskDescription + "</b></p>\n" +
                        "<p>Vazifa beruvchi: <b> " + taskGiver + "</b></p>\n" +
                        "<p>Vazifa deadline: <b> " + deadline + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"></p>";


        return sendEmail(takerEmail, text);
    }

    public boolean mailCompleteTask(String taskGiver, String taskName, String taskAnswer, String taskTaker)    {
//        String link = "http:localhost:8080/api/auth/completeTask?taskName=" + taskName;
        String text =
                        "<p> <b> "+taskTaker+"Xodim vazifani jo`natdi </b></p>\n" +
                        "<p>Vazifa nomi: <b> " + taskName + "</b></p>\n" +
                        "<p>Vazifa javobi: <b> " + taskAnswer + "</b></p>\n" +
                        "<p>Vazifa Bajaruvchi: <b> " + taskTaker + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"><b>Omad yor bo'lsin!</b></p>";
        return sendEmail(taskGiver, text);
    }




}
