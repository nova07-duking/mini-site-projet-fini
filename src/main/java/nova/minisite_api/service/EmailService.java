package nova.minisite_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerEmailOtp(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("raphsadibi@gmail.com");
        message.setTo(to);
        message.setSubject("Activation de votre compte - Code OTP");
        message.setText("Bonjour,\n\nVotre code de vérification est : " + code +
                "\n\nIl expirera dans 5 minutes.");

        mailSender.send(message);
    }
}
