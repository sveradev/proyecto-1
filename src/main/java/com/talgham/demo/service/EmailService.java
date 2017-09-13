package com.talgham.demo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.talgham.demo.common.Constantes;
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
		
		/** MultiPart para crear mensajes compuestos. */
		MimeMultipart multipart = new MimeMultipart("related");
		BodyPart messageBodyPart = new MimeBodyPart();
    	messageBodyPart.setContent(text, "text/html");
		multipart.addBodyPart(messageBodyPart);
		
		helper.setText(text);
		

		sender.send(message);

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
	
	public static String getEmailTemplate(String templateHtml, Map<String, Object> model) {
		MustacheFactory mf = new DefaultMustacheFactory();
		String result = null;
		FileReader fr;
		try {
			//lee el archivo html y lo trasforma a un string
			fr = new FileReader(templateHtml);
			BufferedReader br= new BufferedReader(fr);
			StringBuilder content=new StringBuilder(1024);
			String s = null;
			while((s=br.readLine())!=null) {
				content.append(s);
			}
			br.close();
			//crea un compilador de mopustache, necesita un stream de lectura del string que creamos antes (strinReader)
			Mustache template = mf.compile(new StringReader(content.toString()), content.toString());
			StringWriter writer = new StringWriter();
			
			//Ejecuta el template, con el stream de lectura, el de escitura y el modelo a popular
			template.execute(writer, model);
			writer.flush();
			result = writer.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//como resultado tenemos un lindo string con html populado
		return result;
	}
}
