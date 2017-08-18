function validarEmail(valor) {
	if (!/^(?:[^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*|"[^\n"]+")@(?:[^<>()[\].,;:\s@"]+\.)+[^<>()[\]\.,;:\s@"]{2,63}$/i.test(valor)){
		alert("La dirección de email es incorrecta!.");
	} 
}

function validarNumero(valor){
	if (!/^([0-9])*$/.test(valor)){
		alert("El valor " + valor + " no es un número");
	}
} 

function validarNumeroDecimal(valor){
	if (!/^([0-9])*.?([0-9])*$/.test(valor)){
		alert("El valor " + valor + " no es un número");
	}
} 

function validarAlfaNumerico(valor){
	if (!/^[a-zA-ZñÑ\s\W]/.test(valor)){
		alert("El valor " + valor + " no es un dato aceptado");
	}
} 
