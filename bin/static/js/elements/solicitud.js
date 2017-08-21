$(document).ready(function(){
	
$("#createSolicitud").click(function() {
		var nombre = $("#nombre").val();
		var titulo = $("#titulo").val();
		var email = $("#email").val();
		var descripcion = $("#descripcion").val();
		
		$.ajax({
			  method: "POST",
				url: "/addSolicitud",
				data: { nombre: nombre, titulo: titulo ,email: email, descripcion: descripcion }, 
			    success:function(data) {
			      alert(data); 
			    }
			  });
	});

});
