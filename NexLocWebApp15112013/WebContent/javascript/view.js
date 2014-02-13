function mostrar(div){
	var entidad = div.split("-");
	$("."+entidad[1]).hide();
	$("#"+div).show();
	
}


/* Opcion activa del menu */
function active_menu(){
	var url = ""+window.location;
	var pagina = url.split("/");
	if(pagina[pagina.length-1]==""){
		$("#menu span .index").parent().addClass("active");
	}else{
		var name = pagina[pagina.length-1].split(".");
		var submenu = name[0].split("-");
		if(submenu.length>1){
			name[0] = submenu[0];
		}
		try{
			var lista = [];
			lista = $("#menu span").get();
	
			for(var i=0; i<lista.length;i++){
				if(name[0] == $(lista[i]).find("div").attr("class")){
					$(lista[i]).addClass("active");
					var backgroundPos = $(lista[i]).find("div").css('background-position').split(" ");
					//now contains an array like ["0%", "50px"]
					
					var xPos = backgroundPos[0];
					
					$(lista[i]).find("div").css('backgroundPosition', xPos+" -24px");
				}
			}
		}catch(e){
		
		}
	}
}


$(document).ready(function(){
	active_menu();
	$(".active").parent().find(".submenu").show();

	/* Manejo de Submenus */
	$("#menu span").click(function(){
		if($(this).parent().find(".submenu") != null){
			if($(this).parent().find(".submenu").is(":hidden")){
				//$(".submenu").css("display","none");
				/*Inicio 28/06/2013 MDTECK Sandy Huanco*/
				$("#menu span").parent().find(".submenu").slideUp(500);
				/*Fin*/
				$(this).parent().find(".submenu").slideDown(500).show();				
			}else{				
				$(this).parent().find(".submenu").slideUp(500);
			}
		}else{
			$(this).find("a").trigger("click");
		}
	});
	
	/* Efecto menu - hover */
	if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
		$("#menu span").mouseenter(function(){
			var backgroundPos = $(this).find("div").css('background-position').split(" ");		
			var xPos = backgroundPos[0];
			
			$(this).find("div").css('backgroundPosition', xPos+" -24px");
		}).mouseleave(function(){
			if($(this).attr("class") != "active"){
				var backgroundPos = $(this).find("div").css('background-position').split(" ");
				
				var xPos = backgroundPos[0];
		
				$(this).find("div").css('backgroundPosition', xPos+" 0px");
			}
			
		});
	}
});