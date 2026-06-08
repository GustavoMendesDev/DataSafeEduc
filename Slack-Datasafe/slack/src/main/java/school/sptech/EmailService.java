package school.sptech;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private final NotificacaoConfig config;

    public EmailService(NotificacaoConfig config) {
        this.config = config;
    }

    public void enviarEmail(InatividadeUsuario usuario, String periodoFormatado) {
        if (!config.isEmailConfigurado()) {
            System.out.println("E-mail não configurado. Defina SMTP_HOST, SMTP_USER e SMTP_PASSWORD.");
            return;
        }

        if (usuario.email() == null || usuario.email().isBlank()) {
            System.out.println("Usuário sem e-mail cadastrado: " + usuario.nome());
            return;
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", String.valueOf(config.isSmtpStartTls()));
        properties.put("mail.smtp.host", config.getSmtpHost());
        properties.put("mail.smtp.port", config.getSmtpPort());

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getSmtpUser(), config.getSmtpPassword());
            }
        });

        try {
            Message mensagem = new MimeMessage(session);
            mensagem.setFrom(new InternetAddress(config.getSmtpFrom()));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usuario.email()));
            mensagem.setSubject("Alerta de inatividade - DataSafe Educ");
            mensagem.setText("""
                    Olá, %s.

                    Identificamos que você está inativo no DataSafe Educ há %s.

                    Acesse o sistema para continuar acompanhando seus dados educacionais.
                    %s
                    """.formatted(usuario.nome(), periodoFormatado, config.getLinkSistema()));

            Transport.send(mensagem);
            System.out.println("E-mail de inatividade enviado para " + usuario.email());
        } catch (Exception erro) {
            System.err.println("Erro ao enviar e-mail para " + usuario.email() + ": " + erro.getMessage());
        }
    }
}
