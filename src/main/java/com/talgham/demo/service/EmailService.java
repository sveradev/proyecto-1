package com.talgham.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.talgham.demo.common.Constantes;
import com.talgham.demo.model.Email;
import com.talgham.demo.repository.EmailRepository;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void prepareAndSend(String to, String subject, String text) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(text);
		};
		try {
			mailSender.send(messagePreparator);
		} catch (MailException e) {
		// runtime exception; compiler will not force you to handle it
		}
	}

	public String crearEmail (Email email) {
		email.setFechaCreacion(new Date());
		emailRepository.save(email);
		return Constantes.GUARDADO;
	}
	
	public Email buscarPorActividad(Long actividad){
		return emailRepository.findByActividad_id(actividad);
	}

	public Iterable<Email> getAllEmails() {
		return emailRepository.findAll();
	}

	public Email buscarPorId(Long id) {
		return emailRepository.findById(id);
	}

	public String guardarEmail(Email email) {
		email.setFechaModificacion(new Date());
		emailRepository.save(email);
		return Constantes.GUARDADO;
	}
}
