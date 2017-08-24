package com.talgham.demo.service;

import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.talgham.demo.model.Email;
import com.talgham.demo.model.Solicitud;
import com.talgham.demo.repository.EmailRepository;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private EmailRepository emailRepository;

	public void sendEmail(String to, String subject, String text) throws Exception {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);

		sender.send(message);
	}
	
	public Email addEmail (String email, String proceso, String subject, String texto) {

		Email emailModel = new Email();
		
		emailModel.setEmail(email);
		emailModel.setProceso(proceso);
		emailModel.setSubject(subject);
		emailModel.setTexto(texto);
		emailRepository.save(emailModel);
		emailModel.setFechaCreacion(new Date());
		return emailModel;
	}
	
	public Email buscarPorProceso(String proceso){
		return emailRepository.findByProceso(proceso);
	}

	public Iterable<Email> getAllEmails() {
		return emailRepository.findAll();
	}
	
	public String enviarMailPor(String proceso, Object[] object ){
		Email emailTemplate = this.buscarPorProceso(proceso);
		if(emailTemplate != null){
			String to = emailTemplate.getEmail();
			String subject = emailTemplate.getSubject();
			String texto = emailTemplate.getTexto();
		
			try {
				emailService.sendEmail(to, subject, texto);
			} catch (Exception e) {
				e.printStackTrace();
				return "Hubo un error al enviar el mail."
			}
		} else {
			return "No se ha encontrado un email configurado para la acción requerida";
		}
		return "enviado";
	}
}
