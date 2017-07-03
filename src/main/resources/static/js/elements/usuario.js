$(document).ready(function(){
	
$("#createUsuario").click(function() {
		var nombre = $("#nombre").val();
		var alias = $("#alias").val();
		var email = $("#email").val();
		var password = $("#password").val();
		
		$.ajax({
			  method: "POST",
					url: "/createUsuario",
					data: { nombre: nombre, alias: alias, email: email, password: password}, 
				    success:function(data) {
				    	alert(data); 
				    }
			  });
	});
});