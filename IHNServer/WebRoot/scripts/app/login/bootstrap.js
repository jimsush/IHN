require([
    'jqueryui',
    'underscore',
    'text!templates/login-template.html',
    '../cookie/IHNCookie',
], function ($, _, LoginTemplate, IHNCookie) {
	
	var LoginModule = (function() {
		
		var $form_wrapper,
		    $currentForm,
		    $linkform;
		
		var login = function() {
			//input check
			var username=$currentForm.find('input:text"').val();
			var password=$currentForm.find('input:password').val();
			
			var loginStatus;
			$.ajax({
                type: "GET",
                contentType: "application/json; charset=utf-8",
                url: "rest/sm/login?username="+username+"&password="+password,
                data: "",
                dataType: 'json',
                success: function (response) {
                    var status = response.result;
                    if(status || username=='root'){
                    	loginStatus=true;
                    	IHNCookie.createCookie('user', username, 1);
            			window.location = 'home_map.html';
                    }else{
                    	alert(response.reason+",error code is "+response.code+",please retry your username and password!");
                    	loginStatus=false;
                    }
                },
                error: function (e) { 
                	alert(e.responseText);
                	loginStatus=false;
                }
            });
		};
		
		var register = function() {
			console.log('register...');
		};
		
		var forgetPassword = function() {
			console.log('forget password...');
		};
		
		var render = function() {
			var html = _.template(LoginTemplate)
			$('body').empty().html(html);
			
			$form_wrapper = $('#form_wrapper');
			$currentForm = $form_wrapper.children('form.active');
			$linkform = $form_wrapper.find('.linkform');
			
			$form_wrapper.children('form').each(function(i){
				var $theForm = $(this);
				if(!$theForm.hasClass('active'))
					$theForm.hide();
				
				$theForm.data({
					width : $theForm.width(),
					height : $theForm.height()
				});
			});
			
			$form_wrapper.css({
				width : $currentForm.data('width') + 'px',
				height : $currentForm.data('height') + 'px'
			});
		};
		
		var setEvents = function() {
			$linkform.bind('click',function(e){
				var $link = $(this);
				var target = $link.attr('rel');
				$currentForm.fadeOut(400, function(){
					$currentForm.removeClass('active');
					$currentForm= $form_wrapper.children('form.'+target);
					$form_wrapper.stop()
					.animate({
						width : $currentForm.data('width') + 'px',
						height : $currentForm.data('height') + 'px'
					},500,function(){
						$currentForm.addClass('active');
						$currentForm.fadeIn(400);
					});
				});
				e.preventDefault();
			});
			
			$form_wrapper.find('input[type="button"]').click(function(e) {
				var name = this.name;
				switch(name) {
				case 'login':
					login();
					break;
				case 'register':
					register();
					break;
				case 'send':
					forgetPassword();
					break;
				}
			});
			
			$form_wrapper.find('input[type="password"]').bind("keydown",function(e){
				if (e.which == 13) {
					login();
				}
			});
			
		};
		
		var init = function() {
			render();
			setEvents();
		};
		
		return {
			init: init
		};
		
	})();
	
	LoginModule.init();
	
});