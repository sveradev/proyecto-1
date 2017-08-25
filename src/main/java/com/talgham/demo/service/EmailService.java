package com.talgham.demo.service;

import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.talgham.demo.model.Email;
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
	
	public Email addEmail (String direccion, String actividad, String subject, String texto) {

		Email emailModel = new Email();
		
		emailModel.setDireccion(direccion);
		emailModel.setActividad(actividad);;
		emailModel.setSubject(subject);
		emailModel.setTexto(texto);
		emailModel.setFechaCreacion(new Date());
		emailRepository.save(emailModel);
		return emailModel;
	}
	
	public Email buscarPorActividad(String actividad){
		return emailRepository.findByActividad(actividad);
	}

	public Iterable<Email> getAllEmails() {
		return emailRepository.findAll();
	}

	public Email buscarPorId(Long id) {
		return emailRepository.findById(id);
	}

	public void guardarEmail(Email email) {
		emailRepository.save(email);
	}
}
