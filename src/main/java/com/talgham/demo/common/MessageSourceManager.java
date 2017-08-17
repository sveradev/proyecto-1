package com.talgham.demo.common;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class MessageSourceManager {
	
	private static MessageSourceManager instance;
	
	private Locale locale;
	private MessageSource messageSource;
	
	private MessageSourceManager() {}
	
	public static MessageSourceManager getInstance() {
		if(instance == null) {
			instance = new MessageSourceManager();
		}
		return instance;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public String getMessage(String code) {
		return getMessage(code, null);
	}
	
	public String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, code, locale);
	}
	public String getMessage(String code, Object arg) {
		Object[] args = {arg};
		return getMessage(code, args);
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
