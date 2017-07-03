$(document).ready(function(){
	
$("#validateLogin").click(function() {
		var alias = $("#alias").val();
		var password = $("#password").val();
		$.ajax({
			  method: "POST",
				url: "/validateUsuario",
				data: { alias: alias, password: password }, 
			    success:function(data) {
			      alert(data); 
			    }
			  });
	});
});